package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BagTest {

    Bag bag=new Bag();

    @Test
    public void getRandomStudentTest() throws Exception {
        ArrayList<Student> arr=new ArrayList<>();
        for (Type_Student t : Type_Student.values()){
            for (int i=0; i<24; i++){
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
        for(int i=0; i<120; i++){
            switch (out.get(i).type()) {
                case FROG:
                    fr++;
                    break;
                case GNOME:
                    g++;
                    break;
                case DRAGON:
                    d++;
                    break;
                case FAIRY:
                    fa++;
                    break;
                case UNICORN:
                    u++;
                    break;
            }
        }
        assertEquals(24,fr);
        assertEquals(24,g);
        assertEquals(24,d);
        assertEquals(24,fa);
        assertEquals(24,u);
        assertThrows(Exception.class,()->bag.getRandomStudent());
    }
}