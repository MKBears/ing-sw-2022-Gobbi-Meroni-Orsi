package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Ch_11 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private ArrayList<Student> students;
    private final Match match;
    Player player;
    private Student student;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Ch_11(Match match) throws Exception {
        price=2;
        activated=false;
        powerUp="Prendi 1 studente da questa carta e piazzalo nella tua sala. " +
                "Poi pesca un nuovo studente dal sacchetto e posizionalo su questa carta.";
        students = new ArrayList<>(4);
        this.match = match;

        for (int i=0; i<4; i++){
            students.add(match.getBag().getRandomStudent());
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
        match.checkProfessor(student.type());
        students.add(match.getBag().getRandomStudent());
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
        StringBuilder pu = new StringBuilder(powerUp);
        pu.append("\n");

        if (students.isEmpty())
            pu.append("\tNon ci sono studenti su questa carta.");
        else {

            for (Student value : students)
                pu.append("    ").append(value.toString());
        }

        return pu.toString();
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
