package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * The enumeration of the colors of each player' towers
 */
public enum Colors implements Serializable {

    WHITE ("\u001b[37m", "Bianco"),
    BLACK ("\u001b[30;1m", "Nero"),
    GREY ("\u001b[35;1m", "Grigio");

    private final String color;
    private final String name;

    Colors(String color, String name) {
        this.color = color;
        this.name = name;
    }

    /**
     *
     * @return the name of the clor
     */
    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return color;
    }


}
