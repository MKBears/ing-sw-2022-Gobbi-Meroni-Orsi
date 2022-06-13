package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manages all the interactions between Controller (server) and the remote player (client)
 */
public class ClientHandler extends Thread{
    private final Socket socket;
    private final Server serve;
    MessageFromClient in;
    Message4Client out;
    private Controller controller;
    private String userName;
    private Colors color;
    private Wizards wizard;
    private Player avatar;
    private int movedStudentsNumber;
    private int state;
    private Match match;
    private boolean expertMatch;
    private boolean connected;
    private boolean ongoingMatch;
    private AssistantCard playedAssistant;
    private Student movedStudent;
    private int movedStudentPosition;
    private int motherNatureSteps;
    private Cloud chosenCloud;
    private boolean nack;
    private int nackCounter;

    /**
     *
     * @param s the socket associated with this player
     */
    public ClientHandler (Socket s, Server server){
        socket = s;
        this.serve = server;
        connected = true;
        ongoingMatch = false;

        try {
            out = new Message4Client(socket);
            in = new MessageFromClient(socket, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connected = true;
        nackCounter = 0;
    }

    /**
     *
     * The run part of the ClientHandler thread: calls changeState() and wakes up and sleeps
     */
    public void run(){
        in.start();
        //System.out.println("Stream in ingresso partito");

        do {
            try {
                changeState();

                synchronized (this) {
                    do {
                        System.out.println("Player " + userName + ": dormo");
                        this.wait();
                        System.out.println("Player " + userName + ": sveglio");
                    } while (!controller.isMyTurn(this));
                }
            } catch (InterruptedException | SocketException e) {
                out.sendGenericError("Internal server error ("+e.getMessage()+")");
                state = 8;
            }
        } while (ongoingMatch);
    }

    /**
     * Is the finite state machine thart controls the single client (therefore the single player). It is made of cases that simulate the states of the ideal machine
     * @throws InterruptedException
     * @throws SocketException
     */
    private void changeState() throws InterruptedException, SocketException {
        synchronized (this) {
            switch (state) {
                case 0:
                    //LogIn/Registration, match preparation (create/join match, choose the wizard)
                    do {
                        //System.out.println("Aspetto il client");
                        wait();
                        //System.out.println("Let's-a goooo!");

                        if (serve.getUserNames().contains(userName) && serve.inactivePlayer(this)) {
                            //System.out.println("Okie-dokie!");
                            out.sendLoginSucceeded();
                            System.out.println("Login avvenuto con successo: "+userName);
                            nack = false;
                        } else {
                            out.sendLoginFailed();
                            nack = true;
                        }
                    } while (nack);

                    if (serve.canConnectPlayer(userName)) {
                        try {
                            serve.joinGame(null, this);
                        } catch (Exception e) {
                            out.sendGenericError("Unable to connect to the match ("+e.getMessage()+")");
                        }
                    }
                    else {
                        out.sendListOfGames(serve.getJoinableMatches(), serve.getPausedMatches(userName));
                        //System.out.println("Dopo listOfGames");

                        do {
                            wait();
                        } while (nack);
                        //System.out.println("Mando maghi");

                        do {
                            out.sendWizard(controller.getWizards());
                            wait();
                        } while (nack);
                        out.sendACK();
                        controller.chooseWizard(wizard);
                        //System.out.println("helooo");
                        createAvatar(color, controller.getPlayersNum(), expertMatch);

                        controller.createMatch();
                        //wait();
                    }

                    do {
                        System.out.println("Player "+userName+": aspetto la creazione del match");
                        this.wait();
                    } while (!controller.readyToStart());

                    do {
                        out.sendCreation(controller.getMatch());
                        System.out.println("Mandata creation");
                        wait();
                    } while (nack);
                    out.start();
                    socket.setSoTimeout(5000);
                    state = 1;
                    break;
                case 1:
                    //PLANNING phase: notify refilled clouds
                    do {
                        out.sendRefillClouds(match.getCloud());
                        System.out.println("Player "+userName+": mando le nuvole riempite");
                        wait();
                    } while (nack);

                    synchronized (controller) {
                        System.out.println("notificato");
                        controller.notify();
                    }
                    state = 2;
                    break;
                case 2:
                    //PLANNING phase: play an assistant card
                    do {
                        out.sendChooseCard(playableAssistants(controller.getPlayedAssistants()));
                        wait();
                        System.out.println("Player "+userName+": ricevuto assistente");
                    } while (nack);
                    System.out.println("Player "+userName+" dopo while");
                    avatar.draw(playedAssistant);
                    System.out.println("Player "+userName+": gioco assistente");
                    controller.playAssistantCard(playedAssistant, this);

                    if (avatar.hasNoCardsLeft()) {
                        controller.notifyEndedAssistants(this);
                    }
                    state = 3;
                    break;
                case 3:
                    //ACTION phase: moving students from the entrance
                    //check if they can control any professor
                    do {
                        out.sendMoveStudents();
                        /*if(controller.getCurrentPlayer()!=controller.getFirstPlayer()){
                            wait();
                        }*/
                        System.out.println(this.avatar.getUserName()+": sto aspettando");
                        wait();
                    } while (nack);
                    System.out.println(this.avatar.getUserName()+": uscito dal wait");//////

                    for (int i=0; i<movedStudentsNumber; i++) {
                        System.out.println(this.avatar.getUserName()+": sono dentro al for");
                        if (movedStudentPosition == 12) {
                            try {
                                avatar.getBoard().placeStudent(movedStudent);
                                System.out.println(this.avatar.getUserName()+": situa 1");
                            }
                            catch (Exception e) {
                                out.sendGenericError("Desynchronized ("+e.getMessage()+")");
                                out.sendCreation(match);
                                i--;
                            }
                        } else {
                            for (Land land : match.getLands()) {
                                if (land.getID() == movedStudentPosition) {
                                    land.addStudent(avatar.getBoard().removeStudent(movedStudent));
                                    System.out.println(this.avatar.getUserName()+": situa 2");
                                }
                            }
                        }
                        System.out.println("sono QUIIIII");
                        controller.notifyMovedStudent(this, movedStudent, movedStudentPosition);

                        if (i < movedStudentsNumber-1) {
                            System.out.println(this.avatar.getUserName()+": altra wait");
                            wait();
                        }
                    }
                    checkAllProfessors();
                    controller.notifyProfessors();
                    state = 4;
                case 4:
                    ///ACTION phase: moving Mother Nature
                    //calculate the influence in that Land and verify if it joins other lands
                    do {
                        out.sendMoveMN();
                        wait();
                    } while (nack);
                    match.moveMotherNature(motherNatureSteps);
                    try {
                        controller.controlLand();
                    } catch (Exception e) {
                        out.sendGenericError("Desynchronized ("+e.getMessage()+")");
                        out.sendCreation(match);
                    }
                    uniteLands();
                    //System.out.println(match);
                    controller.notifyMovedMN(this, motherNatureSteps);

                    if (match.getMotherNature().getPosition().hasChanged()) {
                        try {
                            controller.notifyChanges();
                        } catch (Exception e) {
                            out.sendGenericError("Desynchronized ("+e.getMessage()+")");
                            out.sendCreation(match);
                        }
                    }

                    if (ongoingMatch) {
                        state = 5;
                    } else {
                        state = 6;
                        break;
                    }
                case 5:
                    System.out.println("Dentro alla fase 5");
                    //ACTION phase: choose a cloud and import students to the entrance
                    ArrayList<Cloud> clouds = new ArrayList<>();

                    do {
                        clouds.addAll(Arrays.asList(match.getCloud()));
                        out.sendChooseCloud(clouds);
                        clouds.clear();
                        System.out.println("Mandato chooseCloud");
                        this.wait();
                    } while (nack);
                    System.out.println("sono qui");
                    do {
                        System.out.println(userName+": altra wait");

                        if (chosenCloud != null) {
                            for (Cloud clou : match.getCloud()) {
                                System.out.println("Tra i due fuochi");
                                if (chosenCloud.equals(clou)) {
                                    System.out.println("sono qua");
                                    avatar.getBoard().importStudents(clou.getStudents());
                                }
                            }
                        }
                    }while (chosenCloud==null);
                    System.out.println(this.avatar.getUserName()+": sono uscito");
                    controller.chooseCloud(chosenCloud, this);
                    System.out.println(controller.getCurrentPlayer());
                    state = 1;
                    break;
                case 6:
                    //END phase: sending winner and GameRecap
                    try {
                        do {
                            out.sendEndGame(controller.getWinner(), controller.getEndExplanation(), controller.getGameRecap());
                            wait();
                        } while (nack);
                    } catch (Exception e) {
                        out.sendGenericError("Desynchronized ("+e.getMessage()+")");
                    }
                    break;
                case 7:
                    //Fase AZIONE: gioca una carta personaggio
            }
        }
    }

    /**
     * If in input is false it sends a nack message to the client, if the counter of NACK is equal to 3 it sends creation.
     * If the input is true is all ok and it wakes up himself to continue the game
     * @param ack
     * @throws Exception
     */
    public synchronized void setAck (boolean ack) throws Exception {
        nack = !ack;
        if (ack) {
            //out.sendACK();
            nackCounter = 0;
            notifyAll();
            System.out.println("Player "+userName+": ack");
        }
        else {
            if (nackCounter < 3) {
                System.out.println("Mando nack");
                out.sendNACK();
                nackCounter++;
            } else if (nackCounter == 3) {
                out.sendCreation(match);
            }
            else {
                connected = false;
                closeConnection();
            }
        }
    }

    public boolean getNack() {
        return nack;
    }

    /**
     * Called from MessageFromClient and sends the creation message after receiving three NACK message or sends the last sent message
     * @throws Exception
     */
    public synchronized void sendMessageAgain () throws Exception {
        nack = true;
        nackCounter++;

        if (nackCounter == 3) {
            out.sendCreation(match);
        } else if (nackCounter > 3) {
            connected = false;
            closeConnection();
        }
        notifyAll();
    }

    public synchronized void setUserName(String userName) {
        this.userName = userName;
        //System.out.println("Username impostato");
        notify();
    }

    /**
     * Sends the server the new player's username and sets it to himself
     * @param userName
     */
    //@SuppressWarnings("unchecked")
    public synchronized void register (String userName) {
        this.serve.addUserName(userName);
        setUserName(userName);
        //System.out.println("Registrato");
    }

    public String getUserName() {
        return userName;
    }

    public synchronized void setColor(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }
    public synchronized void setWizard(Wizards wizard) {
        this.wizard = wizard;
    }

    /**
     * It creates the match
     * @param playersNum
     * @param expert
     */
    public synchronized void createMatch (int playersNum, boolean expert) {
        //System.out.println("Inizio a creare la partita");
        expertMatch = expert;
        controller = serve.createMatch(this, playersNum, expertMatch);
    }

    /**
     * Sends the server the match chose by the palyer
     * @param creator
     */
    public synchronized void joinMatch (String creator) {
        try {
            controller = serve.joinGame(creator, this);
        } catch (Exception e) {
            out.sendGenericError(e.getMessage());
            state = 8;
        }
    }

    public synchronized void setMatch(Match match){
        this.match = match;

        if (controller.getPlayersNum() == 3) {
            movedStudentsNumber = 4;
        }
        else {
            movedStudentsNumber = 3;
        }
        ongoingMatch = true;
        notify();
    }

    public synchronized Message4Client getOutputStream() {
        return out;
    }

    /**
     * Creates an instance of the Class Player in the game model
     * @param color the color of the towers this player controls
     * @param playersNum to decide how many towers instantiate in the board
     * @param expert true if it's an expert match
     */
    private void createAvatar(Colors color, int playersNum, boolean expert){
        int towersNum;
        System.out.println("Creo avatar");

        if (playersNum==2 || playersNum==4){
            towersNum = 8;
        }
        else {
            towersNum = 6;
        }
        avatar = new Player(userName, color, towersNum, wizard, expert);
    }

    /**
     *
     * @return the representation of this player in the model
     */
    public Player getAvatar(){
        return avatar;
    }

    /**
     *
     * @param playedAssistants the AssistantCards already chosen
     * @return the AssistantCards that are possible to choose
     */
    private ArrayList<AssistantCard> playableAssistants (ArrayList<AssistantCard> playedAssistants){
        ArrayList<AssistantCard> playable;
        ArrayList<AssistantCard> deck;
        deck = avatar.getDeck();
        playable = new ArrayList<>(deck.size());
        boolean found;

        for (AssistantCard card : deck) {
            found = false;
            for (AssistantCard assist : playedAssistants)
                if (card.getValue() == assist.getValue()) {
                    found = true;
                    break;
                }
            if (!found)
                playable.add(card);
        }

        if (playable.isEmpty()) {
            return playedAssistants;
        }
        else {
            return playable;
        }
    }

    public synchronized void setPlayedAssistant(AssistantCard playedAssistant) {
        this.playedAssistant = playedAssistant;
    }

    /**
     *
     * @param steps the steps MotherNature has to do
     */
    public synchronized void moveMN (int steps) {
        motherNatureSteps = steps;
    }

    /**
     * For every Type_Student
     */
    private void checkAllProfessors(){
        for (Type_Student e: Type_Student.values()) {
            match.checkProfessor(e);
        }
    }

    private void uniteLands()  {
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=match.getLands().size()-1) {
                if (match.getMotherNature().getPosition().getTowerColor().equals(
                        match.getLands().get((match.getLands().indexOf(match.getMotherNature().getPosition()) + 1) % match.getLands().size()).getTowerColor())) {
                    match.uniteLandAfter(match.getLands().indexOf(match.getMotherNature().getPosition()));
                }
            }else if (match.getLands().get(0).getTowerColor().equals(match.getLands().get(match.getLands().size()-1).getTowerColor())){
                match.uniteLandAfter(match.getLands().indexOf(match.getMotherNature().getPosition()));
            }
        }catch (Exception e){
            System.out.println("isola dopo senza torre");
        }
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=0){
                if(match.getMotherNature().getPosition().getTowerColor().equals(
                        match.getLands().get(match.getLands().indexOf(match.getMotherNature().getPosition())-1).getTowerColor())){
                    match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
                }
            }else if(match.getLands().get(0).getTowerColor().equals(match.getLands().get(match.getLands().size()-1).getTowerColor())){
                match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
            }
        }catch(Exception e){
            System.out.println("isola prima senza torri");
        }
    }

    public synchronized void moveStudent (Student student, Integer position) {
        movedStudent = student;
        movedStudentPosition = position;
    }

    public synchronized void chooseCloud (Cloud cloud) {
        chosenCloud = cloud;
    }

    public synchronized void endMatch() {
        ongoingMatch = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public synchronized void setDisconnected() throws Exception {
        connected = false;
        closeConnection();

        if (controller != null) {
            controller.notifyPlayerDisconnected(this);
        }
    }

    public synchronized void setConnected() {
        connected = true;
        controller.connectPlayer(this);
    }

    public int seeState() {
        return state;
    }

    public synchronized void setState(int state) {
        this.state = state;
    }

    /**
     * Closes inward and outward stream and the socket
     * @throws Exception fails to close the socket
     */
    public synchronized void closeConnection() throws Exception{
        in.halt();
        out.close();
        socket.close();
    }

}