package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private final String userName;
    private final Colors color;
    private final Board board;
    private final Wizards wizard;
    private ArrayList<AssistantCard> deck;

    /**
     * @param userName
     * @param color the color of the towers this player has in their board
     * @param towersNum 8 if there are 2 or 4 players, 6 if the number of players is 3
     * @param wizard associated with the rear of the cards
     * @param expert indicates if we are playing an ordinary match or an expert one
     */
    public Player(String userName, Colors color, int towersNum, Wizards wizard, boolean expert){
        this.userName = userName;
        this.color = color;
        this.wizard = wizard;
        if (expert){
            board = new Board_Experts(towersNum, color);
        }
        else {
            board = new Board(towersNum, color);
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


    public String getUserName() {
        return userName;
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
     * Plays the card with the specified value and removes it from the deck
     * @return Mother Nature's steps of the played card
     */
    public int draw (int value){
        int position = 0;
        for (int i=0; i<deck.size(); i++){
            if (value==deck.get(i).getValue()){
                position = i;
            }
        }
        return deck.remove(position).getMNSteps();
    }

    /**
     *
     * @return true if the deck is empty
     */
    public boolean hasNoCardsLeft(){
        return deck.isEmpty();
    }



}
