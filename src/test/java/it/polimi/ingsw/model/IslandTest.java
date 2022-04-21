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

    @Test //
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
        Island d=new Island(12345);
        try {
            d.getTowerColor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Colors c=Colors.BLACK;
        ArrayList<Student>s=new ArrayList<>();
        Student gg= new Student(Type_Student.GNOME);
        s.add(gg);
        Board b= new Board(2,c, s);
        Tower t=new Tower(c,b);
        ArrayList<Tower> old=new ArrayList<>();
        old.add(t);
        ArrayList<Tower> h=b.getTowers();
        assertNull(island.getTower());
        island.changeTower(t);
        assertEquals(h,island.getTower().getBoard().getTowers()); //dovrebbe essere uguale perchè null
        assertEquals(t,island.getTower());
        try {
            assertEquals(t.getColor(),island.getTowerColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(b,island.getTower().getBoard());
        assertEquals(old,island.getAllTowers());
        Colors colo=Colors.GREY;
        Board boa= new Board(2,colo,s);
        Tower tow=new Tower(colo,boa);
        island.changeTower(tow);
        assertEquals(3,b.getTowers().size());
        assertEquals(tow,island.getTower());
        assertEquals(boa,island.getTower().getBoard());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(island.isThereNoEntry());
        try {
            island.setNoEntry(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertFalse(island.isThereNoEntry());
        try {
            island.setNoEntry(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unionTest(){
        Colors c=Colors.BLACK;
        ArrayList<Student>s=new ArrayList<>();
        Student gg= new Student(Type_Student.GNOME);
        s.add(gg);
        Board b= new Board(3,c,s );
        Tower t=new Tower(c,b);
        ArrayList<Land>lands=new ArrayList<>();
        Island isa=new Island(9999);
        Board bruh=new Board(3,Colors.GREY, s);
        Tower tow=new Tower(Colors.GREY,bruh);
        isa.changeTower(tow);
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
        assertEquals(t,is.get(0).getTower());
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
        try {
            island.uniteIslands(isa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

