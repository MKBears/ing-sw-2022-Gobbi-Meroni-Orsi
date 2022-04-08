package it.polimi.ingsw.model;

public class AssistantCard {
    private final int value;
    private final int MNSteps;

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
}
