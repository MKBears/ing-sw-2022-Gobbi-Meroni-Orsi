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
    Map<Student, String> map;

    public MessageFromClient(ObjectInputStream in, Controller controller, ClientHandler ch){
        this.in=in;
        this.controller=controller;
        this.ch=ch;
        map = new HashMap<>();
    }

    public void run(){
        while (true) {
            try {
                message = (String) in.readObject();
                switch (message) {
                    case "ACK":
                        break;
                    case "Login":
                        String username = (String) in.readObject();
                        ch.setUsername();
                        break;
                    case "ChoosingGame":  ///Da mettere a posto: ricevo direttamente il match che voglio joinare/resumare
                        String choice=(String) in.readObject();
                        ch.match(choice);
                        break;
                    case "NACK":
                        ch.sendMessageAgain();
                        break;
                    case "NumPlayers":
                        int num=(int)in.readObject();
                        ch.setPlayersNum(num);
                        break;
                    case "Choice":
                        Wizards w=(Wizards) in.readObject();
                        ch.setWizard(w);
                        break;
                    case "ChosenCard":
                        AssistantCard ass=(AssistantCard) in.readObject();
                        ch.setChosenCard(ass);
                        break;
                    case "Student1":
                    case "Student2":
                        Student s=(Student) in.readObject();
                        String position=(String) in.readObject();
                        if (s != null) {
                            map.put(s,position);
                        } else map.put(null, null);
                        break;
                    case "Student3":
                        Student st=(Student) in.readObject();
                        String positio=(String) in.readObject();
                        if (st != null) {
                            map.put(st,positio);
                        } else map.put(null, null);
                        ch.movedStudent(map);
                        map.clear();
                    case "StepsMN":
                        int i=(int) in.readObject();
                        ch.stepsMN(i);
                        break;
                    case "ChoiceCloud":
                        Cloud cloud=(Cloud) in.readObject();
                        ch.chosenCloud(cloud);
                        break;
                }
                ch.notify();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
