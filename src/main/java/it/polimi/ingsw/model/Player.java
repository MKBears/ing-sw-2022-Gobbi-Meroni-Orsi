package it.polimi.ingsw.model;

public class Player {
    private final int playerID;
    private final Colors color;
    private final Board board;

    public Player (int ID, Colors color){
        playerID = ID;
        this.color = color;
        board= new Board();
    }

    public int getPlayerID() {
        return playerID;
    }

}
