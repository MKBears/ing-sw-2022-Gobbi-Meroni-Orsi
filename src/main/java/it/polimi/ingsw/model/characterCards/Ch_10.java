package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;

public class Ch_10 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;

    public Ch_10(){
        price=1;
        activated=false;
        powerUp="You may excange up to 2 Students between your "+
                "Entrance and your Dining Room.";
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
}
