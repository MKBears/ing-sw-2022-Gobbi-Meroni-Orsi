package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Type_Student implements Serializable {
    DRAGON("\u001B[31m"+"Drago   "), GNOME("\u001B[33m"+"Folletto"), FAIRIE("\u001B[35m"+"Fatina  "), UNICORN("\u001B[34m"+"Unicorno"), FROG("\u001B[32m"+"Rana    ");
    private final String name;

    Type_Student(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
