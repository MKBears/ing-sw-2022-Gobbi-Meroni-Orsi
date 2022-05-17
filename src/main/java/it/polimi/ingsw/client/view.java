package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.List;

public interface view {
    String getUsername();
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
}
