package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.List;

public interface View {

    /**
     * request for the username
     * @return username of the player
     */
    String getUsername();

    /**
     * Sets the instance of class Message4Server to send messages to the server
     * @param server
     */
    void setServer(Message4Server server);

    /**
     * request of the wizard
     * @param wizards wizard that can be chosen
     * @return the wizard chosen
     */
    Wizards getWizard(List<Wizards> wizards);

    /**
     * request to choose the cloud
     * @param clouds that can be chosen
     * @return the cloud chosen
     */
    Cloud getCloud(List<Cloud> clouds);

    /**
     * request to choose the assistant card
     * @param cards that can be chosen
     * @return the card chosen
     */
    AssistantCard getAssistantCard(List<AssistantCard> cards);

    /**
     * request for the number of steps
     * @param pl the player who want to move mother nature
     * @return the number of step
     */
    int getNumStep(Player pl);

    /**
     * comunicate the player who win the match
     * @param pl player who win
     */
    void getWinner(Player pl);

    /**
     * request of where move the student
     * @param match match of the player
     * @return int that express where move the student (12 for the board if less than 12 is the id of the land)
     */
    int getDestination(Match match);

    /**
     * show the match
     * @param match match of the player
     */
    void printMatch(Match match);

    /**
     * show the turn
     * @param pl player of the turn
     * @param phase phase of the match
     */
    void printTurn(Player pl,String phase);

    /**
     * show that it is the last round
     */
    void lastRound();

    /**
     * choose of a student in the entrance to be moved
     * @param pl player who have to move the student
     * @return the student chosen
     */
    Student getStudent(Player pl);

    /**
     * show the title
     */
    void getTitolo();

    /**
     * wake up the thread from a wait to do a new request
     * @param state request that the thread has to do to the player
     */
    void wakeUp(String state);

    /**
     * Sets the instance of the player who runs Eriantys' CLI
     * @param me
     */
    void setMe(Player me);

    /**
     * Sets the instance of the match and initializes action.
     * If there is an expert match, this method sets attribute characters
     * @param match
     */
    void setMatch(Match match);

    /**
     * Sets the lists of available assistants cards to play
     * @param cards
     */
    void setCards(List<AssistantCard> cards);

    /**
     * Sets the list of available wizards
     * @param willy
     */
    void setWilly(List<Wizards> willy);

    /**
     * sets the list of available clouds to choose between
     * @param clouds
     */
    void setClouds(List<Cloud> clouds);

    /**
     * Asks the user to choose between joining, resuming o creating a new match
     * @param join the list of matches the player can join
     * @param resume the list of matches the player can resume
     */
    void chooseMatch(List<String> join,List<String> resume);

    /**
     * set nack tru to resend the parameters to the sever
     */
    void setNack();

    /**
     * Asks the player if they want to register and then the username
     * @return the username
     */
    String chooseLogin();

    /**
     * Asks the player to choose a land to put the student on
     * @param lands all the lands in the sky
     * @return the chosen land
     */
    Land chooseLand(List<Land> lands);

    /**
     * Asks the player to choose a student to move from the entrance (used when playing a character card)
     * @param student the entrance
     * @return the chosen student
     */
    Student chooseStudent(List<Student> student);

    /**
     * Asks the player to choose a color of which not to compute influence on a land
     * @return
     */
    Type_Student chooseColorStudent();

    /**
     * Notifies the player if another one connects to the match
     * @param username the username of the connected player
     */
    void playerConnected(String username);

    /**
     * Notifies the player if another one disconnects from the match
     * @param username the username of the disconnected player
     */
    void playerDisconnected(String username);

    /**
     * Notifies the player if all the other players are disconnected from the match
     */
    void playerDisconnectedAll();

    /**
     * Notifies the player if another player finished their assistant cards
     * @param p the player who finished their assistants
     */
    void finishedAC(Player p);

    /**
     * Asks the user if they want to play a character card
     * @param cards the available character cards
     * @return the chosen card
     */
    CharacterCard chooseChCard(CharacterCard[] cards);

    /**
     * Sets the available character cards
     * @param characters
     */
    void setCharacters(CharacterCard[] characters);

    /**
     * Prints the received notification
     * @param message the message to print
     */
    void printNotification (String message);
}
