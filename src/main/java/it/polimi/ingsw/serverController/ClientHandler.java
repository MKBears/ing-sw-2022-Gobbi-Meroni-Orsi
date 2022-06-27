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
        this.serve = server;
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
            } catch (Exception e) {
                out.sendGenericError("Internal server error ("+e.getMessage()+")");
                state = 8;
            }
        } while (ongoingMatch);
    }

    /**
     * Is the finite state machine thart controls the single client (therefore the single player). It is made of cases that simulate the states of the ideal machine
     * @throws InterruptedException if a wait is interrupted
     * @throws SocketException if the socket goes down unexpectedly
     */
    private void changeState() throws Exception {
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
                        if(!controller.isGame_from_memory()) {
                            do {
                                out.sendWizard(controller.getWizards());
                                wait();
                            } while (nack);
                            out.sendACK();
                            controller.chooseWizard(wizard);
                            //System.out.println("helooo");
                            createAvatar(color, controller.getPlayersNum(), expertMatch);

                            controller.createMatch();
                        }
                        //wait();
                    }
                    if(!controller.isGame_from_memory()) {
                        do {
                            System.out.println("Player " + userName + ": aspetto la creazione del match");
                            this.wait();
                        } while (!controller.readyToStart());
                    }
                    do {
                        out.sendCreation(controller.getMatch());
                        System.out.println("Mandata creation");
                        wait();
                    } while (nack);
                    out.start();
                    socket.setSoTimeout(5000);
                    if(!controller.isGame_from_memory()) {
                        state = 1;
                    }
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
                    System.out.println("carte personaggio");
                    if(expertMatch){
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
                    }
                    state=4;
                case 4:
                    ///ACTION phase: moving Mother Nature
                    //calculate the influence in that Land and verify if it joins other lands
                    do {
                        out.sendMoveMN();
                        System.out.println("Mandato muovi mn");
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
                        //System.out.println(match);
                        controller.notifyMovedMN(this, motherNatureSteps);

                        if (match.getMotherNature().getPosition().hasChanged()) {
                            try {
                                controller.notifyChanges();
                            } catch (Exception e) {
                                out.sendGenericError("Desynchronized (" + e.getMessage() + ")");
                                out.sendCreation(match);
                            }
                        }
                    }else{
                        try {
                            match.getMotherNature().getPosition().setNoEntry(false);
                            controller.notifyMovedMN(this, motherNatureSteps);
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
                    System.out.println(chosenCloud);
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
            }
        }
    }

    /**
     * If in input is false it sends a nack message to the client, if the counter of NACK is equal to 3 it sends creation.
     * If the input is true is all ok and it wakes up himself to continue the game
     * @param ack
     * @throws Exception if closeConnection throws Exception
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
        //System.out.println("Username impostato");
        notify();
    }

    /**
     * Sends the server the new player's username and sets it to himself
     * @param userName
     */
    public synchronized void register (String userName) {
        this.serve.addUserName(userName);
        setUserName(userName);
        //System.out.println("Registrato");
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
            e.printStackTrace();
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
                    System.out.println(match.getLands());
                }
            }else if (match.getLands().get(0).getTowerColor().equals(match.getLands().get(match.getLands().size()-1).getTowerColor())){
                match.uniteLandAfter(match.getLands().indexOf(match.getMotherNature().getPosition()));
                System.out.println(match.getLands());
            }
        }catch (Exception e){
            System.out.println("isola dopo senza torre");
        }
        try{
            if(match.getLands().indexOf(match.getMotherNature().getPosition())!=0){
                if(match.getMotherNature().getPosition().getTowerColor().equals(
                        match.getLands().get(match.getLands().indexOf(match.getMotherNature().getPosition())-1).getTowerColor())){
                    match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
                    System.out.println(match.getLands());
                }
            }else if(match.getLands().get(0).getTowerColor().equals(match.getLands().get(match.getLands().size()-1).getTowerColor())){
                match.uniteLandBefore(match.getLands().indexOf(match.getMotherNature().getPosition()));
                System.out.println(match.getLands());
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
        System.out.println("Ho ricevuto la nuvola scelta, che è la seguente: "+ cloud.toString());
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

    public void setCh_1_Student(Student ch_1_Student) {
        this.ch_1_Student = ch_1_Student;
    }

    public void setCh_1_land(Land ch_1_land) {
        this.ch_1_land = ch_1_land;
    }

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
                    if (((Expert_Match)match).getCard()[i] instanceof Ch_1) {
                        Ch_1 c=(Ch_1) ((Expert_Match)match).getCard()[i];
                        c.setStudent(ch_1_Student);
                        for (Land land : match.getLands()) {
                            if (ch_1_land.getID() == land.getID()) {
                                c.setLand(land);
                            }
                        }
                        c.setPlayer(avatar);
                        ((Board_Experts) avatar.getBoard()).playCharacter(c);
                        System.out.println(c.getStudents());
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
    public void setCh_10_students(ArrayList<Student> ch_10_students) {
        this.ch_10_students = ch_10_students;
    }

    public void setCh_10_types(ArrayList<Type_Student> ch_10_types) {
        this.ch_10_types = ch_10_types;
    }

    public void setCh_11_student(Student ch_11_student) {
        this.ch_11_student = ch_11_student;
    }

    public void setCh_12_type(Type_Student ch_12_type) {
        this.ch_12_type = ch_12_type;
    }

    public String getChosenCh() {
        return chosenCh;
    }

    public Land getCh_5_land() {
        return ch_5_land;
    }

    public void setExpertMatch(boolean expertMatch) {
        this.expertMatch = expertMatch;
    }

    public Type_Student getCh_12_type() {
        return ch_12_type;
    }

    public ArrayList<Student> getCh_10_students() {
        return ch_10_students;
    }

    public ArrayList<Type_Student> getCh_10_types() {
        return ch_10_types;
    }

    public Land getCh_1_land() {
        return ch_1_land;
    }

    public Student getCh_1_Student() {
        return ch_1_Student;
    }

    public Student getCh_11_student() {return ch_11_student;}

    public void setController(Controller controller) {this.controller = controller;}

    public void setAvatar(Player avatar){this.avatar=avatar;}

    public void setmatch(Match match){this.match=match;}


}