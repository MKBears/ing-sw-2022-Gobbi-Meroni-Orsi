package it.polimi.ingsw.model;

import java.io.Serializable;

public class Student implements Serializable {
    final private Type_Student type;

    public Student(Type_Student type){
        this.type=type;
    }

    public Type_Student type() {
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
        return type.toString() + "(X)" + "\u001B[0m";
    }

}
