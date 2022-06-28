package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageFromServer extends Thread{
    private String message;
    private ObjectInputStream in;
    private Message4Server server;
    private ClientGui cg;

    public MessageFromServer(ObjectInputStream in, Message4Server server, ClientGui cg){
        message=null;
        this.in=in;
        this.server=server;
        this.cg=cg;
    }

    @Override
    public void run() {
        while (true) {
            try {
                message = (String) in.readObject();
                if(!message.equals("Ping")) {
                    System.out.println("Ricevuto: " + message);
                }
                switch (message) {
                    case "Ping":
                        server.sendPONG();
                        //System.out.println("Mandato pong");
                        break;
                    case "NACK": //non lo soooo
                        break;
                    case "ListOfGames":
                        cg.setJoinandResume((ArrayList<String>) in.readObject(), (ArrayList<String>) in.readObject());
                        break;
                    case "Wizard":
                        cg.setWilly((ArrayList<Wizards>) in.readObject());
                        break;
                    case "Creation":
                        synchronized (in) {
                            cg.setMatch((Match) in.readObject());
                        }
                        break;
                    case "RefillClouds":
                        synchronized (in) {
                            cg.setStudentsClouds();
                        }
                        break;
                    case "ChooseCard": //"ChooseCard"
                        synchronized (in) {
                            cg.setCards((List<AssistantCard>) in.readObject());
                        }
                        break;
                    case "ChooseCloud":
                        synchronized (in) {
                            cg.setClouds((ArrayList<Cloud>) in.readObject());
                        }
                        break;
                    case "NotifyChosenCard":
                        synchronized (in) {
                            cg.setChosenCard((AssistantCard) in.readObject(), (Player) in.readObject());
                        }
                        break;
                    case "NotifyAllPlayersDisconnected":
                    case "ACK":
                    case "MoveStudents":
                    case "MoveMN":
                        break;
                    case "NotifyMoveStudents (id)":
                        synchronized (in) {
                            cg.setNotifyMovedStudentId((Student) in.readObject(), (int) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyMoveStudents (board)":
                        synchronized (in) {
                            cg.setNotifyMovedStudentBoard((Student) in.readObject(), (Board) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyMovementMN":
                        synchronized (in) {
                            cg.setNotifyMovementMN((int) in.readObject(), (ArrayList<Land>) in.readObject());
                        }
                        break;
                    case "NotifyProfessors":
                        synchronized (in) {
                            cg.setProf((Map<Type_Student, Player>) in.readObject());
                        }
                        break;
                    case "NotifyChosenCloud":
                        synchronized (in) {
                            Player p=(Player) in.readObject();
                            Cloud c=(Cloud) in.readObject();
                            cg.setNotifyChosenCLoud(p,c);
                        }
                        break;
                    case "NotifyTowers (land)":
                        synchronized (in) {
                            cg.setNotifyTowersLand((ArrayList<Tower>) in.readObject(), (Land) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyTowers (board)":
                        synchronized (in) {
                            cg.setNotifyTowersBoard((ArrayList<Tower>) in.readObject(), (Board) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "EndGame":
                        //qualcosa
                        break;
                    case "LastTower":
                        //qualcosa
                        break;
                    case "NoMoreStudents":
                        //qualcosa
                        break;
                    case "NextTurn":
                        synchronized (in) {
                            cg.setNextTurn((Player) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyPlayerConnected":
                            cg.setNotifyPlayerConnected((String) in.readObject(), (boolean) in.readObject());
                        break;
                    case "FinishedAssistants":
                        //qualcosa
                        break;
                    case "GenericError":
                        //qualcosa
                        break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if(!message.equals("Ping") && !message.equals("ACK")) {
                synchronized (cg) {
                    cg.setReceived(message);
                    cg.notifyAll();
                }
            }
        }
    }
}
