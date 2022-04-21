package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CloudTest { //


    Bag bag=new Bag();
    int l=3;
    Cloud cloud;
    Cloud cloudd;
    Bag bagg=new Bag();

    {
        try {
            cloud = new Cloud(bag,2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            cloudd=new Cloud(bagg,3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            cloudd=new Cloud(bagg,5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void controlSetUp(){
        int s=cloud.getStudents().size();
        assertEquals(l,s);
    }

    @Test
    public void studentTest(){
        ArrayList<Student> arr=new ArrayList<>();
        for (Type_Student t : Type_Student.values()){
            for (int i=0; i<26; i++){
                Student s = new Student(t);
                arr.add(s);
            }
        }
        for(int i=0; i<l;i++) {
            assertNull(cloud.getStudents().get(i));
        }
        cloud.importStudents();
        assertTrue(arr.containsAll(cloud.getStudents()));
        for (int j=0; j<500; j++) {
            cloudd.importStudents();
        }
    }

    @Test
    public void choiceTest(){
        assertFalse(cloud.hasBeenChosen());
        cloud.choose();
        assertTrue(cloud.hasBeenChosen());
        cloud.reset();
        assertFalse(cloud.hasBeenChosen());
    }

}