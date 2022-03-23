package it.polimi.ingsw.model;

public class Player {
    private final int playerID;
    private final Color color;

    public Player (int ID, Color color){
        playerID = ID;
        this.color = color;
    }

    public int getPlayerID() {
        return playerID;
    }

}
