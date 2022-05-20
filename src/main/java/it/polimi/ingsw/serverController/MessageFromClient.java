package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Wizards;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class MessageFromClient extends Thread{

    private ObjectInputStream in;
    private Controller controller;
    private ClientHandler ch;
    private String message;
    private boolean nack;
    Map<Student, Integer> map;

    public MessageFromClient(ObjectInputStream in, Controller controller, ClientHandler ch){
        this.in=in;
        this.controller=controller;
        this.ch=ch;
        map = new HashMap<>();
        nack=false;
    }

    public void run(){
        while (true) {
            try {
                message = (String) in.readObject();
                switch (message) {
                    case "ACK":
                        ch.setAck();
                        break;
                    case "Login":
                        String username = (String) in.readObject();
                        ch.setAck();
                        ch.setUsername();
                        break;
                    case "ChoosingGame":  ///Da mettere a posto: ricevo direttamente il match che voglio joinare/resumare
                        String choice = (String) in.readObject();
                        ch.setAck();
                        ch.game(choice);
                        break;
                    case "NewGame":
                        ch.setAck();
                        //decisione da prendere: penso che mandi creation
                        break;
                    case "NACK":
                        ch.setAck();
                        ch.sendMessageAgain();
                        break;
                    case "NumPlayers":
                        int num = (int) in.readObject();
                        ch.setAck();
                        ch.setPlayersNum(num);
                        break;
                    case "Choice":
                        Wizards w = (Wizards) in.readObject();
                        ch.setAck();
                        ch.setWizard(w);
                        break;
                    case "ChosenCard":
                        AssistantCard ass = (AssistantCard) in.readObject();
                        ch.setAck();
                        ch.setChosenCard(ass);
                        break;
                    case "MovedStudent":
                        Student s = (Student) in.readObject();
                        int position = (int) in.readObject();
                        ch.setAck();
                        ch.moveStudent(s, position);
                        map.clear();
                        break;
                    case "StepsMN":
                        int i = (int) in.readObject();
                        ch.setAck();
                        ch.stepsMN(i);
                        break;
                    case "ChoiceCloud":
                        Cloud cloud = (Cloud) in.readObject();
                        ch.setAck();
                        ch.chosenCloud(cloud);
                        break;
                    default:
                        ch.setNack();
                    }
                ch.notify();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setAck(){
        nack=false;
    }

    public void setNack(){
        nack=true;
    }
}
