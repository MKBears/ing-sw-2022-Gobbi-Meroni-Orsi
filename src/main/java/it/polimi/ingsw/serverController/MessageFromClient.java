package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Wizards;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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
                System.out.println(message);
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
                            System.out.println("Ricevuto: "+message);
                            Integer playersNum = (Integer) in.readObject();
                            System.out.println("Ricevuto: "+playersNum);
                            Boolean expert = (Boolean) in.readObject();
                            System.out.println("Ricevuto: "+expert);
                            ch.createMatch(playersNum, expert);
                            System.out.println("Ricevuto num giocatori e expert");
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
                        ch.moveStudent((Student) in.readObject(), in.readInt());
                        ch.setAck(true);
                        break;
                    case "StepsMN":
                        ch.moveMN(in.readInt());
                        ch.setAck(true);
                        break;
                    case "ChoiceCloud":
                        ch.chooseCloud((Cloud) in.readObject());
                        ch.setAck(true);
                        break;
                    case "Nack":
                        ch.sendMessageAgain();
                    case "Pong":
                        missedPongs = 0;

                        if (!ch.isConnected()) {
                            ch.setConnected();
                        }
                    default:
                        System.out.println("Ricevo stringhe strane: "+message);
                        ch.setAck(false);
                }
                synchronized (ch) {
                    ch.notify();
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Ricevo oggetti sconosciuti");
                ch.setAck(false);
            } catch (IOException i) {
                missedPongs++;

                if (missedPongs == 3) {
                    try {
                        ch.setDisconnected();
                    } catch (InterruptedException e) {
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
