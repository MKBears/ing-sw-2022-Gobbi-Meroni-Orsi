package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public class Ch_8 implements CharacterCard, Serializable {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private Player player;

    public Ch_8(){
        price=2;
        activated=false;
        powerUp="During this turn, you count as "+
                "having 2 more influence.";
    }

    @Override
    public void activatePowerUp() {
        player.setTwo_more_influence(true);
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

