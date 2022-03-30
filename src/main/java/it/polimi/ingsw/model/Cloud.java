package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Cloud {

    private Student[] students;
    private final Bag bag;
    private boolean hasBeenChosen;

    public Cloud (Bag bag){
        students = new Student[3];
        this.bag = bag;
    }

    public void importStudents(){
        for (int i=0; i<3; i++){
            students[i] = bag.getrandomStudent();
        }
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> s = new ArrayList<>();
        for (int i=0; i<3; i++){
            s.add(students[i]);
            students[i] = null;
        }
        return s;
    }

    public void choose(){
        hasBeenChosen = true;
    }

    public boolean hasBeenChosen(){
        return hasBeenChosen;
    }

    public void reset(){
        hasBeenChosen = false;
    }

}
