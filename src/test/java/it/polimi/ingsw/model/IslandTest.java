package it.polimi.ingsw.model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {

    Island island;
    int id;

    @BeforeEach
    public void setUp() {
        //Bag bag=new Bag();
        id=0;
        island=new Island(id);
        /*for(int i=0; i<10; i++){
            island.addStudent(bag.getRandomStudent());
        }*/
    }

    @Test
    public void initializationTest(){
        assertEquals(id,island.getID());
        assertEquals(null,island.getTower());
        assertFalse(island.isThereNoEntry());
        assertTrue(island.getStudents().isEmpty());
        assertEquals(1,island.size());
        assertEquals(island,island.getHead());
    }

    @Test
    public void studentsTest(){
        ArrayList<Student> stu=new ArrayList<>();
        Bag bag=new Bag();
        for(int i=0; i<10; i++){
            try {
                stu.add(bag.getRandomStudent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(Student s : stu){
            island.addStudent(s);
        }
        assertEquals(stu,island.getStudents());
    }

    @Test
    public void towersTest(){
        Colors c=Colors.BLACK;
        Board b= new Board(2,c);
        Tower t=new Tower(c,b);
        ArrayList<Tower> old=new ArrayList<>();
        old.add(t);
        assertEquals(null,island.changeTower(t));
        assertEquals(t,island.getTower());
        assertEquals(old,island.getAllTowers());
    }

    @Test
    public void influenceTest(){
        ArrayList<Student> stu=new ArrayList<>();
        int d=0;
        int fa=0;
        int fr=0;
        int g=0;
        int u=0;
        Bag bag=new Bag();
        for(int i=0; i<20; i++){
            Student s= null;
            try {
                s = bag.getRandomStudent();
            } catch (Exception e) {
                e.printStackTrace();
            }
            island.addStudent(s);
            switch (s.getType()){
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
        assertEquals(fr,island.getInfluence(Type_Student.FROG));
        assertEquals(g,island.getInfluence(Type_Student.GNOME));
        assertEquals(d,island.getInfluence(Type_Student.DRAGON));
        assertEquals(fa,island.getInfluence(Type_Student.FAIRIE));
        assertEquals(u,island.getInfluence(Type_Student.UNICORN));
    }

    @Test
    public void entryTest(){
        assertFalse(island.isThereNoEntry());
        try {
            island.setNoEntry(true);
        } catch (DuplicateValueException e) {
            e.printStackTrace();
        }
        assertTrue(island.isThereNoEntry());
        try {
            island.setNoEntry(false);
        } catch (DuplicateValueException e) {
            e.printStackTrace();
        }
        assertFalse(island.isThereNoEntry());
    }

    @Test
    public void unionTest(){
        Colors c=Colors.BLACK;
        Board b= new Board(3,c);
        Tower t=new Tower(c,b);
        Tower s;
        ArrayList<Land>lands=new ArrayList<>();
        for(int h=2; h<7; h++) {
            Island i = new Island(h);
            s = i.changeTower(t);
            if(h>3)
            {
                ArrayList<Island>isles=new ArrayList<>();
                isles.add(i);
                Archipelago arch=new Archipelago(isles);
                lands.add(arch);
            }
            lands.add(i);
        }

        ArrayList<Island>is=new ArrayList<>();
        is.add(island);
        for(Land l: lands){
            is.addAll(l.getIslands());
        }
        Archipelago pelago=new Archipelago(is);
        for(Land i: lands){
            try {
                assertEquals(pelago, island.uniteIslands(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

