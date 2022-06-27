package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The class representing the output stream towards the client and all the possible messages
 */
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
                switch (message) {
                    case "ACK" -> ch.setAck(true);
                    case "Login" -> {
                        ch.setUserName((String) in.readObject());
                        ch.setAck(true);
                    }
                    case "Registration" -> {
                        ch.register((String) in.readObject());
                        ch.setAck(true);
                    }
                    case "ChoosingGame" -> {
                        message = (String) in.readObject();
                        if (message.equals("NewGame")) {
                            Integer playersNum = (Integer) in.readObject();
                            Boolean expert = (Boolean) in.readObject();
                            ch.createMatch(playersNum, expert);
                            //System.out.println("Ricevuto num giocatori e expert");
                            File file=new File("src/main/resources/matches/"+ch.getUserName()+".txt");
                            file.delete();
                            ch.setAck(true);
                        } else {
                            ch.joinMatch(message);
                            ch.setAck(true);
                        }
                    }
                    case "Choice" -> {
                        ch.setWizard((Wizards) in.readObject());
                        ch.setAck(true);
                    }
                    case "ChosenCard" -> {
                        ch.setPlayedAssistant((AssistantCard) in.readObject());
                        ch.setAck(true);
                    }
                    case "MovedStudent" -> {
                        ch.moveStudent((Student) in.readObject(), (int) in.readObject());
                        ch.setAck(true);
                    }
                    case "StepsMN" -> {
                        ch.moveMN((int) in.readObject());
                        ch.setAck(true);
                    }
                    case "ChoiceCloud" -> {
                        ch.chooseCloud((Cloud) in.readObject());
                        ch.setAck(true);
                    }
                    case "NACK" -> ch.sendMessageAgain();
                    case "Pong" -> {
                        missedPongs = 0;
                        if (!ch.isConnected()) {
                            ch.setConnected();
                        }
                    }
                    case "No_Ch" -> {
                        ch.setUseCh(false);
                        ch.setAck(true);
                    }
                    case "Ch_1" -> {
                        Student s = (Student) in.readObject();
                        Land l = (Land) in.readObject();
                        ch.setCh_1_Student(s);
                        ch.setCh_1_land(l);
                        ch.setChosenCh("Ch_1");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    case "Ch_2" -> {
                        ch.setChosenCh("Ch_2");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    case "Ch_4" -> {
                        ch.setChosenCh("Ch_4");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    case "Ch_5" -> {
                        Land land = (Land) in.readObject();
                        ch.setCh_5_land(land);
                        ch.setChosenCh("Ch_5");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    case "Ch_8" -> {
                        ch.setChosenCh("Ch_8");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    case "Ch_10" -> {
                        ArrayList<Student> stds = (ArrayList<Student>) in.readObject();
                        ArrayList<Type_Student> types = (ArrayList<Type_Student>) in.readObject();
                        ch.setCh_10_students(stds);
                        ch.setCh_10_types(types);
                        ch.setChosenCh("Ch_10");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    case "Ch_11" -> {
                        Student st = (Student) in.readObject();
                        ch.setCh_11_student(st);
                        ch.setChosenCh("Ch_11");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    case "Ch_12" -> {
                        Type_Student type = (Type_Student) in.readObject();
                        ch.setCh_12_type(type);
                        ch.setChosenCh("Ch_12");
                        ch.setUseCh(true);
                        ch.setAck(true);
                    }
                    default -> ch.setAck(false);
                }
            } catch (ClassNotFoundException e) {
                try {
                    ch.setAck(false);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } catch (Exception i) {
                missedPongs++;

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

    /**
     * Sets the state to stopped
     */
    public void halt () {
        running = false;
    }
}
