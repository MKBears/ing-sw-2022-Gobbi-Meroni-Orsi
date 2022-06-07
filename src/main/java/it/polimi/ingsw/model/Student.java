package it.polimi.ingsw.model;

import java.io.Serializable;

public record Student(Type_Student type) implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return type == student.type;
    }

    @Override
    public String toString() {
        return type.toString() + "(X)" + "\u001B[0m";
    }

}
