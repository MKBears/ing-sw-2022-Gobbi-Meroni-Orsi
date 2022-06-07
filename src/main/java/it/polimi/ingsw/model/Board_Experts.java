package it.polimi.ingsw.model;

/**
 * The class representing the board each player controls in an experts match.
 * Contains the same things as Board (non-experts) does and has the same methods.
 * Moreover, contains all the coins gained during the game by the player and has a method to activate the power of a Character card.
 */
public class Board_Experts extends Board {
    private int coins;

    /**
     *
     * @param towersNum 8: 2 players;
     *                  6: 3 players;
     *                  0 or 8: 4 players
     * @param color the color of the towers
     */
    public Board_Experts(int towersNum, Colors color){
        super(towersNum, color);
        coins=1;
    }

    public int getCoinsNumber() {
        return coins;
    }

    /**
     * Controls if the player has enough coins to play the specified card and activates its power
     * @param card the card a player chooses to play
     * @throws Exception if there are not enough coins to activate the card's power
     */
    public void playCharacter(CharacterCard card) throws Exception{
        int cost = card.getPrice();

        if(coins<cost){
            throw new Exception("You don't have enough money to activate this card.");
        }
        else
        {
            coins=coins-cost;
            card.activatePowerUp();
        }
    }

    @Override
    public void placeStudent(Student student) throws Exception{
        final int c1 = 2, c2 = 5, c3 = 8;
        super.placeStudent(student);

        if (getStudentsOfType(student.type()) == c1 ||
                getStudentsOfType(student.type()) == c2 ||
                getStudentsOfType(student.type()) == c3){
            coins++;
        }
    }

    @Override
    public String toString() {
        return  super.toString()+
                "coins= " + coins;
    }
}
