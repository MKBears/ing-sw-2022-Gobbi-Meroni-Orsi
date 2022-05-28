package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Archipelago;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;

public class Ch_3 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private Player player;
    private Match match;
    private View view;

    public Ch_3(Match match,View view){
        this.match=match;
        this.view=view;
        price=3;
        activated=false;
        powerUp="Choose an Island and resolve the Island as if "+
                "Mothere Nature had ended her movement there."+
                " Mother Nature will still move and the island where" +
                " she ends her movement will also be resolved.";
    }

    @Override
    public void activatePowerUp() {

        if(!activated){
            activated=true;
        }
    }

    @Override
    public short getPrice() {
        if (activated) {
            return (short) (price + 1);
        } else {
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

    //public GroupOfIslands getIslands() {
    //    return Islands;
    //}

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }
}
