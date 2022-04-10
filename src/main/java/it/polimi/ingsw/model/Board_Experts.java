package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board_Experts extends Board{
    private int coins;

    public Board_Experts(int towersNum, Colors color){
        super(towersNum, color);
        coins=1;
    }

    public int getCoinsNumber() {
        return coins;
    }

    public void payCharacter(int cost) throws Exception{
        if(coins<cost){
            throw new Exception("You don't have enough money");
        }
        else
        {
            coins=coins-cost;
        }
    }

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
