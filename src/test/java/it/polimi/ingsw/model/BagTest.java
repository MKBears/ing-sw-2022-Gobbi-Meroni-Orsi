package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BagTest {

    Bag bag=new Bag();

    @Test
    public void getRandomStudentTest(){
        ArrayList<Student> arr=new ArrayList<>();
        for (Type_Student t : Type_Student.values()){
            for (int i=0; i<26; i++){
                Student s = new Student(t);
                arr.add(s);
            }
        }
        assertEquals(true, arr.contains(bag.getRandomStudent()));
    }
}