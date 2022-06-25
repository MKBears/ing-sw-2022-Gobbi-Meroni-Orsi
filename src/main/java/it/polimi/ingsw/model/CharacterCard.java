package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * The class representing a character card
 */
public interface CharacterCard extends Serializable {

    /**
     * Activates the card's power
     */
    void activatePowerUp() throws Exception;

    /**
     *
     * @return the price of the card (it's increased by 1 of it has been activated at least one time yet)
     */
    short getPrice();

    /**
     *
     * @return true if the card has already been activated at least once
     */
    boolean hasBeenActivated();

    /**
     *
     * @return the description of the power of the card
     */
    String getPowerUp();

    /**
     * Sets the last player to draw this card
     * @param player
     */
    void setPlayer(Player player);

    int getNumber();

    void setActivated();
}
