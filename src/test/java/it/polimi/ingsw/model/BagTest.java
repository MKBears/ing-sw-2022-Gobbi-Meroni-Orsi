package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BagTest {

    Bag bag=new Bag();

    @Test
    public void getRandomStudentTest() throws Exception {
        ArrayList<Student> arr=new ArrayList<>();
        for (Type_Student t : Type_Student.values()){
            for (int i=0; i<26; i++){
                Student s = new Student(t);
                arr.add(s);
            }
        }

        int size=arr.size();
        ArrayList<Student> out=new ArrayList<>();
        for (int i=0; i<size; i++){
            out.add(bag.getRandomStudent());
        }
        int fr=0;int g=0; int d=0; int fa=0; int u=0;
        for(int i=0; i<130; i++){
            switch (out.get(i).getType()) {
                case FROG:
                    fr++;
                    break;
                case GNOME:
                    g++;
                    break;
                case DRAGON:
                    d++;
                    break;
                case FAIRIE:
                    fa++;
                    break;
                case UNICORN:
                    u++;
                    break;
            }
        }
        assertEquals(26,fr);
        assertEquals(26,g);
        assertEquals(26,d);
        assertEquals(26,fa);
        assertEquals(26,u);
        try {
            Student stu = bag.getRandomStudent();
        }
        catch(Exception e){}
    }
}