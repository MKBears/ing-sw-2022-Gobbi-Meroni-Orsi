package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.Match;

import java.io.Serializable;
import java.util.ArrayList;

public record GameSaved(Match match,
                        int state,
                        int players_num,
                        int firstPlayer,
                        boolean expert_match,
                        ArrayList<String> usernames)
        implements Serializable {
}
