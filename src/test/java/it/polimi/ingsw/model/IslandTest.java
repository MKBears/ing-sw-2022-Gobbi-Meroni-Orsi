package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {

    int id=0;
    Island island= new Island(id);


    @Test
    public void initializationTest(){
        assertEquals(id,island.getID());
        assertNull(island.getTower());
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
    public void towersTest(){  //da rivedere
        Colors c=Colors.BLACK;
        Board b= new Board(2,c);
        Tower t=new Tower(c,b);
        ArrayList<Tower> old=new ArrayList<>();
        old.add(t);
        assertEquals(t,island.getTower());
        assertEquals(old,island.getAllTowers());
    }

    @Test
    public void influenceTest(){
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
        ArrayList<Land>lands=new ArrayList<>();
        for(int h=2; h<7; h++) {
            Island i = new Island(h);
            i.changeTower(t);
            if(h>3)
            {
                ArrayList<Island>isles=new ArrayList<>();
                for(int z=h+3; z<(h+22);z++){
                    Island isl=new Island(z);
                    isl.changeTower(t);
                    isles.add(isl);
                }
                Archipelago arch=new Archipelago(isles);
                lands.add(arch);
            }
            lands.add(i);
        }

        ArrayList<Island>is=new ArrayList<>();
        island.changeTower(t);
        is.add(island);
        for(Land l: lands){
            is.addAll(l.getIslands());
        }
        assertEquals(t,is.get(0).getTower()); ///
        Archipelago pelago=new Archipelago(is);
        Archipelago out;
        for(Land i: lands){
            try {
                out=island.uniteIslands(i);
                for(int j=0; j<out.size(); j++){
                    assertTrue(pelago.getIslands().containsAll(out.getIslands()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

