package it.polimi.ingsw.serverController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.model.*;

/**
 * This class contains all the possible message to send to the client
 */
public class Message4Client {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String name;
    private String message;
    private Message4Client toclient;

    /**
     *
     * @param out the out parameter for TCP connection
     * @param in the in parameter for TCP connection
     */
    public Message4Client(ObjectOutputStream out, ObjectInputStream in){
        this.out=out;
        this.in=in;
    }

    /**
     * Base positive response for a client request (the message received was correct)
     */
    public void sendACK(){
        name="ACK";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Base negative response for a client request (the message received was not correct)
     */
    public void sendNACK(){
        name="NACK";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When the username is correct
     */
    public void sendLoginSucceeded(){
        name="LoginSucceeded";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When the username is incorrect
     * @return a new username
     */
    public String sendLoginFailed(){
        name="LoginFailed";
        try {
            out.writeObject(name);
            message=(String)in.readObject();
            if(message=="Login"){
                return (String)in.readObject();
            }
            else return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When there is no game started with the selection of the player
     */
    public void sendNoGames(){
        name="NoGames";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When selected "join game"
     * @param games a list of games that is possible to join
     * @return the name of the chosen game or "ERROR"
     */
    public String sendListOfGames(ArrayList<String> games){
        name="ListOfGames";
        try {
            out.writeObject(name);
            out.writeObject(games);
            message= (String)in.readObject();
            if(message=="GameSelected"){
                return (String)in.readObject();
            }
            else return "ERROR";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When selected "resume game"
     * @param games a list of games that is possible to resume (associated with the username)
     * @return the chosen game or "ERROR"
     */
    public String sendListOfStartedGames(ArrayList<String> games){
        name="ListOfStartedGames";
        try {
            out.writeObject(name);
            out.writeObject(games);
            message= (String)in.readObject();
            if(message=="GameSelected"){
                return (String)in.readObject();
            }
            else return "ERROR";
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * It sends the client all the actual match (initialized)
     * @param match the actual match
     * @return ACK or NACK
     */
    public String sendCreation(Match match){
        name="Creation";
        try {
            out.writeObject(name);
            out.writeObject(match);
            return (String) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Asks the clinent which wizard whants to choose
     * @param wizards a list of the possible wizard to choose
     * @return the chosen wizard or null
     */
    public Wizards sendWizard(ArrayList<Wizards> wizards){ //ritorna il wizard scelto
        name="Wizard";
        try {
            out.writeObject(name);
            out.writeObject(wizards);
            message= (String)in.readObject();
            if(message=="Choice"){
                return (Wizards) in.readObject();
            }else return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server sends the students to refill the clouds
     * @param students1 students for the first cloud
     * @param students2 students for the second cloud
     * @return ACK or NACK
     */
    public String sendRefillClouds(ArrayList<Student> students1, ArrayList<Student> students2){
        name="RefillClouds";
        try {
            out.writeObject(name);
            out.writeObject(students1);
            out.writeObject(students2);
            return (String) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server sends the students to refill the clouds
     * @param students1 students for the first cloud
     * @param students2 students for the second cloud
     * @param students3 students for the third cloud
     * @return ACK or NACK
     */
    public String sendRefillClouds(ArrayList<Student> students1, ArrayList<Student> students2, ArrayList<Student> students3){
        name="RefillClouds";
        try {
            out.writeObject(name);
            out.writeObject(students1);
            out.writeObject(students2);
            out.writeObject(students3);
            return (String) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server sends the students to refill the clouds
     * @param students1 students for the first cloud
     * @param students2 students for the second cloud
     * @param students3 students for the third cloud
     * @param students4 students fot the fourth cloud
     * @return ACK or NACK
     */
    public String sendRefillClouds(ArrayList<Student> students1, ArrayList<Student> students2, ArrayList<Student> students3, ArrayList<Student> students4){
        name="RefillClouds";
        try {
            out.writeObject(name);
            out.writeObject(students1);
            out.writeObject(students2);
            out.writeObject(students3);
            out.writeObject(students4);
            return (String) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server asks the client to choose the assistant card
     * @param cards list of the possible card to choose
     * @return the chosen card or null
     */
    public AssistantCard sendChooseCard(ArrayList<AssistantCard> cards){
        name="ChooseCard";
        try {
            out.writeObject(name);
            out.writeObject(cards);
            message= (String) in.readObject();
            if(message=="ChosenCard"){
                return (AssistantCard) in.readObject();
            } else return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server asks the client to tell him where the player decides to move the students
     * @return a map with <Student, Sting> (the string contains where the student is OR null
     */
    public Map sendMoveStudents(){
        name="MoveStudents";
        try {
            out.writeObject(name);
            Student stu;
            String m;
            Map<Student, String> map= new HashMap<>();
            for(int i=0; i<3; i++){
                message=(String) in.readObject();
                if(message=="Student1" || message=="Student2" || message=="Student3"){
                    stu=(Student)in.readObject();
                    m=(String)in.readObject();
                    if(stu!=null){
                        map.put(stu, m);
                    }
                    else map.put(null, null);
                }else return null;
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server asks the client the cloud that choses the player
     * @param clouds the possible clouds to chose
     * @return the chosen cloud or null
     */
    public Cloud sendChooseCloud(ArrayList<Cloud> clouds){
        name="ChooseCloud";
        try {
            out.writeObject(name);
            out.writeObject(clouds);
            message=(String) in.readObject();
            if(message=="ChoiceCloud"){
                return (Cloud) in.readObject();
            }else return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to all the clients the chosen card
     * @param card the chosen card
     * @param player the player that chose the card
     * @return ACK or NACK
     */
    public String sendNotifyChosenCard(AssistantCard card, Player player){
        name="NotifyChosenCard";
        try {
            out.writeObject(name);
            out.writeObject(card);
            out.writeObject(player);
            return (String) in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server asks the client the steps the player decides for Mother Nature
     * @return the spetps or -1
     */
    public int sendMoveMN(){
        name="MoveMN";
        try {
            out.writeObject(name);
            message= (String) in.readObject();
            if(message=="StepsMN"){
                return(int)in.readObject();
            }else return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to all the clients the movement of someone's students, we have to call it for every student moved
     * @param student the signle student
     * @param player the students's player
     * @param id the id of the island or archipelago
     * @return ACK or NACK
     */
    public String sendNotifyMoveStudents(Student student, Player player, int id){
        name="NotifyMoveStudents (id)";
        try {
            out.writeObject(name);
            out.writeObject(student);
            out.writeObject(player);
            out.writeObject(id);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to all the clients the movement of someone's students, we have to call it for every student moved
     * @param student the signle student
     * @param player the student's player
     * @param board the board of the player
     * @return ACk or NACK
     */
    public String sendNotifyMoveStudents(Student student, Player player, Board board){
        name="NotifyMoveStudents (board)";
        try {
            out.writeObject(name);
            out.writeObject(student);
            out.writeObject(player);
            out.writeObject(board);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to all the clients the new position of Mother Nature
     * @param id the id of the island
     * @param lands an update of the situation of the lands
     * @return ACK or NACK
     */
    public String sendNotifyMovementMN(int id, ArrayList<Land> lands){
        name="NotifyMovementMN";
        try {
            out.writeObject(name);
            out.writeObject(id);
            out.writeObject(lands);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to all the clients the changes of the professors situation
     * @param professors the professors of the board
     * @return ACK or NACK
     */
    public String sendNotifyProfessors(Map<Type_Student, Player> professors){
        name="NotifyProfessors";
        try {
            out.writeObject(name);
            out.writeObject(professors);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to all the slients the cloud chosen by a player
     * @param player the player that chose
     * @param cloud the chosen cloud
     * @return ACK or NACK
     */
    public String sendNotifyChosenCloud(Player player, Cloud cloud){
        name="NotifyChosenCloud";
        try {
            out.writeObject(name);
            out.writeObject(player);
            out.writeObject(cloud);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies the clients the situation of the towers in the game
     * @param towers
     * @param land the land involved
     * @return ACK or NACK
     */
    public String sendNotifyTowers(ArrayList<Tower> towers, Land land){
        name="NotifyTowers (land)";
        try {
            out.writeObject(name);
            out.writeObject(towers);
            out.writeObject(land);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies the clients the situation of the towers in the game
     * @param towers
     * @param board the board involved
     * @return ACK or NACK
     */
    public String sendNotifyTowers(ArrayList<Tower> towers, Board board){
        name="NotifyTowers (board)";
        try {
            out.writeObject(name);
            out.writeObject(towers);
            out.writeObject(board);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to all the clients the end of the game
     * @param winner the winner player
     * @param explanation the string that contain the explaination of the winning
     * @param lands the situation of the lands
     * @param boards the situation of the boards
     * @return ACK or NACK
     */
    public String sendEndGame(Player winner, String explanation, ArrayList<Land> lands, ArrayList<Board> boards){
        name="EndGame";
        try {
            out.writeObject(name);
            out.writeObject(winner);
            out.writeObject(explanation);
            out.writeObject(lands);
            out.writeObject(boards);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies to the players that someone has on elast tower
     * @param player the involved player
     * @return ACK or NACK
     */
    public String sendLastTower(Player player){
        name="LastTower";
        try {
            out.writeObject(name);
            out.writeObject(player);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies the players that there are no more students
     * @return ACK or NACK
     */
    public String sendNoMoreStudents(){
        name="NoMoreStudents";
        try {
            out.writeObject(name);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
//NON C'è ChChosen
    public void sendChanges(){//DA MODIFICARE----------
        name="Changes";
        try {
            out.writeObject(name);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The server notifies the players the one ho has to begin his turn
     * @param player the palyer that begins the turn
     * @param turn
     * @return ACk or NACK
     */
    public String sendNextTurn(Player player, String turn){
        name="NextTurn";
        try {
            out.writeObject(name);
            out.writeObject(player);
            out.writeObject(turn);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Ping message
     * @return ACK or NACK
     */
    public String sendPing(){ //ritorna ACK o NACK
        name="Ping";
        try {
            out.writeObject(name);
            return (String)in.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
