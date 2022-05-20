package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Wizards implements Serializable {
    WIZARD1, WIZARD2, WIZARD3, WIZARD4;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case (0):
                return "mago 1\n";
            case (1):
                return "mago 2\n";
            case (2):
                return "mago 3\n";
            case (3):
                return "mago 4\n";
        }
    return null;
    }
}
