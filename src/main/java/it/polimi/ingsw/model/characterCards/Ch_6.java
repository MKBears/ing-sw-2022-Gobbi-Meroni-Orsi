package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;

public class Ch_6 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private it.polimi.ingsw.model.Island Island;
    private Player player;


    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Ch_6(){
        price=3;
        activated=false;
        powerUp="When resoling a Conquering on an Island, "+
                "Towers do not count towards influence.";
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

    public Island getIsland() {
        return Island;
    }
}
