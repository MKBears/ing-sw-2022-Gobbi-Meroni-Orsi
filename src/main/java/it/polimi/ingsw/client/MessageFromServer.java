package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.serverController.GameRecap;

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
                            cg.setClouds();
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
                            cg.setNotifyMovedStudentBoard((Student) in.readObject(), (String) in.readObject());
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
                        synchronized (in){
                            cg.setEndGame((Player) in.readObject(),(String) in.readObject(),(GameRecap) in.readObject());
                        }
                        break;
                    case "LastTower":
                        synchronized (in){
                            cg.setLastTower((Player) in.readObject());
                        }
                        break;
                    case "NoMoreStudents":
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
                        synchronized (in) {
                            cg.setFinish((Player) in.readObject());
                        }
                        break;
                    case "GenericError":
                            cg.setError((String) in.readObject());
                        break;
                    case "Ch":
                        synchronized (in) {
                            cg.setCh((CharacterCard[]) in.readObject());
                        }
                        break;
                    case "NotifyCh_1":
                        synchronized (in) {
                            cg.setCh_1((Land) in.readObject(), (List<Student>) in.readObject(), (Student) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyCh_2":
                        synchronized (in){
                            cg.setCh_2((Map<Type_Student, Player>) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyCh_4":
                        synchronized (in){
                            cg.setCh_4((String) in.readObject());
                        }
                        break;
                    case "NotifyCh_5":
                        synchronized (in){
                            cg.setCh_5((Land) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyCh_10":
                        synchronized (in){
                            cg.setCh_10((String) in.readObject(), (ArrayList<Student>) in.readObject(), (ArrayList<Type_Student>) in.readObject());
                        }
                        break;
                    case "NotifyCh_11":
                        synchronized (in){
                            cg.setCh_11((ArrayList<Student>) in.readObject(), (String) in.readObject(), (Student) in.readObject());
                        }
                        break;
                    case "NotifyCh_12":
                        synchronized (in){
                            cg.setCh_12((Type_Student) in.readObject(), (String) in.readObject());
                        }
                        break;
                    case "NotifyCh_8":
                        synchronized (in){
                            cg.setCh_8((String) in.readObject());
                        }
                        break;
                    case "NotifyThreeArchipelagos":
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
