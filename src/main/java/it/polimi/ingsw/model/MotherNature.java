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

    /*
       In questo modo facciamo calcolare la nuova posizione di MN alla classe Match, che invia qui direttamente
        la nuova isola, ma si potrebbe anche far calcolare direttamente qui la nuova posizione inviando solo
        un intero/short col numero di passi che MN deve fare (pero' serve un riferimento alle isole anche in
        questa classe)
     */

    /**
     *
     * @param position changes the actual position of MN
     */
    public void setPosition(Land position) {
        this.position = position;
    }
}
