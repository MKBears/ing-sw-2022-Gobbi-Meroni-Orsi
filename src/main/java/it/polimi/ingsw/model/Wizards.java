package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Wizards implements Serializable {
    WIZARD1, WIZARD2, WIZARD3, WIZARD4;

    @Override
    public String toString() {
        switch (this.ordinal()) {
            case (0):
                return "mago 1\n"+
                        " _____\n"+
                        "|  1  |\n"+
                        "| $ $ |\n" +
                        "|  () |\n"+
                        "|_____|\n";
            case (1):
                return "mago 2\n"+
                        " _____\n"+
                        "|  2  |\n"+
                        "| ° ° |\n" +
                        "| ___ |\n"+
                        "|_____|\n";
            case (2):
                return "mago 3\n"+
                        " _____\n"+
                        "|  3  |\n"+
                        "| *_* |\n" +
                        "|     |\n"+
                        "|_____|\n";
            case (3):
                return "mago 4\n"+
                        " _____\n"+
                        "|  4  |\n"+
                        "| ^ ^ |\n" +
                        "|  o  |\n"+
                        "|_____|\n";
        }
    return null;
    }
}
