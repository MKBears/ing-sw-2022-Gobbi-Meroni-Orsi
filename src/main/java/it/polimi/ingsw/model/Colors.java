package it.polimi.ingsw.model;

public enum Colors {

    WHITE ("white"),
    BLACK ("black"),
    GRAY ("gray");

    private final String color;

    Colors(String color) {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }

}
