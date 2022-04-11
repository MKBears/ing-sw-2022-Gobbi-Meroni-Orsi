package it.polimi.ingsw.model;

public class Tower {
    private final Colors color;
    private final Board board;

    public Tower (Colors color, Board board){
        this.color = color;
        this.board = board;
    }

    public Colors getColor() {
        return color;
    }

    public Board getBoard() {
        return board;
    }

}
