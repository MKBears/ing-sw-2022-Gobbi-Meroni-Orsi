package it.polimi.ingsw.model;

import java.io.Serializable;

public class AssistantCard implements Serializable{
    private final int value;
    private int MNSteps;

    /**
     *
     * @param value the value to determine the first player to move Mother Nature
     * @param steps the maximum number of steps Mother Nature could take after a player played this card
     */
    public AssistantCard (int value, int steps){
        this.value = value;
        MNSteps = steps;
    }

    public int getValue() {
        return value;
    }

    public int getMNSteps() {
        return MNSteps;
    }

    public void ch_4_effect(){MNSteps += 2;}

    @Override
    public String toString() {
        String assistant =  " _______ \n"+
                            "| "+value;

        if (value < 10)
            assistant += " ";

        assistant +=    "  "+MNSteps+" |\n"+
                        "|  /\\\\  |\n"+
                        "| //_\\\\ |\n"+
                        "|//   \\\\|\n"+
                        "|_______|";

        return assistant;
    }
}
