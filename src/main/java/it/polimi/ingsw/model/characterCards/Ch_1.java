package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.client.Action;
import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class Ch_1 implements CharacterCard {
    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Match match;

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    private Player player;
    private List<Student> students;
    View view;

    public Ch_1(Match match,View view){
        students = new ArrayList<>();
        price = 1;
        activated = false;
        powerUp = "Choose between the four Students on this card and place it on an " +
                "Island of your choice.";
        this.match = match;
        this.view=view;
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
        Student student=view.chooseStudent(students);
        Land land= view.chooseLand(match.getLands());
        land.addStudent(student);
        students.remove(student);
        try {
            students.add(match.getBag().getRandomStudent());
        } catch (Exception e) {
            e.printStackTrace();
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

    public List<Student> getStudents() {
        return students;
    }
}
