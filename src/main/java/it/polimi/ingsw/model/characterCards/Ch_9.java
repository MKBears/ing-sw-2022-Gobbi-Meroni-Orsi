package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;

public class Ch_9 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private Player player;

    public Ch_9(){
        price=3;
        activated=false;
        powerUp="Choose a color of Student: during the influence "+
                "calculation this turn, that color adds no influence.";
    }

    @Override
    public void activatePowerUp() {
        //...

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
