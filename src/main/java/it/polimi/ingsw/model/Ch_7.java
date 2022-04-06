package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Ch_7 implements CharacterCard{

    private final short price;
    private boolean activated;
    private final String powerUp;
    private ArrayList<Student> Students=new ArrayList<>(6);

    public Ch_7(ArrayList<Student> Input){
        price=1;
        activated=false;
        powerUp="You may wake up to 3 Students form this card and"+
                " replace them with the same number of Students from your Entrance.";
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
