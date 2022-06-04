package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Colors implements Serializable {

    WHITE ("\u001b[36m"),
    BLACK ("\u001b[30m"),
    GREY ("\u001b[37;1m");

    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }

    @Override
    public String toString() {
        return color;
    }
}
