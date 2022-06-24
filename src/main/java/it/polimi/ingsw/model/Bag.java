package it.polimi.ingsw.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Contains all the Students to sort during the game
 */
public class Bag implements Serializable {
    private final List<Student> students=new ArrayList<>();

    /**
     * Constructor of class Bag: fills the bag with 24 students of each type
     */
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
     * @throws Exception if there are no more students in the bag
     */
    public Student getRandomStudent() throws Exception{
        if(students.size()>0) {
            Random a = new Random();
            int x = a.nextInt(200);
            x = x % students.size();
            return students.remove(x);
        }
        else
            throw new Exception("No more students");
    }

    /**
     * Activates 12th character card's power and puts
     * all the students removed from each player's board again in the bag
     * @param students
     */
    public void ch12effect(List<Student> students){
        this.students.addAll(students);
    }
}
