package it.polimi.ingsw.model;

public class Ch_4 implements CharacterCard{

    private final short price;
    private boolean activated;
    private final String powerUp;

    public Ch_4(){
        price=1;
        activated=false;
        powerUp="You may move Mother Naure up to 2 additional Islands"+
                " than is indicated by the Assistant card you've played.";
    }

    @Override
    public void activatePowerUp() {
        //...

        if (!activated){
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
