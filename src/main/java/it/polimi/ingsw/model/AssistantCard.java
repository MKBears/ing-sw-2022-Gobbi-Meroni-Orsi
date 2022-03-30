package it.polimi.ingsw.model;

public class AssistantCard {
    private final short value;
    private final short MNSteps;

    public AssistantCard (short value, short steps){
        this.value = value;
        MNSteps = steps;
    }

    public short getValue() {
        return value;
    }

    public short getMNSteps() {
        return MNSteps;
    }
}
