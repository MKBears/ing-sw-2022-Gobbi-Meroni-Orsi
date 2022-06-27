package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.io.Serializable;

public class Ch_5 implements CharacterCard, Serializable {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Island[] Islands= new Island[4];
    private final Match match;
    private Land land;


    public Ch_5(Match match){
        this.match=match;
        price=2;
        activated=false;
        for(int i=0; i<4; i++){
            Islands[i]=null;
        }
        powerUp="Place a No Entry tile on an Island of your choice. "+"" +
                "The first time Mother Nature ends her movement there, "+"" +
                "put the No Entry tile back onto this card DO NOT calculate influence"+"" +
                " on that Island, or place any Towers.";
    }

    @Override
    public void activatePowerUp() {
        try {
            land.setNoEntry(true);
        } catch (Exception e) {
            e.printStackTrace();
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

    public Island[] getIslands() {
        return Islands;
    }

    @Override
    public void setPlayer(Player player) {
    }

    /**
     *
     * @param land where add the no entry
     */
    public void setLand(Land land) {
        this.land = land;
    }

    @Override
    public int getNumber() {
        return 5;
    }

    public void setActivated(){activated=true;}
}
