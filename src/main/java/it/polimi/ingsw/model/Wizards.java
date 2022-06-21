package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Wizards implements Serializable {
    WIZARD1 (1), WIZARD2 (2), WIZARD3 (3), WIZARD4 (4);
    int order;

    Wizards (int order){
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
