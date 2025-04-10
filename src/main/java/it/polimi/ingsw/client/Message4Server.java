package it.polimi.ingsw.client;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Wizards;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import it.polimi.ingsw.model.*;

/**
 * This class contains all the possible message to send to the sever
 */
public class Message4Server {
    private final ObjectOutputStream out;
    private String name;

    /**
     *
     * @param out the out parameter for TCP connection
     */
    Message4Server(ObjectOutputStream out){
        this.out=out;
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
     * @param username of the player
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

    /**
     * When receives ping responds with pong
     */
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
    }

    /**
     * Sends true if the player wants to play an expert match, elsewhere sends false
     * @param expert
     */
    public void sendExpertMatch (boolean expert) {
        synchronized (this) {
            try {
                out.writeObject(expert);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
    public void sendMovedStudent(Student stu, int pos ){
        synchronized (this) {
            try {
                name = "MovedStudent";
                out.writeObject(name);
                out.writeObject(stu);
                out.writeObject(pos);
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

    /**
     * message to play the character card 1
     * @param student student moved
     * @param land where move the student
     */
    public void sendChooseCh1(Student student,Land land){
        synchronized (this){
            try{
                name= "Ch_1";
                out.writeObject(name);
                out.writeObject(student);
                out.writeObject(land);
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
    }

    /**
     * message to play the character card 2
     */
    public void sendChooseCh2(){
        synchronized (this){
            try{
                name= "Ch_2";
                out.writeObject(name);
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
    }

    /**
     * message to play the character card 4
     */
    public void sendChooseCh4(){
        synchronized (this){
            try{
                name= "Ch_4";
                out.writeObject(name);
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
    }

    /**
     * message to play the character card 5
     * @param land to set no entry
     */
    public void sendChooseCh5(Land land) {
        synchronized (this) {
            try {
                name = "Ch_5";
                out.writeObject(name);
                out.writeObject(land);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
    }


    /**
     * message to use the character card 11
     * @param student student to be placed in the dinning room
     */
    public void sendChooseCh11(Student student){
        synchronized (this){
            try{
                name= "Ch_11";
                out.writeObject(name);
                out.writeObject(student);
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
    }

    /**
     * message for use the character card 12
     * @param type_student to be removed from the dinning room
     */
    public void sendChooseCh12(Type_Student type_student){
        synchronized (this){
            try{
                name="Ch_12";
                out.writeObject(name);
                out.writeObject(type_student);
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
    }

    /**
     * message for decide to not use a character card
     */
    public void sendNoCh(){
        synchronized (this){
            try{
                name="No_Ch";
                out.writeObject(name);
            }catch(IOException e){
                throw new RuntimeException();
            }
        }
    }

    /**
     * message for the character card 10
     * @param students to be moved from the entrance to the dinning room
     * @param type_students type of the student removed from the dinning room and placed in the entrance
     */
    public void sendChooseCh10(ArrayList<Student> students,ArrayList<Type_Student> type_students){
        synchronized (this){
            try{
                name="Ch_10";
                out.writeObject(name);
                out.writeObject(students);
                out.writeObject(type_students);
            }catch(IOException e){
                throw new RuntimeException();
            }
        }
    }


    /**
     * message to use the character card 8
     */
    public void sendChooseCh8(){
        synchronized (this){
            try{
                name="Ch_8";
                out.writeObject(name);
            }catch(IOException e){
                throw new RuntimeException();
            }
        }
    }
}
