package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

public class Ch_5 implements CharacterCard {

    private final short price;
    private boolean activated;
    private final String powerUp;
    private final Island[] Islands= new Island[4];
    private final Match match;
    private Land land;
    private int noEntryCounter;


    public Ch_5(Match match){
        this.match=match;
        price=2;
        activated=false;
        noEntryCounter = 4;
        for(int i=0; i<4; i++){
            Islands[i]=null;
        }
        powerUp="Piazza una tessera divieto su un'isola a tua scelta." +
                "La prima volta che Madre Natura termina il suo movimento lì,"+
                " rimettere la tessera divieto sulla carta SENZA calcolare l'influenza " +
                "su quell'isola ne piazzare torri.";
    }

    @Override
    public void activatePowerUp() throws Exception {
        if (noEntryCounter == 0)
            throw new Exception("Tutti i divieti sono già stati piazzati");
        land.setNoEntry(true);
        noEntryCounter--;

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

    public int getNoEntryCounter() {
        return noEntryCounter;
    }

    public void setActivated(){activated=true;}

    public void returnNoEntryTile() {
        noEntryCounter++;
    }
}
