package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * The class representing a tower
 */
public class Tower implements Serializable {
    private final Colors color;
    private final Board board;

    /**
     * Builds a tower of the specified color
     * @param color
     * @param board the board in which the tower is initially built
     */
    public Tower (Colors color, Board board){
        this.color = color;
        this.board = board;
    }

    /**
     *
     * @return the color of the tower
     */
    public Colors getColor() {
        return color;
    }

    /**
     *
     * @return the board of the tower (even if it has been moved to an island)
     */
    public Board getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "torre di colore "+ color +' ';
    }
}
