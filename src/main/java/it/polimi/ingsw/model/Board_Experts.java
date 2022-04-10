package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board_Experts extends Board{
    private int coins;

    /**
     *
     * @param towersNum 8 if there are 2 or 4 players, 6 if the number of players is 3
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
     * Subtracts the cost of the activation of the power of a character card from the coins collected in the board
     * @param cost represents the cost a character card needs to be paid in coins to activate its power
     * @throws Exception if there are not enough coins to activate the power of the card
     */
    public void payCharacter(int cost) throws Exception{
        if(coins<cost){
            throw new Exception("You don't have enough money");
        }
        else
        {
            coins=coins-cost;
        }
    }

    /**
     * Adds a single student to the entrance
     * @param student
     */
    public void addStudent(Student student){
        ArrayList<Student> s = getEntrance();
        s.add(student);
        setEntrance(s);
    }

    @Override
    public void placeStudent(Student student) throws Exception{
        final int c1 = 2, c2 = 5, c3 = 8;
        super.placeStudent(student);

        if (getStudentsOfType(student.getType()) == c1 ||
                getStudentsOfType(student.getType()) == c2 ||
                getStudentsOfType(student.getType()) == c3){
            coins++;
        }
    }
    
}
