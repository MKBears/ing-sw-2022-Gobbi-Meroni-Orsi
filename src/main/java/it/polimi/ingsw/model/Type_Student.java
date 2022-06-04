package it.polimi.ingsw.model;

import java.io.Serializable;

public enum Type_Student implements Serializable {
    DRAGON("\u001B[31m"), GNOME("\u001B[33;1m"), FAIRY("\u001B[35m"), UNICORN("\u001B[34;1m"), FROG("\u001B[32m");
    private final String name;

    Type_Student(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
