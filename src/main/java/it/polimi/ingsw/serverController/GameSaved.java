package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.Match;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSaved implements Serializable {
    private final Match match;
    private final ArrayList<String> usernames;
    private final int state;
    private final int players_num;
    private final int firstPlayer;
    private final boolean expert_match;

    public GameSaved(Match match, int state, int players_num, int firstPlayer, boolean expert_match,ArrayList<String> usernames) {
        this.match = match;
        this.usernames=usernames;
        this.state= state;
        this.players_num=players_num;
        this.firstPlayer=firstPlayer;
        this.expert_match=expert_match;
    }

    public Match getMatch() {
        return match;
    }

    public int getState() {
        return state;
    }

    public int getPlayers_num() {
        return players_num;
    }

    public int getFirstPlayer() {
        return firstPlayer;
    }

    public boolean isExpert_match() {
        return expert_match;
    }

    public ArrayList<String> getUsernames() {
        return usernames;
    }
}
