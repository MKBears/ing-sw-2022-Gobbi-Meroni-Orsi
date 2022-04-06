package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;

public class Ch_11 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private  ArrayList<Student> Students=new ArrayList<>(4);

    public Ch_11(ArrayList<Student> Input){
        price=2;
        activated=false;
        powerUp="Take 1 Student from this card and place it in your Dining Room. " +
                "Then, draw a new Student from the Bag and place it on this card.";
        Students=Input;
    }

    @Override
    public void activatePowerUp() {
        //...

        if(!activated){
            activated=true;
        }

    }

    @Override
    public short getPrice() {
        if(activated){
            return (short)(price+1);
        }
        else{
            return price;
        }
    }

    @Override
    public boolean hasBeenActivated() {
        return activated;
    }

    @Override
    public String getPowerUp() {
        return powerUp;
    }

    public ArrayList<Student> getStudents() {
        return Students;
    }
}
