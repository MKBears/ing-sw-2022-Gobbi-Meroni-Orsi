package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;

public class AssistantCard implements Serializable{
    private final int value;
    private final int MNSteps;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssistantCard that = (AssistantCard) o;
        return value == that.value && MNSteps == that.MNSteps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, MNSteps);
    }
}
