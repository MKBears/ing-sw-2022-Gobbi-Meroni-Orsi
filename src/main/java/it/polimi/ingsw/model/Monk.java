package it.polimi.ingsw.model;

public class Monk implements CharacterCard{
    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Bag bag;
    private Student[] students;

    public Monk(Bag bag){
        students = new Student[4];
        price = 1;
        activated = false;
        powerUp = "Choose between the four Students on this card and place it on an " +
                "Island of your choice.";
        this. bag = bag;

        for (int i=0; i<4; i++){
            students[i] = bag.getrandomStudent();
        }
    }

    @Override
    public void activatePowerUp() {
        Island island;
        int student;

        //fa scegliere al player lo studente e l'isola su cui mandarlo

        island.setStudents(students[student]);
        students[student] = bag.getrandomStudent();
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
