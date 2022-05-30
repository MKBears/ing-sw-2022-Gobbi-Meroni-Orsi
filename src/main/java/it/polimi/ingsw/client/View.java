package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public interface View {
    String getUsername();
    void setServer(Message4Server server);
    Wizards getWizard(List<Wizards> wizards);
    Cloud getCloud(List<Cloud> clouds);
    AssistantCard getAssistantCard(List<AssistantCard> cards);
    int getNumStep(Player pl);
    void getWinner(Player pl);
    String getDestination(Match match);
    void printMatch(Match match);
    void printTurn(Player pl,String phase);
    void lastRound();
    int getNumPlayer();
    Student getStudent(Player pl);
    void getTitolo();
    void wakeUp(String state);
    void setMe(Player me);
    void setMatch(Match match);
    void setCards(List<AssistantCard> cards);
    void setWilly(List<Wizards> willy);
    void setClouds(List<Cloud> clouds);
    void chooseMatch(List<String> join,List<String> resume);
    void setNack();
    String chooseLogin();
    Land chooseLand(List<Land> lands);
    Student chooseStudent(List<Student> student);
    Type_Student chooseColorStudent();
    void playerConnected(String username);
    void playerDisconnected(String username);
    void playerDisconnectedAll();
    void finishedAC(Player p);
    void chooseChCard(CharacterCard[] cards);
}
