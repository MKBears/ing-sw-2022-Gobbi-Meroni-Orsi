package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * contains a link of the Land where it is now
 */
public class MotherNature implements Serializable {
    private Land position;

    /**
     *
     * @param position contains che Land where it is now (constructor)
     */
    public MotherNature (Land position){
        this. position = position;
    }

    /**
     *
     * @return the position of MN wright now
     */
    public Land getPosition() {
        return position;
    }

    /**
     *
     * @param position changes the actual position of MN
     */
    public void setPosition(Land position) {
        this.position = position;
    }
}
