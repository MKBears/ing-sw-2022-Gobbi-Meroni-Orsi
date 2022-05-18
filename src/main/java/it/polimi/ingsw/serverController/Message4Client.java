package it.polimi.ingsw.serverController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.model.*;

/**
 * This class contains all the possible message to send to the client
 */
public class Message4Client extends Thread{  //METTI DENTRO RUN DEL PING

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String name;
    private String message;
    private ClientHandler ch;

    /**
     *
     * @param out the out parameter for TCP connection
     * @param in the in parameter for TCP connection
     */
    public Message4Client(ObjectInputStream in, ObjectOutputStream out){
        this.out=out;
        this.in= in;
    }

    /**
     * Base positive response for a client request (the message received was correct)
     */
    public void sendACK(){
        synchronized (this) {
            name = "ACK";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Base negative response for a client request (the message received was not correct)
     */
    public void sendNACK(){
        synchronized (this) {
            name = "NACK";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * When the username is correct
     */
    public void sendLoginSucceeded(){
        synchronized (this) {
            name = "LoginSucceeded";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * When the username is incorrect
     */
    public void sendLoginFailed(){
        synchronized (this) {
            name = "LoginFailed";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * When there is no game started with the selection of the player
     */
    public void sendNoGames(){
        synchronized (this) {
            name = "NoGames";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * When selected "join game"
     * @param joinGames a list of games that is possible to join
     * @param resumeGames a list of games that is possible to resume
     * @return the name of the chosen game or "ERROR"
     */
    public void sendListOfGames(ArrayList<String> joinGames, ArrayList<String> resumeGames){  //unisci  con started
        synchronized (this) {
            name = "ListOfGames";
            try {
                out.writeObject(name);
                out.writeObject(joinGames);
                out.writeObject(resumeGames);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * It sends the client all the actual match (initialized)
     * @param match the actual match
     */
    public void sendCreation(Match match){
        synchronized (this) {
            name = "Creation";
            try {
                out.writeObject(name);
                out.writeObject(match);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Asks the clinent which wizard whants to choose
     * @param wizards a list of the possible wizard to choose
     */
    public void sendWizard(ArrayList<Wizards> wizards) { //ritorna il wizard scelto
        synchronized (this) {
            name = "Wizard";
            try {
                out.writeObject(name);
                out.writeObject(wizards);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server sends the clouds full of new students
     * @param newclouds list of the new clouds full of new students
     */
    public void sendRefillClouds(ArrayList<Cloud> newclouds){
        synchronized (this) {
            name = "RefillClouds";
            try {
                out.writeObject(name);
                out.writeObject(newclouds);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client to choose the assistant card
     * @param cards list of the possible card to choose
     */
    public void sendChooseCard(ArrayList<AssistantCard> cards){
        synchronized (this) {
            name = "ChooseCard";
            try {
                out.writeObject(name);
                out.writeObject(cards);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client to tell him where the player decides to move the students
     */
    public void sendMoveStudents(){
        synchronized (this) {
            name = "MoveStudents";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client the cloud that choses the player
     * @param clouds the possible clouds to chose
     */
    public void sendChooseCloud(ArrayList<Cloud> clouds){
        synchronized (this) {
            name = "ChooseCloud";
            try {
                out.writeObject(name);
                out.writeObject(clouds);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the chosen card
     * @param card the chosen card
     * @param player the player that chose the card
     */
    public void sendNotifyChosenCard(AssistantCard card, Player player){
        synchronized (this) {
            name = "NotifyChosenCard";
            try {
                out.writeObject(name);
                out.writeObject(card);
                out.writeObject(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server asks the client the steps the player decides for Mother Nature
     */
    public void sendMoveMN(){
        synchronized (this) {
            name = "MoveMN";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the movement of someone's students, we have to call it for every student moved
     * @param student the signle student
     * @param player the students's player
     * @param id the id of the island or archipelago
     */
    public void sendNotifyMoveStudents(Student student, Player player, int id, String username){
        synchronized (this) {
            name = "NotifyMoveStudents (id)";
            try {
                out.writeObject(name);
                out.writeObject(student);
                out.writeObject(player);
                out.writeObject(id);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the movement of someone's students, we have to call it for every student moved
     * @param student the signle student
     * @param player the student's player
     * @param board the board of the player
     */
    public String sendNotifyMoveStudents(Student student, Player player, Board board, String username){
        synchronized (this) {
            name = "NotifyMoveStudents (board)";
            try {
                out.writeObject(name);
                out.writeObject(student);
                out.writeObject(player);
                out.writeObject(board);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the new position of Mother Nature
     * @param id the id of the island
     * @param lands an update of the situation of the lands
     */
    public String sendNotifyMovementMN(int id, ArrayList<Land> lands){
        synchronized (this) {
            name = "NotifyMovementMN";
            try {
                out.writeObject(name);
                out.writeObject(id);
                out.writeObject(lands);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the changes of the professors situation
     * @param professors the professors of the board
     */
    public String sendNotifyProfessors(Map<Type_Student, Player> professors){
        synchronized (this) {
            name = "NotifyProfessors";
            try {
                out.writeObject(name);
                out.writeObject(professors);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the slients the cloud chosen by a player
     * @param player the player that chose
     * @param cloud the chosen cloud
     */
    public String sendNotifyChosenCloud(Player player, Cloud cloud){
        synchronized (this) {
            name = "NotifyChosenCloud";
            try {
                out.writeObject(name);
                out.writeObject(player);
                out.writeObject(cloud);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the clients the situation of the towers in the game
     * @param towers
     * @param land the land involved
     */
    public void sendNotifyTowers(ArrayList<Tower> towers, Land land, String username){
        synchronized (this) {
            name = "NotifyTowers (land)";
            try {
                out.writeObject(name);
                out.writeObject(towers);
                out.writeObject(land);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the clients the situation of the towers in the game
     * @param towers
     * @param board the board involved
     */
    public void sendNotifyTowers(ArrayList<Tower> towers, Board board, String username){
        synchronized (this) {
            name = "NotifyTowers (board)";
            try {
                out.writeObject(name);
                out.writeObject(towers);
                out.writeObject(board);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to all the clients the end of the game
     * @param winner the winner player
     * @param explanation the string that contain the explaination of the winning
     * @param lands the situation of the lands
     * @param boards the situation of the boards
     */
    public void sendEndGame(Player winner, String explanation, ArrayList<Land> lands, ArrayList<Board> boards){
        synchronized (this) {
            name = "EndGame";
            try {
                out.writeObject(name);
                out.writeObject(winner);
                out.writeObject(explanation);
                out.writeObject(lands);
                out.writeObject(boards);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies to the players that someone has on elast tower
     * @param player the involved player
     */
    public String sendLastTower(Player player){
        synchronized (this) {
            name = "LastTower";
            try {
                out.writeObject(name);
                out.writeObject(player);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the players that there are no more students
     */
    public String sendNoMoreStudents(){
        synchronized (this) {
            name = "NoMoreStudents";
            try {
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
//NON C'è ChChosen
    public void sendChanges(){//DA MODIFICARE----------
        synchronized (this) {
            name = "Changes";
            try {
                out.writeObject(name);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The server notifies the players the one ho has to begin his turn
     * @param player the palyer that begins the turn
     * @param turn
     */
    public String sendNextTurn(Player player, String turn){
        synchronized (this) {
            name = "NextTurn";
            try {
                out.writeObject(name);
                out.writeObject(player);
                out.writeObject(turn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*/**
     * Ping message
     * @return Pong or nothing
     *
    public String sendPing() throws IOException { //ritorna ACK o NACK
        name="Ping";
        out.writeObject(name);
        try {
            return (String)in.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }*/
    public void run(){
        boolean condition=true;

        while (condition) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                name="Ping";
                synchronized (this) {
                    out.writeObject(name);
                    name = (String) in.readObject();
                }
            } catch (ClassNotFoundException | IOException e) {
                ch.setDisconnected();
            }

        }

    }
}
