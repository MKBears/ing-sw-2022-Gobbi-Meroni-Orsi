package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Wizards;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import it.polimi.ingsw.model.*;

/**
 * This class contains all the possible message to send to the sever
 */
public class Message4Server {
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String name;
    private String message;

    /**
     *
     * @param in the in parameter for TCP connection
     * @param out the out parameter for TCP connection
     */
    Message4Server(ObjectInputStream in, ObjectOutputStream out){
        this.out=out;
        this.in=in;
    }

    /**
     * The client sends the username of this player
     * @param username the username
     */
    public void sendLogin(String username) {
        synchronized (this) {
            name = "Login";
            try {
                out.writeObject(name);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

  }
    }

    /**
     * The client sends the username for a new avatar
     * @param username
     */
    public void sendRegistration(String username) {
        synchronized (this) {
            name = "Registration";
            try {
                out.writeObject(name);
                out.writeObject(username);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The client sends which type of game wants to do
     * @param choice the choice: it could be the name of a game to resume or join or a string "NewGame" that means the played decides to start a new game
     */
    public void sendChoosingGame(String choice){
        synchronized (this) {
            try {
                name = "ChoosingGame";
                out.writeObject(name);
                out.writeObject(choice);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Base positive response for a client request (the message received was correct)
     */
    public void sendACK(){
        synchronized (this) {
            try {
                name = "ACK";
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void sendPONG(){
        synchronized (this) {
            try {
                name = "Pong";
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Base negative response for a client request (the message received was correct)
     */
    public void sendNACK(){
        synchronized (this) {
            try {
                name = "NACK";
                out.writeObject(name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The client sends the number of players selected by the main player
     * @param num the number of players
     */
    public void sendNumPlayers(int num){
        synchronized (this) {
            try {
                out.writeObject(num);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Inviato: "+num);
    }

    public void sendExpertMatch (boolean expert) {
        synchronized (this) {
            try {
                out.writeObject(expert);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Inviato: "+expert);
    }

    /**
     * The player sends his choice about the wizard he wants
     * @param wizard the chosen wizard
     */
    public void sendChoice(Wizards wizard){
        synchronized (this) {
            try {
                name = "Choice";
                out.writeObject(name);
                out.writeObject(wizard);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The player sends his choice about the assistant card
     * @param card the chosen card
     */
    public void sendChosenCard(AssistantCard card){
        synchronized (this) {
            try {
                name = "ChosenCard";
                out.writeObject(name);
                out.writeObject(card);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The player sends the movement of the student, we have to call it for each student
     * @param stu the student involved
     * @param pos must be the id of the land or the board (board is 12, the lands are 0-11)
     */
    public void sendMovedStudent(Student stu, int pos ){  //dai la possibilità di mettere null negli ultimi due se non ne muove tre
        synchronized (this) {
            try {
                name = "MovedStudent";
                out.writeObject(name);
                out.writeObject(stu);
                out.writeObject(pos);
                System.out.println("mandato movedstudent");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The player sends his choice about the next movement of Mother Nature
     * @param steps the steps
     */
    public void sendStepsMN(int steps){
        synchronized (this) {
            try {
                name = "StepsMN";
                out.writeObject(name);
                out.writeObject(steps);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * The player sends the chosen cloud
     * @param cloud the chosen cloud
     */
    public void sendChoiceCloud(Cloud cloud){
        synchronized (this) {
            try {
                name = "ChoiceCloud";
                out.writeObject(name);
                out.writeObject(cloud);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
