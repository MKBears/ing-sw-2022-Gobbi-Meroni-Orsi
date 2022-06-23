package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MessageFromClient extends Thread{

    private final ObjectInputStream in;
    private final ClientHandler ch;
    private boolean running;
    private int missedPongs;

    public MessageFromClient(Socket socket, ClientHandler ch) throws IOException {
        in = new ObjectInputStream(socket.getInputStream());
        this.ch = ch;
        missedPongs = 0;
    }

    public void run(){
        running = true;
        while (running) {
            try {
                String message = (String) in.readObject();
                System.out.println("Ricevuto: "+message);
                switch (message) {
                    case "ACK":
                        ch.setAck(true);
                        break;
                    case "Login":
                        ch.setUserName((String) in.readObject());
                        ch.setAck(true);
                        break;
                    case "Registration":
                        ch.register((String) in.readObject());
                        ch.setAck(true);
                        break;
                    case "ChoosingGame":
                        message = (String) in.readObject();

                        if (message.equals("NewGame")) {
                            //System.out.println("Ricevuto: "+message);
                            Integer playersNum = (Integer) in.readObject();
                            //System.out.println("Ricevuto: "+playersNum);
                            Boolean expert = (Boolean) in.readObject();
                            //System.out.println("Ricevuto: "+expert);
                            ch.createMatch(playersNum, expert);
                            //System.out.println("Ricevuto num giocatori e expert");
                            ch.setAck(true);
                        }
                        else {
                            ch.joinMatch(message);
                            ch.setAck(true);
                        }
                        break;
                    case "Choice":
                        ch.setWizard((Wizards) in.readObject());
                        ch.setAck(true);
                        break;
                    case "ChosenCard":
                        ch.setPlayedAssistant((AssistantCard) in.readObject());
                        ch.setAck(true);
                        break;
                    case "MovedStudent":
                        ch.moveStudent((Student) in.readObject(), (int) in.readObject());
                        ch.setAck(true);
                        break;
                    case "StepsMN":
                        ch.moveMN((int) in.readObject());
                        ch.setAck(true);
                        break;
                    case "ChoiceCloud":
                        ch.chooseCloud((Cloud) in.readObject());
                        ch.setAck(true);
                        break;
                    case "NACK":
                        ch.sendMessageAgain();
                        break;
                    case "Pong":
                        missedPongs = 0;

                        if (!ch.isConnected()) {
                            ch.setConnected();
                        }
                        break;
                    case "No_Ch":
                        ch.setUseCh(false);
                        ch.setAck(true);
                        break;
                    case "Ch_1":
                        Student s=(Student) in.readObject();
                        Land l=(Land)in.readObject();
                        ch.setCh_1_Student(s);
                        ch.setCh_1_land(l);
                        ch.setChosenCh("Ch_1");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    case "Ch_2":
                        ch.setChosenCh("Ch_2");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    case "Ch_4":
                        ch.setChosenCh("Ch_4");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    case "Ch_5":
                        Land land=(Land)in.readObject();
                        ch.setCh_5_land(land);
                        ch.setChosenCh("Ch_5");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    case "Ch_3":
                        Land lan=(Land)in.readObject();
                        ch.setCh_3_land(lan);
                        ch.setChosenCh("Ch_3");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    case "Ch_10":
                        ArrayList<Student> stds=(ArrayList<Student>)in.readObject();
                        ArrayList<Type_Student> types=(ArrayList<Type_Student>)in.readObject();
                        ch.setCh_10_students(stds);
                        ch.setCh_10_types(types);
                        ch.setChosenCh("Ch_10");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    case "Ch_11":
                        Student st=(Student)in.readObject();
                        ch.setCh_11_student(st);
                        ch.setChosenCh("Ch_11");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    case "Ch_12":
                        Type_Student type=(Type_Student)in.readObject();
                        ch.setCh_12_type(type);
                        ch.setChosenCh("Ch_12");
                        ch.setUseCh(true);
                        ch.setAck(true);
                        break;
                    default:
                        System.out.println("Player "+ch.getUserName()+": "+"Ricevo stringhe strane: "+message);
                        ch.setAck(false);
                        break;
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Player "+ch.getUserName()+": "+"Ricevo oggetti sconosciuti");
                try {
                    ch.setAck(false);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception i) {
                missedPongs++;
                System.out.println("Player "+ch.getUserName()+": "+i.getMessage());

                if (missedPongs == 3) {
                    try {
                        ch.setDisconnected();
                        running = false;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        try {
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void halt () {
        running = false;
    }
}
