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

    /**
     *
     * @return the number of coins held by the player on their board
     */
    public int getCoinsNumber() {
        return coins;
    }

    /**
     * Controls if the player has enough coins to play the specified card and activates its power
     * @param card the card a player chooses to play
     */
    public void playCharacter(CharacterCard card) throws Exception {
        int cost = card.getPrice();
        coins -= cost;

        try {
            card.activatePowerUp();
        } catch (Exception e) {
            coins += cost;
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void placeStudent(Student student) throws Exception{
        final int c1 = 3, c2 = 6, c3 = 9;
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
                "\n\u001b[33m(¤)\u001B[0mx" + coins;
    }

    public void subCoin(int s){
        coins=coins-s;
    }
}
