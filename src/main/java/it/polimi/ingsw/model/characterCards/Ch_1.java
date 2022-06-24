package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ch_1 implements CharacterCard, Serializable {
    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Match match;
    private final List<Student> students;
    private Student student;
    private Land land;

    public Ch_1(Match match){
        students = new ArrayList<>();
        price = 1;
        activated = false;
        powerUp = "Choose between the four Students on this card and place it on an " +
                "Island of your choice.";
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
    public void activatePowerUp() {
        land.addStudent(student);
        for (Student s:students) {
            if(s.type().equals(student.type()))   {
                students.remove(s);
                break;
            }
        }
        try {
            students.add(match.getBag().getRandomStudent());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return powerUp;
    }

    public List<Student> getStudents() {
        return students;
    }

    @Override
    public void setPlayer(Player player) {
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setLand(Land land) {
        this.land = land;
    }

    @Override
    public int getNumber() {
        return 1;
    }

}
