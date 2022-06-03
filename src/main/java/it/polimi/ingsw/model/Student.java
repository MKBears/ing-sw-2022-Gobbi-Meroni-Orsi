package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;

public class Student implements Serializable {
    private final Type_Student type;

    public Student(Type_Student type) {
        this.type = type;
    }

    public Type_Student getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return type == student.type;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
