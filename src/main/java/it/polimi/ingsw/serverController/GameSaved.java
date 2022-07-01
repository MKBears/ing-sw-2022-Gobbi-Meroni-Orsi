package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.Match;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Type of data in memory for saving and resuming game
 * @param match the state of the match
 * @param state state of the controller
 * @param players_num number of players
 * @param firstPlayer the first player who have to play
 * @param expert_match if it is an Expert_Match
 * @param usernames List of the usernames of the players
 */
public record GameSaved(Match match,
                        int state,
                        int players_num,
                        int firstPlayer,
                        boolean expert_match,
                        ArrayList<String> usernames)
        implements Serializable {
}
