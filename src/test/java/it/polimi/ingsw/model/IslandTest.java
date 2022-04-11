package it.polimi.ingsw.model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class IslandTest {

    Island island;
    int id;

    @BeforeEach
    void setUp() {
        //Bag bag=new Bag();
        id=0;
        island=new Island(id);
        /*for(int i=0; i<10; i++){
            island.addStudent(bag.getRandomStudent());
        }*/
    }

    @Test
    void initializationTest(){
        assertEquals(island.getID(),id);
        assertEquals(island.getTower(),null);
        assertFalse(island.isThereNoEntry());
        assertEquals(island.getStudents(),null);
    }

    @Test
    void studentsTest(){
        ArrayList<Student> stu=new ArrayList<>();
        Bag bag=new Bag();
        for(int i=0; i<10; i++){
            stu.add(bag.getRandomStudent());
        }
        for(Student s : stu){
            island.addStudent(s);
        }
        assertEquals(island.getStudents(),stu);
    }

    @Test
    void towersTest(){
        Colors c=Colors.BLACK;
        Board b=new Board(8,Colors.GREY);
        Tower t=new Tower(c,b);
        ArrayList<Tower> old=new ArrayList<>();
        old.add(t);
        assertEquals(island.changeTower(t),null);
        assertEquals(island.getTower(),t);
        assertEquals(island.getAllTowers(),old);
    }
}