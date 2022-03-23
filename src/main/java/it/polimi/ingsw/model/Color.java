package it.polimi.ingsw.model;

public enum Color {

    WHITE ("white"),
    BLACK ("black"),
    GRAY ("gray");

    private final String color;

    Color(String color) {
        this.color = color;
    }

    public String getColor()
    {
        return color;
    }

}
