package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Wizards;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MessageFromClient extends Thread{

    private final ObjectInputStream in;
    private final ClientHandler ch;
    Map<Student, Integer> map;

    public MessageFromClient(Socket socket, ClientHandler ch) throws IOException {
        in=new ObjectInputStream(socket.getInputStream());
        this.ch=ch;
        map = new HashMap<>();
    }

    public void run(){
        while (true) {
            try {
                String message = (String) in.readObject();
                switch (message) {
                    case "ACK":
                        break;
                    case "Login":
                        String username = (String) in.readObject();
                        ch.setUserName(username);
                        break;
                    case "ChoosingGame":  ///Da mettere a posto: ricevo direttamente il match che voglio joinare/resumare
                        String choice = (String) in.readObject();
                        ch.game(choice);
                        break;
                    case "NewGame":
                        //decisione da prendere: penso che mandi creation
                        break;
                    case "NACK":
                        ch.sendMessageAgain();
                        break;
                    case "NumPlayers":
                        int num = (int) in.readObject();
                        ch.setPlayersNum(num);
                        break;
                    case "Choice":
                        Wizards w = (Wizards) in.readObject();
                        ch.setWizard(w);
                        break;
                    case "ChosenCard":
                        AssistantCard ass = (AssistantCard) in.readObject();
                        ch.setPlayedAssistant(ass);
                        break;
                    case "MovedStudent":
                        Student s = (Student) in.readObject();
                        int position = (int) in.readObject();
                        ch.moveStudent(s, position);
                        map.clear();
                        break;
                    case "StepsMN":
                        int i = (int) in.readObject();
                        ch.moveMN(i);
                        break;
                    case "ChoiceCloud":
                        Cloud cloud = (Cloud) in.readObject();
                        ch.chooseCloud(cloud);
                        break;
                    }
                ch.notify();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
