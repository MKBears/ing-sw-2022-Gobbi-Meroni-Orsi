package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Type_Student;

import java.io.Serializable;

public class Ch_12 implements CharacterCard, Serializable {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Match match;
    private Type_Student type;


    public Ch_12(Match match){
        this.match=match;
        price=3;
        activated=false;
        powerUp="Scegli un colore di studente; ogni giocatore (incluso te)" +
                "deve rimettere nel sacchetto 3 studenti di quel colore presenti nella sua sala." +
                "Chi avesse meno di 3 studenti di quel colore, rimetterà quelli che ha.";
    }

    @Override
    public void activatePowerUp() {
        for (int i = 0; i < match.getPlayersNum(); i++) {
            match.getBag().ch12effect(match.getPlayer()[i].getBoard().ch_12_effect(type));
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
    }

    public void setType(Type_Student type) {
        this.type = type;
    }

    @Override
    public int getNumber() {
        return 12;
    }

    public void setActivated(){activated=true;}
}
