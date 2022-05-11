package it.polimi.ingsw.serverController;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;

import it.polimi.ingsw.model.*;

public class message4Client {

    private ObjectOutputStream out;
    private String name;

    public message4Client(ObjectOutputStream out){
        this.out=out;
    }

    public void sendACK(){
        name="ACK";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNACK(){
        name="NACK";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendLoginSucceeded(){
        name="LoginSucceeded";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendLoginFailed(){
        name="LoginFailed";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNoGames(){
        name="NoGames";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendListOfGames(ArrayList<String> games){
        name="ListOfGames";
        try {
            out.writeObject(name);
            out.writeObject(games);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendListOfStartedGames(ArrayList<String> games){
        name="ListOfStartedGames";
        try {
            out.writeObject(name);
            out.writeObject(games);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCreation(Match match){
        name="Creation";
        try {
            out.writeObject(name);
            out.writeObject(match);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendWizard(ArrayList<Wizards> wizards){
        name="Wizard";
        try {
            out.writeObject(name);
            out.writeObject(wizards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRefillClouds(ArrayList<Student> students1, ArrayList<Student> students2){
        name="RefillClouds";
        try {
            out.writeObject(name);
            out.writeObject(students1);
            out.writeObject(students2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRefillClouds(ArrayList<Student> students1, ArrayList<Student> students2, ArrayList<Student> students3){
        name="RefillClouds";
        try {
            out.writeObject(name);
            out.writeObject(students1);
            out.writeObject(students2);
            out.writeObject(students3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRefillClouds(ArrayList<Student> students1, ArrayList<Student> students2, ArrayList<Student> students3, ArrayList<Student> students4){
        name="RefillClouds";
        try {
            out.writeObject(name);
            out.writeObject(students1);
            out.writeObject(students2);
            out.writeObject(students3);
            out.writeObject(students4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChooseCard(ArrayList<AssistantCard> cards){
        name="ChooseCard";
        try {
            out.writeObject(name);
            out.writeObject(cards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMoveStudents(){
        name="MoveStudents";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChooseCloud(ArrayList<Cloud> clouds){
        name="ChooseCloud";
        try {
            out.writeObject(name);
            out.writeObject(clouds);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyChosenCard(AssistantCard card, Player player){
        name="NotifyChoosenCard";
        try {
            out.writeObject(name);
            out.writeObject(card);
            out.writeObject(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyMoveStudents(Student student, Player player, int id){
        name="NotifyMoveStudents (id)";
        try {
            out.writeObject(name);
            out.writeObject(student);
            out.writeObject(player);
            out.writeObject(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyMoveStudents(Student student, Player player, Board board){
        name="NotifyMoveStudents (board)";
        try {
            out.writeObject(name);
            out.writeObject(student);
            out.writeObject(player);
            out.writeObject(board);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyMovementMN(int id, ArrayList<Land> lands){
        name="NotifyMovementMN";
        try {
            out.writeObject(name);
            out.writeObject(id);
            out.writeObject(lands);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyProfessors(Map<Type_Student, Player> professors){
        name="NotifyProfessors";
        try {
            out.writeObject(name);
            out.writeObject(professors);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyChosenCloud(Player player, Cloud cloud){
        name="NotifyChosenCloud";
        try {
            out.writeObject(name);
            out.writeObject(player);
            out.writeObject(cloud);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyTowers(ArrayList<Tower> towers, Land land){
        name="NotifyTowers (land)";
        try {
            out.writeObject(name);
            out.writeObject(towers);
            out.writeObject(land);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNotifyTowers(ArrayList<Tower> towers, Board board){
        name="NotifyTowers (board)";
        try {
            out.writeObject(name);
            out.writeObject(towers);
            out.writeObject(board);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEndGame(Player winner, String explanaton, ArrayList<Land> lands, ArrayList<Board> boards){
        name="EndGame";
        try {
            out.writeObject(name);
            out.writeObject(winner);
            out.writeObject(explanaton);
            out.writeObject(lands);
            out.writeObject(boards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendLastTower(Player player){
        name="LastTower";
        try {
            out.writeObject(name);
            out.writeObject(player);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNoMoreStudents(){
        name="NoMoreStudents";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendChanges(){//DA MODIFICARE
        name="Changes";
        try {
            out.writeObject(name);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendNextTurn(Player player, String turn){
        name="NextTurn";
        try {
            out.writeObject(name);
            out.writeObject(player);
            out.writeObject(turn);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPing(){
        name="Ping";
        try {
            out.writeObject(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
