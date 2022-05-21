package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.io.*;
import java.net.Socket;
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

        while (ongoingMatch) {
            try {
                changeState();
                wait();
            } catch (InterruptedException e) {
                out.sendGenericError("Internal server error");
                state = 8;
            }
        }
    }

    private void changeState() throws InterruptedException {
        synchronized (this) {
            switch (state) {
                case 0:
                    //LogIn/Registration, match preparation (create/join match, choose the wizard)
                    do {
                        wait();

                        if (server.getUserNames().contains(userName)) {
                            out.sendLoginSucceeded();
                            nack = false;
                        } else {
                            out.sendLoginFailed();
                            nack = true;
                        }
                    } while (nack);

                    if (server.areThereJoinableMatches(userName)) {
                        out.sendListOfGames(server.getJoinableMatches(), server.getJoinableMatches(userName));
                    }
                    else {
                        out.sendNoGames();
                    }

                    do {
                        wait();
                    } while (nack);

                    out.sendWizard(controller.getWizards());
                    wait();
                    controller.chooseWizard (wizard);
                    createAvatar(color, match.getPlayersNum(), expertMatch);
                    wait();
                    out.sendCreation(controller.getMatch());
                    state = 1;
                    break;
                case 1:
                    //PLANNING phase: notify refilled clouds
                    out.sendRefillClouds(match.getCloud());
                    state = 2;
                    break;
                case 2:
                    //PLANNING phase: play an assistant card
                    out.sendChooseCard(playableAssistants(controller.getPlayedAssistants()));
                    wait();
                    avatar.draw(playedAssistant);
                    controller.playAssistantCard(playedAssistant, this);
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
                        wait();

                        synchronized (movedStudent) {
                            if (movedStudentPosition == 12) {
                                try {
                                    avatar.getBoard().placeStudent(avatar.getBoard().removeStudent(movedStudent));
                                }
                                catch (Exception e) {
                                    throw new RuntimeException();
                                }
                            } else {
                                for (Land land : match.getLands()) {
                                    if (land.getID() == movedStudentPosition) {
                                        land.addStudent(avatar.getBoard().removeStudent(movedStudent));
                                    }
                                }
                            }
                        }
                    }
                    checkAllProfessors();
                    controller.notifyMovedStudent(this, movedStudent, movedStudentPosition);
                    state = 4;
                case 4:
                    ///ACTION phase: moving Mother Nature
                    //calculate the influence in that Land and verify if it joins other lands
                    out.sendMoveMN();
                    wait();
                    match.moveMotherNature(motherNatureSteps);
                    try {
                        controller.controlLand();
                    } catch (Exception e) {
                        out.sendGenericError(e.getMessage());
                    }
                    uniteLands();
                    controller.notifyMovedMN(this, motherNatureSteps);

                    if (ongoingMatch) {
                        state = 5;
                    } else {
                        state = 6;
                        break;
                    }
                case 5:
                    //ACTION phase: choose a cloud and import students to the entrance
                    out.sendChooseCloud(controller.getChosenClouds());
                    wait();
                    avatar.getBoard().importStudents(chosenCloud.getStudents());
                    controller.chooseCloud(chosenCloud, this);
                    state = 1;
                    break;
                case 6:
                    //Fine partita: si invia il vincitore
                    break;
                case 7:
                    //Fase AZIONE: gioca una carta personaggio
            }
        }
    }

    public synchronized void setAck (boolean ack) {
        nack = !ack;
        if (ack) {
            out.sendACK();
            nackCounter = 0;
            notify();
        }
        else {
            if (nackCounter < 3) {
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

    public synchronized void sendMessageAgain () {
        nack = true;
        nackCounter++;

        if (nackCounter == 3) {
            out.sendCreation(match);
        } else if (nackCounter > 3) {
            //chiude la connessione
        }
        notify();
    }

    public synchronized void setUserName(String userName) {
        this.userName = userName;
    }

    public synchronized void register (String userName) {
        server.addUserName(userName);
        setUserName(userName);
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

    public synchronized void setMatch(Match match) {
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

    public synchronized void setDisconnected() throws InterruptedException {
        connected = false;
        controller.notifyPlayerDisconnected(this);
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