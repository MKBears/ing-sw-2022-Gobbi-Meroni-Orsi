package it.polimi.ingsw.model;

public interface CharacterCard {

    void activatePowerUp();

    short getPrice();

    boolean hasBeenActivated();

    String getPowerUp();
}
