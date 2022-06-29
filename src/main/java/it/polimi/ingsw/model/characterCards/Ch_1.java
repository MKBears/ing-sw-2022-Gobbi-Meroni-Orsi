package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ch_1 implements CharacterCard {
    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Match match;
    private ArrayList<Student> students;
    private Student student;
    private Land land;

    public Ch_1(Match match){
        students = new ArrayList<>();
        price = 1;
        activated = false;
        powerUp = "Prendi 1 studente dalla carta e piazzalo su un'isola a scelta." +
                " Poi, pesca 1 studente dal sacchetto e mettilo su questa carta.";
        this.match = match;
        for (int i=0; i<4; i++){
            try {
                students.add(match.getBag().getRandomStudent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void activatePowerUp() throws Exception {
        land.addStudent(student);
        for (int i = 0; i < 4; i++) {
            if(students.get(i).type()==student.type()){
                students.remove(i);
                break;
            }
        }
        students.add(match.getBag().getRandomStudent());
        activated = true;
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
        StringBuilder pu = new StringBuilder(powerUp);
        pu.append("\nStudenti sulla carta :  ");

        for (int i=0; i<students.size(); i++)
            pu.append("  ").append(i+1).append(".").append(students.get(i));

        return pu.toString();
    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public void setPlayer(Player player) {
    }

    /**
     *
     * @param student student to be moved
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     *
     * @param land where put the student
     */
    public void setLand(Land land) {
        this.land = land;
    }

    @Override
    public int getNumber() {
        return 1;
    }

    /**
     * student update for the client
     * @param students new students of the card
     */
    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public ArrayList<Student> copy(){
        ArrayList<Student> result=new ArrayList<>();
        for (Student s:students) {
           result.add(new Student(s.type()));
        }
        return result;
    }

    public void setActivated(){activated=true;}
}
