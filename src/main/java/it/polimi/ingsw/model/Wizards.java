package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * The enumeration representing the wizard types
 */
public enum Wizards implements Serializable {
    WIZARD1 (1), WIZARD2 (2), WIZARD3 (3), WIZARD4 (4);
    private final int order;

    Wizards (int order){
        this.order = order;
    }

    /**
     *
     * @return the order of the wizard (e.g. 1 if it's WIZARD1)
     */
    public int getOrder() {
        return order;
    }
}
