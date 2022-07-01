package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.client.Action;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;

public class Ch_10 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private Player player;
    private ArrayList<Student> entrance_student;
    private ArrayList<Type_Student> room_student;
    private Match match;

    public Ch_10(Match match){
        price=1;
        activated=false;
        powerUp="Puoi scambiare fra loro fino a 2 studenti presenti nella tua sala e nel tuo ingresso.";
        this.match=match;
    }


    @Override
    public void activatePowerUp() {
        for (int i = 0; i < entrance_student.size(); i++) {
            player.getBoard().ch_10_effect(entrance_student.get(i),room_student.get(i));
        }
        Action action=new Action(match);
        action.checkAllProfessors();
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

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     *
     * @param entrance_student student moved from entrance to dinning room
     */
    public void setEntrance_student(ArrayList<Student> entrance_student) {
        this.entrance_student = entrance_student;
    }

    /**
     *
     * @param room_student type of students moved from dinning room to entrance
     */
    public void setRoom_student(ArrayList<Type_Student> room_student) {
        this.room_student = room_student;
    }

    @Override
    public int getNumber() {
        return 10;
    }

    public void setActivated(){activated=true;}

}
