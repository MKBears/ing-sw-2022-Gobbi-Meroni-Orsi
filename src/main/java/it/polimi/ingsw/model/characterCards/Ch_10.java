package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Student;
import it.polimi.ingsw.model.Type_Student;

public class Ch_10 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private Player player;
    private View view;
    public Ch_10(View view){
        this.view=view;
        price=1;
        activated=false;
        powerUp="You may excange up to 2 Students between your "+
                "Entrance and your Dining Room.";
    }


    @Override
    public void activatePowerUp() {
        for (int i = 0; i < 2; i++) {
            Student entrance_student=view.chooseStudent(player.getBoard().getEntrance());
            Type_Student room_student=view.chooseColorStudent();
            player.getBoard().ch_10_effect(entrance_student,room_student);
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

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }
}
