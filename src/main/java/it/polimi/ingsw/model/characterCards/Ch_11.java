package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.io.Serializable;
import java.util.ArrayList;

public class Ch_11 implements CharacterCard, Serializable {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private ArrayList<Student> students;
    private final Bag bag;
    Player player;
    private Student student;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Ch_11(Bag bag) throws Exception {
        price=2;
        activated=false;
        powerUp="Take 1 Student from this card and place it in your Dining Room. " +
                "Then, draw a new Student from the Bag and place it on this card.";
        students = new ArrayList<>(4);
        this.bag = bag;

        for (int i=0; i<4; i++){
            students.add(bag.getRandomStudent());
        }
    }

    @Override
    public void activatePowerUp() throws Exception {
        for (Student s:this.students) {
            if(student.type().equals(s.type())){
                students.remove(s);
                break;
            }
        }
        player.getBoard().ch_11_effect(student);
        students.add(bag.getRandomStudent());
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

    /**
     *
     * @return the students on this card
     */
    public ArrayList<Student> getStudents() {
        return students;
    }

    /**
     *
     * @param student to be added to the dinning room
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public int getNumber() {
        return 11;
    }

    public ArrayList<Student> copy(){
        ArrayList<Student> result=new ArrayList<>();
        for (Student s:students) {
            result.add(new Student(s.type()));
        }
        return result;
    }

    /**
     * change the student on the card for the client
     * @param students new students of the card
     */
    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void setActivated(){activated=true;}
}
