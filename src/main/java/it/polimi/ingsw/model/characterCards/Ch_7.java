package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;

public class Ch_7 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private ArrayList<Student> students=new ArrayList<>(6);
    public Player player;
    public Match match;
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Ch_7(Match match){
        price=1;
        activated=false;
        this.match=match;
        powerUp="You may wake up to 3 Students form this card and"+
                " replace them with the same number of Students from your Entrance.";
        for (int i = 0; i < 3; i++) {
            try {
                students.add(match.getBag().getRandomStudent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void activatePowerUp() {
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
