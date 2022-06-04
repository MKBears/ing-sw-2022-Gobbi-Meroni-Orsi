package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.util.Map;

public class GameRecap {
    int playersNum;
    String[] players;
    Colors[] colors;
    Wizards[] wizards;
    int[] builtTowers;
    int[] controlledProfessors;

    public GameRecap (ClientHandler[] players, Match match) {
        playersNum = players.length;
        this.players = new String[playersNum];
        colors = new Colors[playersNum];
        wizards = new Wizards[playersNum];
        builtTowers = new int[playersNum];
        controlledProfessors = new int[playersNum];

        fillFields(players, match);
        fieldSort();
    }

    private void fillFields(ClientHandler[] players, Match match) {
        Map<Type_Student, Player> professors = match.getProfessors();

        for (int i=0; i<playersNum; i++){
            this.players[i] = players[i].getUserName();
            colors[i] = players[i].getColor();
            wizards[i] = players[i].getAvatar().getWizard();

            switch (playersNum) {
                case 3:
                    builtTowers[i] = 6-players[i].getAvatar().getBoard().getTowersNum();
                    break;
                case 2:
                    builtTowers[i] = 8-players[i].getAvatar().getBoard().getTowersNum();
                    break;
                case 4:
                    //boh
                    break;
            }
            controlledProfessors[i] = 0;

            for (Type_Student professor : Type_Student.values()) {
                if (professors.get(professor).equals(players[i].getAvatar())) {
                    controlledProfessors[i]++;
                }
            }
        }
    }

    private void fieldSort() {
        int maxIndex;
        String tempPlayer;
        Colors tempColor;
        Wizards tempWizard;
        int tempTowers, tempProfessors;

        for (int i=0; i<playersNum-1; i++) {
            maxIndex = i;

            for (int j=i+1; j<playersNum; j++) {
                if (builtTowers[j] > builtTowers[maxIndex]) {
                    maxIndex = j;
                } else if (builtTowers[j] == builtTowers[maxIndex]) {
                    if (controlledProfessors[j] > controlledProfessors[maxIndex]) {
                        maxIndex = j;
                    }
                }
            }

            if (maxIndex != i) {
                tempPlayer = players[i];
                tempColor = colors[i];
                tempWizard = wizards[i];
                tempTowers = builtTowers[i];
                tempProfessors = controlledProfessors[i];

                players[i] = players[maxIndex];
                colors[i] = colors[maxIndex];
                wizards[i] = wizards[maxIndex];
                builtTowers[i] = builtTowers[maxIndex];
                controlledProfessors[i] = controlledProfessors[maxIndex];

                players[maxIndex] = tempPlayer;
                colors[maxIndex] = tempColor;
                wizards[maxIndex] = tempWizard;
                builtTowers[maxIndex] = tempTowers;
                controlledProfessors[maxIndex] = tempProfessors;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(new StringBuilder());

        for (int i = 0; i< playersNum; i++) {
            string.append(i).append(".\t").append(players[i]).
                    append("\t").append(colors[i].toString()).
                    append("\t").append(wizards[i].toString()).
                    append("\t").append(builtTowers[i]).
                    append("\t").append(controlledProfessors[i]);
            if (i != playersNum-1) {
                string.append("\n");
            }
        }

        return string.toString();
    }
}
