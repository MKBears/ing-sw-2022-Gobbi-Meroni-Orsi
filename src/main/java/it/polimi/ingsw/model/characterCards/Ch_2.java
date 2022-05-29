package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Match;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Type_Student;

public class Ch_2 implements CharacterCard {
    private final short price = 2;
    private boolean activated;
    private final String powerUp;
    private Player player;
    private Match match;
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }



    public Ch_2(Match match){
        this.match=match;
        activated = false;
        powerUp = "During this turn you take control of any number " +
                "of Professors even if you have the same number of Students " +
                "as the player who currently controls them.";
    }

    @Override
    public void activatePowerUp() {
        for (Type_Student e:Type_Student.values()) {
            int a=0;
            int i;
            activated = true;
            for (i = 0; i < match.getPlayersNum(); i++)
                if(match.getPlayer()[i].getBoard().getStudentsOfType(e)>match.getPlayer()[a].getBoard().getStudentsOfType(e))
                    a=i;
            if(player.getBoard().getStudentsOfType(e)>=match.getPlayer()[a].getBoard().getStudentsOfType(e)){
                if(match.getProfessors().containsKey(e))
                    match.getProfessors().replace(e,player);
                else
                    match.getProfessors().put(e,player);
            }else {
                if (match.getProfessors().containsKey(e) && match.getPlayer()[a].getBoard().getStudentsOfType(e) > match.getProfessors().get(e).getBoard().getStudentsOfType(e)) {
                    match.getProfessors().replace(e, match.getPlayer()[a]);
                } else if (!match.getProfessors().containsKey(e) && match.getPlayer()[a].getBoard().getStudentsOfType(e)>0) {
                    match.getProfessors().put(e, match.getPlayer()[a]);
                }
            }
        }
    }

    @Override
    public short getPrice() {
        if (activated){
            return (short) (price+1);
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
}
