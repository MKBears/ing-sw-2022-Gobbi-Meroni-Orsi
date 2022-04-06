package it.polimi.ingsw.model;

public class Ch_12 implements CharacterCard{

    private final short price;
    private boolean activated;
    private final String powerUp;

    public Ch_12(){
        price=3;
        activated=false;
        powerUp="Choose a type of Student: every player (including yourself) must" +
                "return 3 Students of that type from their Dining Room to the bag." +
                " If any player has fewer than 3 Students of that type, return has many " +
                "Students as they have.";
    }

    @Override
    public void activatePowerUp() {
        //....

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
