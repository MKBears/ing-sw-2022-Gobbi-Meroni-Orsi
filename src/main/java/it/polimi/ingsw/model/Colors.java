package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Colors implements Serializable {

    WHITE ("white"),
    BLACK ("black"),
    GREY ("grey");

    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }

}
