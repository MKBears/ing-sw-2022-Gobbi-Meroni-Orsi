package it.polimi.ingsw.model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArchipelagoTest {  //fai initialization test

    Colors color=Colors.GRAY;
    Board board=new Board(20, color);
    Tower t=new Tower(color, board);
    static ArrayList<Island> is= new ArrayList<>();

    @BeforeAll
    static void setUp() {
        for (int i = 0; i < 20; i++) {
            Island island = new Island(i);
            is.add(island);
        }
    }
    Archipelago pelago= new Archipelago(is);


    @Test
    public void towersTest(){
        assertEquals(t,pelago.getTower());
        Colors c=Colors.BLACK;
        Board b=new Board(20,c);
        Tower to=new Tower(c,board);
        pelago.changeTower(to);
        assertFalse(board.hasNoTowersLeft());
        assertEquals(40,board.getTowers());
        assertEquals(to,pelago.getTower());
        assertEquals(20,pelago.getAllTowers().size());
    }

    @Test
    public void studentsTest(){
        ArrayList<Student>studenti=new ArrayList<>();
        Type_Student type=Type_Student.DRAGON;
        Student s=new Student(type);
        for(int i=0; i<20; i++){
            studenti.add(s);
        }

    }
}