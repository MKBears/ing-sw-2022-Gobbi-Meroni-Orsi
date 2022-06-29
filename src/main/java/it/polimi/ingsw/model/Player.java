package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The class representing a player in the match
 */
public class Player implements Serializable {
    private final String userName;
    private final Colors color;
    private Board board;
    private final Wizards wizard;
    private ArrayList<AssistantCard> deck;
    private AssistantCard playedCard;
    private boolean two_more_influence;

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
        two_more_influence=false;
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

    /**
     *
     * @return the player's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     *
     * @return the player's towers' color
     */
    public Colors getColor() {
        return color;
    }

    /**
     *
     * @return the player's wizard
     */
    public Wizards getWizard() {
        return wizard;
    }

    /**
     *
     * @return the player's board
     */
    public Board getBoard() {
        return  board;
    }

    /**
     *
     * @return the player's deck
     */
    public ArrayList<AssistantCard> getDeck() {
        return (ArrayList<AssistantCard>) deck.clone();
    }

    /**
     * Plays the card with the specified value and removes it from the deck, but saves it as the played card
     * @param assistantCard the played assistant card
     */
    public void draw (AssistantCard assistantCard){
        for (int i=0;i<deck.size();i++) {
            if (assistantCard.getValue() == deck.get(i).getValue()) {
                playedCard = deck.remove(i);
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
        return "" + color.toString() + megaUserName().indent(26) + "\u001B[0m" + board.toString().indent(22) +'\n';
    }

    /**
     * Auxiliary method used by toString method in order to get the player's username in a fancy way
     * @return the player's username
     */
    private String megaUserName () {
        String userName = this.userName.toLowerCase();
        StringBuilder megaName = new StringBuilder();

        for (int i=0; i<userName.length(); i++) {
            switch (userName.charAt(i)) {
                case 'a', 'v', 'm', 'n' -> megaName.append("     ");
                case 'b', 'r', 'p', 'd', 'g', '8', '9', '0', '5', '6', '2' -> megaName.append(" _  ");
                case 'c', 'f', 'e', 's' -> megaName.append(" _ ");
                case 't' -> megaName.append("___ ");
                case 'i', 'j', '.' -> megaName.append("  ");
                case 'q', 'o', 'k', 'l', 'x', ' ', '4', '/' -> megaName.append("   ");
                case 'w' -> megaName.append("       ");
                case 'z', '7' -> megaName.append("__ ");
                case '1', '3' -> megaName.append("_  ");
                default -> megaName.append("    ");
            }
        }

        megaName.append("\n");

        for (int i=0; i<userName.length(); i++) {
            switch (userName.charAt(i)) {
                case 'a' -> megaName.append(" /\\  ");
                case 'b' -> megaName.append("|_) ");
                case 'r', 'u', '0' -> megaName.append("| | ");
                case 'c' -> megaName.append("/  ");
                case 'g' -> megaName.append("/_  ");
                case 'e', 'f' -> megaName.append("|_ ");
                case 'd' -> megaName.append("| \\ ");
                case 'h', 'p', 'y' -> megaName.append("|_| ");
                case 'i' -> megaName.append("| ");
                case 'j' -> megaName.append("┐ ");
                case 'k' -> megaName.append("|/ ");
                case 'l' -> megaName.append("|  ");
                case 'm' -> megaName.append("|\\/| ");
                case 'n' -> megaName.append("|\\ | ");
                case 'q', 'o' -> megaName.append("/\\ ");
                case 's' -> megaName.append("/_ ");
                case 't' -> megaName.append(" |  ");
                case 'v' -> megaName.append("\\  / ");
                case 'w' -> megaName.append("\\    / ");
                case 'x' -> megaName.append("\\/ ");
                case 'z' -> megaName.append(" / ");
                case '1' -> megaName.append(" | ");
                case '2' -> megaName.append(" _| ");
                case '3' -> megaName.append("_| ");
                case '4' -> megaName.append(" / ");
                case '5' -> megaName.append("|_  ");
                case '6' -> megaName.append("|_  ");
                case '7', '/' -> megaName.append(" / ");
                case '8', '9' -> megaName.append("|_| ");
                case '.' -> megaName.append("  ");
                default -> megaName.append("   ");
            }
        }

        megaName.append("\n");

        for (int i=0; i<userName.length(); i++) {
            switch (userName.charAt(i)) {
                case 'a' -> megaName.append("/‾‾\\ ");
                case 'b' -> megaName.append("|_) ");
                case 'c' -> megaName.append("\\_ ");
                case 'd' -> megaName.append("|_/ ");
                case 'e', 'l' -> megaName.append("|_ ");
                case 'f' -> megaName.append("|  ");
                case 'g' -> megaName.append("\\_| ");
                case 'h' -> megaName.append("| | ");
                case 'i' -> megaName.append("| ");
                case 'j' -> megaName.append("⌡ ");
                case 'k' -> megaName.append("|\\ ");
                case 'm' -> megaName.append("|  | ");
                case 'n' -> megaName.append("| \\| ");
                case 'o' -> megaName.append("\\/ ");
                case 'p' -> megaName.append("|   ");
                case 'q' -> megaName.append("\\X ");
                case 'r' -> megaName.append("|‾\\ ");
                case 's' -> megaName.append("_/ ");
                case 't' -> megaName.append(" |  ");
                case 'u' -> megaName.append("|_| ");
                case 'v' -> megaName.append(" \\/  ");
                case 'w' -> megaName.append(" \\/\\/  ");
                case 'x' -> megaName.append("/\\ ");
                case 'y' -> megaName.append(" _| ");
                case 'z' -> megaName.append("/_ ");
                case '_' -> megaName.append("___");
                case '1' -> megaName.append(" | ");
                case '2' -> megaName.append("|_  ");
                case '3' -> megaName.append("_| ");
                case '4' -> megaName.append("/┼ ");
                case '5', '9' -> megaName.append(" _| ");
                case '6', '8', '0' -> megaName.append("|_| ");
                case '7', '/' -> megaName.append("/  ");
                case '.' -> megaName.append("o ");
                default  -> megaName.append("   ");
            }
        }

        megaName.append("\n");

        return megaName.toString();
    }

    /**
     *
     * @param board to insert in the class player
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * effect of the card
     * @param two_more_influence true if the player has played the character card
     */
    public void setTwo_more_influence(boolean two_more_influence) {
        this.two_more_influence = two_more_influence;
    }

    /**
     *
     * @return true if this turn he has two more influence
     */
    public boolean isTwo_more_influence() {
        return two_more_influence;
    }
}
