package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Manages all the interactions between Controller (server) and the remote player (client)
 */
public class ClientHandler extends Thread{
    private final Socket socket;
    private final Server server;
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
        this.server = server;
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

    public void run(){
        in.start();
        System.out.println("Stream in ingresso partito");

        do {
            try {
                System.out.println("Si va in scena");
                changeState();

                do {
                    wait();
                } while (!controller.isMyTurn(this));
            } catch (InterruptedException | SocketException e) {
                out.sendGenericError("Internal server error");
                state = 8;
            }
        } while (ongoingMatch);
    }

    private void changeState() throws InterruptedException, SocketException {
        synchronized (this) {
            switch (state) {
                case 0:
                    //LogIn/Registration, match preparation (create/join match, choose the wizard)
                    do {
                        System.out.println("Aspetto il client");
                        wait();
                        System.out.println("Let's-a goooo!");

                        if (server.getUserNames().contains(userName) && server.inactivePlayer(this)) {
                            System.out.println("Okie-dokie!");
                            out.sendLoginSucceeded();
                            System.out.println("Login avvenuto con successo: "+userName);
                            nack = false;
                        } else {
                            out.sendLoginFailed();
                            nack = true;
                        }
                    } while (nack);

                    if (server.canConnectPlayer(userName)) {
                        try {
                            server.joinGame(null, this);
                        } catch (Exception e) {
                            out.sendGenericError("Unable to connect to the match");
                        }
                    }
                    else {
                        out.sendListOfGames(server.getJoinableMatches(), server.getPausedMatches(userName));
                        System.out.println("Dopo listOfGames");

                        do {
                            wait();
                        } while (nack);
                        System.out.println("Mando maghi");

                        do {
                            out.sendWizard(controller.getWizards());
                            wait();
                        } while (nack);
                        out.sendACK();
                        controller.chooseWizard(wizard);
                        createAvatar(color, match.getPlayersNum(), expertMatch);
                        wait();
                    }

                    do {
                        wait();
                    } while (nack);

                    do {
                        out.sendCreation(controller.getMatch());
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
                        wait();
                    } while (nack);
                    state = 2;
                    break;
                case 2:
                    //PLANNING phase: play an assistant card
                    do {
                        out.sendChooseCard(playableAssistants(controller.getPlayedAssistants()));
                        wait();
                    } while (nack);
                    avatar.draw(playedAssistant);
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
                        wait();
                    } while (nack);

                    for (int i=0; i<movedStudentsNumber; i++) {
                        if (movedStudentPosition == 12) {
                            try {
                                avatar.getBoard().placeStudent(avatar.getBoard().removeStudent(movedStudent));
                            }
                            catch (Exception e) {
                                out.sendGenericError("Desynchronized");
                                out.sendCreation(match);
                                i--;
                            }
                        } else {
                            for (Land land : match.getLands()) {
                                if (land.getID() == movedStudentPosition) {
                                    land.addStudent(avatar.getBoard().removeStudent(movedStudent));
                                }
                            }
                        }
                        controller.notifyMovedStudent(this, movedStudent, movedStudentPosition);
                        wait();
                    }
                    checkAllProfessors();
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
                        out.sendGenericError("Desynchronized");
                        out.sendCreation(match);
                    }
                    uniteLands();
                    controller.notifyMovedMN(this, motherNatureSteps);
                    if (match.getMotherNature().getPosition().hasChanged()) {
                        try {
                            controller.notifyChanges();
                        } catch (Exception e) {
                            out.sendGenericError("Desynchronized");
                            out.sendCreation(match);
                        }
                    }
                    controller.notifyProfessors();

                    if (ongoingMatch) {
                        state = 5;
                    } else {
                        state = 6;
                        break;
                    }
                case 5:
                    //ACTION phase: choose a cloud and import students to the entrance
                    do {
                        out.sendChooseCloud(controller.getChosenClouds());
                        wait();
                    } while (nack);

                    for (Cloud cloud : match. getCloud()) {
                        if (chosenCloud.equals(cloud)) {
                            avatar.getBoard().importStudents(cloud.getStudents());
                        }
                    }
                    controller.chooseCloud(chosenCloud, this);
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
                        out.sendGenericError("Desynchronized");
                    }
                    break;
                case 7:
                    //Fase AZIONE: gioca una carta personaggio
            }
        }
    }

    public synchronized void setAck (boolean ack) {
        nack = !ack;
        if (ack) {
            //out.sendACK();
            nackCounter = 0;
            notifyAll();
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
                //chiude la connessione
            }
        }
    }

    public boolean getNack() {
        return nack;
    }

    public synchronized void sendMessageAgain () {
        nack = true;
        nackCounter++;

        if (nackCounter == 3) {
            out.sendCreation(match);
        } else if (nackCounter > 3) {
            //chiude la connessione
        }
        notifyAll();
    }

    public synchronized void setUserName(String userName) {
        this.userName = userName;
        System.out.println("Username impostato");
        notify();
    }

    public synchronized void register (String userName) {
        server.addUserName(userName);
        setUserName(userName);
        System.out.println("Registrato");
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

    public synchronized void createMatch (int playersNum, boolean expert) {
        System.out.println("Inizio a creare la partita");
        expertMatch = expert;
        controller = server.createMatch(this, playersNum, expertMatch);
    }

    public synchronized void joinMatch (String creator) {
        try {
            controller = server.joinGame(creator, this);
        } catch (Exception e) {
            out.sendGenericError("Couldn't join match");
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

    public Message4Client getOutputStream() {
        return out;
    }

    /**
     * Creates an instance of the Class Player in the game model
     * @param color the color of the towers this player controls
     * @param playersNum to decide how many towers instantiate in the board
     * @param expert true if it's an expert match
     */
    private synchronized void createAvatar(Colors color, int playersNum, boolean expert){
        int towersNum;

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

    private ArrayList<AssistantCard> playableAssistants (ArrayList<AssistantCard> playedAssistants){
        ArrayList<AssistantCard> playable;
        ArrayList<AssistantCard> deck;
        deck = avatar.getDeck();
        playable = new ArrayList<>(deck.size());

        for (AssistantCard card : deck) {
            if (!playedAssistants.contains(card)) {
                playable.add(card);
            }
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

    public synchronized void moveMN (int steps) {
        motherNatureSteps = steps;
    }

    private void checkAllProfessors(){
        for (Type_Student e: Type_Student.values()) {
            match.checkProfessor(e);//togliere return
        }
    }

    private void uniteLands()  {
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=match.getLands().size()-1) {
                if (match.getMotherNature().getPosition().getTowerColor() ==
                        match.getLands().get((match.getLands().indexOf(match.getMotherNature().getPosition()) + 1) % match.getLands().size()).getTowerColor()) {
                    match.uniteLandAfter(match.getLands().indexOf(match.getMotherNature().getPosition()));
                }
            }else if (match.getLands().get(0).getTowerColor()==match.getLands().get(match.getLands().size()-1).getTowerColor()){
                match.uniteLandAfter(match.getLands().indexOf(match.getMotherNature().getPosition()));
            }
        }catch (Exception e){
            System.out.println("isola dopo senza torre");
        }
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=0){
                if(match.getMotherNature().getPosition().getTowerColor()==
                        match.getLands().get(match.getLands().indexOf(match.getMotherNature().getPosition())-1).getTowerColor()){
                    match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
                }
            }else if(match.getLands().get(0).getTowerColor()==match.getLands().get(match.getLands().size()-1).getTowerColor()){
                match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
            }
        }catch(Exception e){
            //System.out.println("isola prima senza torri");
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

    public synchronized void setDisconnected() throws InterruptedException {
        connected = false;
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