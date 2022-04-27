package it.polimi.ingsw.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains all the Students to sort during the game
 */
public class Bag implements Serializable {  //fare che in input mi dicono anche quanti estrarre
    private final List<Student> students=new ArrayList<>();

    public Bag(){
        Student s;
        for (Type_Student t : Type_Student.values()){
            for (int i=0; i<24; i++){
                s = new Student(t);
                students.add(s);
            }
        }
    }

    /**
     *
     * @return a student that has been extracted randomly by the bag
     * @throws Exception
     */
    public Student getRandomStudent() throws Exception{
        if(students.size()>0) {
            Random a = new Random();
            int x = a.nextInt(2000);
            x = x % students.size();
            return students.remove(x);
        }
        else
            throw new Exception("No more students");
    }

}
