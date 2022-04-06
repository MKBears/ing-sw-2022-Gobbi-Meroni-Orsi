package it.polimi.ingsw.model;

public class Student{
    private final Type_Student type;

    public Student(Type_Student type) {
        this.type = type;
    }

    public Type_Student getType() {
        return type;
    }
}
