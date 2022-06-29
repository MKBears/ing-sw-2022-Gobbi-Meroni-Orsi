package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Type_Student;

import java.io.Serializable;

public class Ch_2 implements CharacterCard {
    private boolean activated;
    private final String powerUp;
    private Player player;
    private final Match match;
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }



    public Ch_2(Match match){
        this.match=match;
        activated = false;
        powerUp = "Durate questo turno, prendi il controllo dei professori " +
                "anche se nella tua sala hai lo stesso numero di studenti del giocatore" +
                "che li controlla in quel momento.";
    }

    @Override
    public void activatePowerUp() {
        for (Type_Student e:match.getProfessors().keySet()) {
            if(match.getProfessors().get(e).getBoard().getStudentsOfType(e)==player.getBoard().getStudentsOfType(e)){
                match.getProfessors().replace(e,player);
            }
        }
        activated = true;
    }

    @Override
    public short getPrice() {
        short price = 2;
        if (activated){
            return (short) (price +1);
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
    @Override
    public int getNumber() {
        return 2;
    }

    public void setActivated(){activated=true;}

}
