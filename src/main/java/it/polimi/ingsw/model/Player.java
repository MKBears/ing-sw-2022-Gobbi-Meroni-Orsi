package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private final int playerID;
    private final Colors color;
    private final Board board;
    private final Wizards wizard;
    private ArrayList<AssistantCard> deck;

    /**
     * @param ID
     * @param color the color of the towers this player has in their board
     * @param towersNum 8 if there are 2 or 4 players, 6 if the number of players is 3
     * @param wizard associated with the rear of the cards
     * @param expert indicates if we are playing an ordinary match or an expert one
     */
    public Player(int ID, Colors color, int towersNum, Wizards wizard, boolean expert, ArrayList<Student>entrance){
        playerID = ID;
        this.color = color;
        this.wizard = wizard;
        if (expert){
            board = new Board_Experts(towersNum, color, entrance);
        }
        else {
            board = new Board(towersNum, color, entrance);
        }
        initializeDeck();
    }

    /**
     * Instantiates and initializes the deck with all 10 assistant cards
     */
    private void initializeDeck(){
        AssistantCard temp;
        deck = new ArrayList<>(10);

        for (int i=1; i<=10; i++){
            temp = new AssistantCard(i,(i+1)/2);
            deck.add(temp);
        }
    }


    public int getPlayerID() {
        return playerID;
    }

    public Colors getColor() {
        return color;
    }

    public Wizards getWizard() {
        return wizard;
    }

    public Board getBoard() {
        return board;
    }

    public ArrayList<AssistantCard> getDeck() {
        return (ArrayList<AssistantCard>) deck.clone();
    }

    /**
     * @return a random card from the deck (and removes it)
     */
    public void draw (AssistantCard card){
        deck.remove(card);
    }

    /**
     *
     * @return true if the deck is empty
     */
    public boolean hasNoCardsLeft(){
        return deck.isEmpty();
    }



}
