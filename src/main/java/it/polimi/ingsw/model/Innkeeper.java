package it.polimi.ingsw.model;

public class Innkeeper implements CharacterCard{
    private final short price;
    private boolean activated;
    private final String powerUp;
    private Player player;

    public Innkeeper(){
        price = 2;
        activated = false;
        powerUp = "During this turn you take control of any number " +
                "of Professors even if you have the same number of Students " +
                "as the player who currently controls them.";
    }

    @Override
    public void activatePowerUp() {
        //...

        if (!activated) {
            activated = true;
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
