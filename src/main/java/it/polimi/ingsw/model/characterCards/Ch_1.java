package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Student;

public class Ch_1 implements CharacterCard {
    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Bag bag;
    private Student[] students;

    public Ch_1(Bag bag){
        students = new Student[4];
        price = 1;
        activated = false;
        powerUp = "Choose between the four Students on this card and place it on an " +
                "Island of your choice.";
        this. bag = bag;

        for (int i=0; i<4; i++){
            students[i] = bag.getRandomStudent();
        }
    }

    @Override
    public void activatePowerUp() {
        Island island;
        int student;

        //fa scegliere al player lo studente e l'isola su cui mandarlo

        //island.setStudents(students[student]);
        //in match bisogna fare in modo di mandare sempre riferimenti alla prima isola di un gruppo
        students[student] = bag.getRandomStudent();
        if (!activated){
            activated = true;
        }
    }

    @Override
    public short getPrice() {
        if (activated){
            return (short) (price+1);
        }
        else {
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

    public Student[] getStudents() {
        return students;
    }
}
