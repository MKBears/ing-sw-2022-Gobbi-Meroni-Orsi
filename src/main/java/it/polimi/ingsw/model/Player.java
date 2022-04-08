package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private final int playerID;
    private final Colors color;
    private final Board board;
    private final Wizards wizard;
    private ArrayList<AssistantCard> deck;

    public Player(int ID, Colors color, int towersNum, Wizards wizard){
        playerID = ID;
        this.color = color;
        this.wizard = wizard;
        board= new Board(towersNum, color);
        initializeDeck();
    }

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

    public AssistantCard draw (){
        Random card = new Random();
        return deck.remove(card.nextInt(deck.size()));
    }

    public boolean hasNoCardsLeft(){
        return deck.isEmpty();
    }

}
