package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * The class representing an assistant card
 */
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

    /**
     *
     * @return the value of the card
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return the maximum mn steps written on the card
     */
    public int getMNSteps() {
        return MNSteps;
    }

    /**
     * Activates 4th character card's power and increases the number of steps by 2
     */
    public void ch_4_effect(){MNSteps += 2;}

    @Override
    public String toString() {
        String assistant =  "\n _______ \n"+
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
