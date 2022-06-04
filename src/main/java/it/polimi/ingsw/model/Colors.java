package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Colors implements Serializable {

    WHITE ("\u001b[36m", "Bianco"),
    BLACK ("\u001b[30m", "Nero"),
    GREY ("\u001b[37;1m", "Grigio");

    private final String color;
    private final String name;

    Colors(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return color;
    }
}
