package it.polimi.ingsw.model;


import java.util.List;
import java.util.Random;

public class Bag {
    private List<Student> students;


    public Student getrandomStudent(){
        Random a=new Random();
        int x=a.nextInt();
        x=x%students.size();
        return students.remove(x);
    }
}
