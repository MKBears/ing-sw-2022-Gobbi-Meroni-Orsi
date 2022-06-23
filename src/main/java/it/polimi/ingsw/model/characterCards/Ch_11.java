package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;

public class Ch_11 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private  ArrayList<Student> students;
    private Bag bag;
    Player player;
    private Student student;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Ch_11(Bag bag){
        price=2;
        activated=false;
        powerUp="Take 1 Student from this card and place it in your Dining Room. " +
                "Then, draw a new Student from the Bag and place it on this card.";
        students = new ArrayList<>(4);
        this.bag = bag;

        for (int i=0; i<4; i++){
            try {
                students.add(bag.getRandomStudent());
            }
            catch (Exception e){};
        }
    }

    @Override
    public void activatePowerUp() {
        for (Student s:this.students) {
            if(student.type().equals(s.type())){
                students.remove(s);
                break;
            }
        }
        player.getBoard().ch_11_effect(student);
        try {
            students.add(bag.getRandomStudent());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setStudent(Student student) {
        this.student = student;
    }
}
