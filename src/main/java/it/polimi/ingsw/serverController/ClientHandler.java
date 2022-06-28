package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

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
    private boolean useCh;
    private String chosenCh;
    private Student ch_1_Student;
    private Land ch_1_land;
    private Land ch_5_land;
    private ArrayList<Student> ch_10_students;
    private ArrayList<Type_Student> ch_10_types;
    private Student ch_11_student;
    private Type_Student ch_12_type;

    /**
     *
     * @param s the socket associated with this player
     */
    public ClientHandler (Socket s, Server server){
        socket = s;
        this.server = server;
        connected = true;
        ongoingMatch = false;
        expertMatch=true;
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

        do {
            try {
                changeState();

                synchronized (this) {
                    do {
                        this.wait();
                    } while (!controller.isMyTurn(this));
                }
            } catch (Exception e) {
                out.sendGenericError("Internal server error ("+e.getMessage()+")");
                ongoingMatch = false;
            }
        } while (ongoingMatch);

        try {
            closeConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Is the finite state machine that controls the single client (therefore the single player).
     * It is made of cases that simulate the states of the ideal machine
     * @throws InterruptedException if a wait is interrupted
     * @throws SocketException if the socket goes down unexpectedly
     */
    private void changeState() throws Exception {
        synchronized (this) {
            switch (state) {
                case 0:
                    //LogIn/Registration, match preparation (create/join match, choose the wizard)
                    do {
                        wait();

                        if (server.getUserNames().contains(userName) && server.inactivePlayer(this)) {
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
                            out.sendGenericError("Unable to connect to the match ("+e.getMessage()+")");
                        }
                    }
                    else {
                        out.sendListOfGames(server.getJoinableMatches(), server.getPausedMatches(userName));
                        do {
                            wait();
                        } while (nack);
                        if(!controller.isGame_from_memory()) {
                            do {
                                out.sendWizard(controller.getWizards());
                                wait();
                            } while (nack);
                            out.sendACK();
                            controller.chooseWizard(wizard);
                            createAvatar(color, controller.getPlayersNum(), expertMatch);

                            controller.createMatch();
                        }
                    }
                    if(!controller.isGame_from_memory()) {
                        do {
                            System.out.println("Player " + userName + ": aspetto la creazione del match");
                            this.wait();
                        } while (!controller.readyToStart());
                    }
                    do {
                        out.sendCreation(controller.getMatch());
                        wait();
                    } while (nack);
                    out.start();
                    socket.setSoTimeout(5000);
                    if(!controller.isGame_from_memory() && state!=6) {
                        state = 1;
                    }
                    break;
                case 1:
                    //PLANNING phase: notify refilled clouds
                    do {
                        out.sendRefillClouds(match.getCloud());
                        wait();
                    } while (nack);

                    synchronized (controller) {
                        controller.notify();
                    }

                    if (state != 6)
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

                    if (state != 6)
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
                                avatar.getBoard().placeStudent(movedStudent);
                            }
                            catch (Exception e) {
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

                        if (i < movedStudentsNumber-1) {
                            wait();
                        }
                    }
                    try {
                        checkAllProfessors();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    controller.notifyProfessors();

                    if(expertMatch){
                        System.out.println("Entro nella fase 7");
                        state = 7;
                        changeState();
                    }
                    else {
                        if (state != 6)
                            state = 4;
                    }
                case 4:
                    ///ACTION phase: moving Mother Nature
                    //calculate the influence in that Land and verify if it joins other lands
                    do {
                        out.sendMoveMN();
                        wait();
                    } while (nack);
                    match.moveMotherNature(motherNatureSteps);
                    if(!match.getMotherNature().getPosition().isThereNoEntry()) {
                        try {
                            controller.controlLand();
                        } catch (Exception e) {
                            out.sendGenericError("Desynchronized (" + e.getMessage() + ")");
                            out.sendCreation(match);
                        }
                        uniteLands();
                        controller.notifyMovedMN(motherNatureSteps);

                        if (match.getMotherNature().getPosition().hasChanged()) {
                            try {
                                controller.notifyChanges();
                            } catch (Exception e) {
                                out.sendGenericError("Desynchronized (" + e.getMessage() + ")");
                                out.sendCreation(match);
                            }
                        }

                        if (match.getLands().size() <= 3)
                            controller.notifyThreeArchipelagos();
                    }else{
                        try {
                            match.getMotherNature().getPosition().setNoEntry(false);

                            for (CharacterCard ch : ((Expert_Match)match).getCard())
                                if (ch instanceof Ch_5)
                                    ((Ch_5) ch).returnNoEntryTile();
                            controller.notifyMovedMN(motherNatureSteps);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (ongoingMatch) {
                        state = 5;
                    } else {
                        state = 6;
                        break;
                    }
                case 5:
                    //ACTION phase: choose a cloud and import students to the entrance
                    ArrayList<Cloud> clouds = new ArrayList<>();

                    do {
                        clouds.addAll(Arrays.asList(match.getCloud()));
                        out.sendChooseCloud();
                        clouds.clear();
                        this.wait();
                    } while (nack);
                    do {
                        if (chosenCloud != null) {
                            for (Cloud clou : match.getCloud()) {
                                if (chosenCloud.equals(clou)) {
                                    avatar.getBoard().importStudents(clou.getStudents());
                                }
                            }
                        }
                    }while (chosenCloud==null);
                    controller.chooseCloud(chosenCloud, this);
                    if (state != 6)
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
                    //ACTION phase: playing a character card
                    do{
                        out.sendCh(((Expert_Match)match).getCard());
                        wait();
                    }while(nack);

                    if (useCh) {
                        try {
                            effectCh();
                        } catch (Exception e) {
                            controller.notifyFinishedStudents();
                        }
                        controller.notifyCh();
                    }

                    if (state != 6)
                        state = 4;
                    break;
            }
        }
    }

    /**
     * If in input is false it sends a nack message to the client, if the counter of NACK is equal to 3 it sends creation.
     * If the input is true is all ok, and it wakes up himself to continue the game
     * @param ack
     * @throws Exception if closeConnection throws Exception
     */
    public synchronized void setAck (boolean ack) throws Exception {
        nack = !ack;
        if (ack) {
            //out.sendACK();
            nackCounter = 0;
            notifyAll();
        }
        else {
            if (nackCounter < 3) {
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

    /**
     *
     * @return true if the server received a nack without being followed by any ack
     */
    public boolean getNack() {
        return nack;
    }

    /**
     * Called from MessageFromClient and sends the creation message after receiving three NACK message or sends the last sent message
     * @throws Exception if closeConnection goes wrong
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

    /**
     * Sets the player's username
     * @param userName
     */
    public synchronized void setUserName(String userName) {
        this.userName = userName;
        notify();
    }

    /**
     * Sends the server the new player's username and sets it to himself
     * @param userName
     */
    public synchronized void register (String userName) {
        this.server.addUserName(userName);
        setUserName(userName);
    }

    /**
     *
     * @return the player's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the player's towers' color
     * @param color
     */
    public synchronized void setColor(Colors color) {
        this.color = color;
    }

    /**
     *
     * @return the player's towers' color
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Sets the player's wizard
     * @param wizard
     */
    public synchronized void setWizard(Wizards wizard) {
        this.wizard = wizard;
    }

    /**
     * It creates the match
     * @param playersNum
     * @param expert
     */
    public synchronized void createMatch (int playersNum, boolean expert) {
        expertMatch = expert;
        controller = server.createMatch(this, playersNum, expertMatch);
    }

    /**
     * Sends the server the match chose by the player
     * @param creator
     */
    public synchronized void joinMatch (String creator) {
        try {
            controller = server.joinGame(creator, this);
        } catch (Exception e) {
            out.sendGenericError(e.getMessage());
            state = 8;
        }
    }

    /**
     * Sets the match the player joined
     * @param match
     */
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

    /**
     *
     * @return the stream towards the client
     */
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

    /**
     * Sets the assistant card played by the remote user
     * @param playedAssistant
     */
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
     * For every Type_Student instance checks what player controls it
     */
    private void checkAllProfessors(){
        for (Type_Student e: Type_Student.values()) {
            match.checkProfessor(e);
        }
    }

    /**
     * Unites the land on which there is nm with the previous or following one if the towers have the same color
     */
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
        }catch (Exception e){}
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=0){
                if(match.getMotherNature().getPosition().getTowerColor().equals(
                        match.getLands().get(match.getLands().indexOf(match.getMotherNature().getPosition())-1).getTowerColor())){
                    match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
                }
            }else if(match.getLands().get(0).getTowerColor().equals(match.getLands().get(match.getLands().size()-1).getTowerColor())){
                match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
            }
        }catch(Exception e){}
    }

    /**
     * Sets the student moved by the remote player and its position (board/an island)
     * @param student the moved student
     * @param position an island's id or 12 if it is the player's board
     */
    public synchronized void moveStudent (Student student, Integer position) {
        movedStudent = student;
        movedStudentPosition = position;
    }

    /**
     * Sets the cloud chosen by the remote player
     * @param cloud
     */
    public synchronized void chooseCloud (Cloud cloud) {
        chosenCloud = cloud;
    }

    /**
     * Sets ongoingMatch attribute on false
     */
    public synchronized void endMatch() {
        ongoingMatch = false;
    }

    /**
     *
     * @return true if the remote player is still connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sets the status to disconnected and closes the socket
     * @throws Exception if it cannot close the connection
     */
    public synchronized void setDisconnected() throws Exception {
        connected = false;
        closeConnection();

        if (controller != null) {
            controller.notifyPlayerDisconnected(this);
        }
    }

    /**
     * Sets the status to connected
     */
    public synchronized void setConnected() {
        connected = true;
        controller.connectPlayer(this);
    }

    /**
     *
     * @return the current state of the player (a number from 0 to 7 depending on what is the ongoing phase)
     */
    public int seeState() {
        return state;
    }

    /**
     * Sets the state of the player
     * @param state
     */
    public synchronized void setState(int state) {
        this.state = state;
    }

    /**
     * Closes inward and outward stream and the socket
     * @throws Exception fails to close the socket
     */
    public synchronized void closeConnection() throws Exception{
        in.halt();
        out.halt();
        sleep (500);
        socket.close();
    }

    /**
     * set noCh when a player doesn't use a character card
     * @param useCh
     */
    public void setUseCh(boolean useCh) {
        this.useCh = useCh;
    }

    /**
     * set the name of the character that it is used by the player
     * @param chosenCh name of the character
     */
    public void setChosenCh(String chosenCh) {
        this.chosenCh = chosenCh;
    }

    /**
     * Sets the student moved from ch card 1
     * @param ch_1_Student
     */
    public void setCh_1_Student(Student ch_1_Student) {
        this.ch_1_Student = ch_1_Student;
    }

    /**
     * Sets the land on which the student moved from ch card 1 has been placed on
     * @param ch_1_land
     */
    public void setCh_1_land(Land ch_1_land) {
        this.ch_1_land = ch_1_land;
    }

    /**
     * Sets the land on which has been placed a no-entry tile
     * @param ch_5_land
     */
    public void setCh_5_land(Land ch_5_land) {
        this.ch_5_land = ch_5_land;
    }

    /**
     * does the effect of the character card if the player has played one
     * @throws Exception if the bag is empty
     */
    public void effectCh() throws Exception{
        switch (chosenCh) {
            case "Ch_1" -> {
                for (int i=0;i<3;i++) {
                    if (((Expert_Match) match).getCard()[i] instanceof Ch_1 c) {
                        c.setStudent(ch_1_Student);
                        for (Land land : match.getLands()) {
                            if (ch_1_land.getID() == land.getID()) {
                                c.setLand(land);
                            }
                        }
                        c.setPlayer(avatar);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
            case "Ch_4" -> {
                for (CharacterCard c : ((Expert_Match) match).getCard()) {
                    if (c instanceof Ch_4) {
                        c.setPlayer(avatar);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
            case "Ch_5" -> {
                for (CharacterCard c : ((Expert_Match) match).getCard()) {
                    if (c instanceof Ch_5) {
                        c.setPlayer(avatar);
                        for (Land land : match.getLands()) {
                            if (land.getID() == ch_5_land.getID())
                                ((Ch_5) c).setLand(land);
                        }
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
            case "Ch_8" -> {
                for (CharacterCard c : ((Expert_Match) match).getCard()) {
                    if (c instanceof Ch_8) {
                        c.setPlayer(avatar);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
            case "Ch_10" -> {
                for (CharacterCard c : ((Expert_Match) match).getCard()) {
                    if (c instanceof Ch_10) {
                        c.setPlayer(avatar);
                        ((Ch_10) c).setEntrance_student(ch_10_students);
                        ((Ch_10) c).setRoom_student(ch_10_types);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
            case "Ch_11" -> {
                for (CharacterCard c : ((Expert_Match) match).getCard()) {
                    if (c instanceof Ch_11) {
                        c.setPlayer(avatar);
                        ((Ch_11) c).setStudent(ch_11_student);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
            case "Ch_12" -> {
                for (CharacterCard c : ((Expert_Match) match).getCard()) {
                    if (c instanceof Ch_12) {
                        c.setPlayer(avatar);
                        ((Ch_12) c).setType(ch_12_type);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
            case "Ch_2" -> {
                for (CharacterCard c : ((Expert_Match) match).getCard()) {
                    if (c instanceof Ch_2) {
                        c.setPlayer(avatar);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                    }
                }
            }
        }
    }

    /**
     * Sets the students removed from this players board
     * @param ch_10_students
     */
    public void setCh_10_students(ArrayList<Student> ch_10_students) {
        this.ch_10_students = ch_10_students;
    }

    /**
     * Sets the tables of which the player decided to switch students with the entrance
     * @param ch_10_types
     */
    public void setCh_10_types(ArrayList<Type_Student> ch_10_types) {
        this.ch_10_types = ch_10_types;
    }

    /**
     * Sets the student the player decided to place on their board
     * @param ch_11_student
     */
    public void setCh_11_student(Student ch_11_student) {
        this.ch_11_student = ch_11_student;
    }

    /**
     * Sets the type the player decided every player must return three students to the bag
     * @param ch_12_type
     */
    public void setCh_12_type(Type_Student ch_12_type) {
        this.ch_12_type = ch_12_type;
    }

    /**
     *
     * @return the ch card the player chose last time
     */
    public String getChosenCh() {
        return chosenCh;
    }

    /**
     *
     * @return the chosen land when activating 5th ch card's power
     */
    public Land getCh_5_land() {
        return ch_5_land;
    }

    /**
     * Sets the match status to expert
     * @param expertMatch
     */
    public void setExpertMatch(boolean expertMatch) {
        this.expertMatch = expertMatch;
    }

    /**
     *
     * @return the chosen student when activating 12th ch card's power
     */
    public Type_Student getCh_12_type() {
        return ch_12_type;
    }

    /**
     *
     * @return the chosen students when activating 10th ch card's power
     */
    public ArrayList<Student> getCh_10_students() {
        return ch_10_students;
    }

    /**
     *
     * @return the chosen tables when activating 10th ch card's power
     */
    public ArrayList<Type_Student> getCh_10_types() {
        return ch_10_types;
    }

    /**
     *
     * @return the chosen land when activating 1st ch card's power
     */
    public Land getCh_1_land() {
        return ch_1_land;
    }

    /**
     *
     * @return the chosen students when activating 1st ch card's power
     */
    public Student getCh_1_Student() {
        return ch_1_Student;
    }

    /**
     *
     * @return the chosen student when activating 11th ch card's power
     */
    public Student getCh_11_student() {return ch_11_student;}

    /**
     * set the controller,it is used for the match from the memory
     * @param controller to be set
     */
    public void setController(Controller controller) {this.controller = controller;}

    /**
     * set the player into the clienthandler, this method is used for the match from memory
     * @param avatar avatar to set into the clienthandler
     */
    public void setAvatar(Player avatar){this.avatar=avatar;}

}