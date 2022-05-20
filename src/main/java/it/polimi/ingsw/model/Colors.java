package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Colors implements Serializable {

    WHITE ("Bianco"),
    BLACK ("Nero"),
    GREY ("Grigio");

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
