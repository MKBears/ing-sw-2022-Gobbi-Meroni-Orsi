package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;

public class Ch_11 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private  ArrayList<Student> students;
    private Bag bag;

    public Ch_11(Bag bag){
        price=2;
        activated=false;
        powerUp="Take 1 Student from this card and place it in your Dining Room. " +
                "Then, draw a new Student from the Bag and place it on this card.";
        students = new ArrayList<>(4);
        this.bag = bag;

        for (int i=0; i<4; i++){
            //students.add(bag.getRandomStudent()); //ERRORE
        }
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
        return students;
    }
}
