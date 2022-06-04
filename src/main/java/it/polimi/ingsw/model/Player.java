package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private final String userName;
    private final Colors color;
    private Board board;
    private final Wizards wizard;
    private ArrayList<AssistantCard> deck;
    private AssistantCard playedCard;

    /**
     * @param userName the userName of the player
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
     * Plays the card with the specified value and removes it from the deck, but saves it as the played card
     * @param assistantCard the played assistant card
     */
    public void draw (AssistantCard assistantCard){
        for (AssistantCard card : deck) {
            if (assistantCard.getValue() == card.getValue()) {
                playedCard = deck.remove(deck.indexOf(card));
                break;
            }
        }
    }

    /**
     *
     * @return the last saved played card
     */
    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    /**
     *
     * @return true if the deck is empty
     */
    public boolean hasNoCardsLeft(){
        return deck.isEmpty();
    }

    @Override
    public String toString() {
        return "Giocatore:" + userName +'\n'+
                "Colore corrispondente: " + color.toString()+color.getName()+"\u001B[0m" +'\n'+
                "con Sala:" + board;
    }

    /**
     *
     * @param board to insert in the class player
     */
    public void setBoard(Board board) {
        this.board = board;
    }
}
