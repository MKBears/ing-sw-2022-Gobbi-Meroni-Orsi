package it.polimi.ingsw.model;

public class Player {
    private final int playerID;
    private final Colors color;

    public Player (int ID, Colors color){
        playerID = ID;
        this.color = color;
    }

    public int getPlayerID() {
        return playerID;
    }

}
