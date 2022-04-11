package it.polimi.ingsw.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bag {  //fare che in input mi dicono anche quanti estrarre
    private List<Student> students;

    public Bag(){
        Student s;
        students=new ArrayList<>();
        for (Type_Student t : Type_Student.values()){
            for (int i=0; i<26; i++){
                s = new Student(t);
                students.add(s);
            }
        }
    }

    public Student getRandomStudent(){
        Random a=new Random();
        int x=a.nextInt();
        x=x%students.size();
        return students.remove(x);
    }

}
