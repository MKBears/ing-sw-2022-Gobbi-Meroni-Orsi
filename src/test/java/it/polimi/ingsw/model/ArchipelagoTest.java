package it.polimi.ingsw.model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArchipelagoTest {

    public static Colors color=Colors.GRAY;
    public static Board board=new Board(20, color);
    public static Tower t=new Tower(color, board);
    public static ArrayList<Island> is= new ArrayList<>();
    public static Type_Student type=Type_Student.DRAGON;
    public static Student s=new Student(type);

    @BeforeAll
    public static void setUp() {
        for (int i = 0; i < 20; i++) {
            Island island = new Island(i);
            for(int j=0; j<20; j++){
                island.addStudent(s);
            }
            is.add(island);
        }
    }
    public static Archipelago pelago= new Archipelago(is);

    @Test
    public void initializationTest(){
        assertEquals(is,pelago.getIslands());
        assertEquals(is.size(),pelago.size());
        assertEquals(is.get(0),pelago.getHead());
        assertEquals(is.get(0).getTower().getColor(),pelago.getTowerColor());
        assertEquals(is.get(0).getInfluence(Type_Student.DRAGON),pelago.getInfluence(Type_Student.DRAGON));
    }
    @Test
    public void towersTest(){
        assertEquals(t,pelago.getTower());
        Colors c=Colors.BLACK;
        //Board b=new Board(20,c);
        Tower to=new Tower(c,board);
        pelago.changeTower(to);
        assertFalse(board.hasNoTowersLeft());
        assertEquals(40,board.getTowers());
        assertEquals(to,pelago.getTower());
        assertEquals(20,pelago.getAllTowers().size());
    }

    @Test
    public void studentsTest(){
        ArrayList<Student>stu=new ArrayList<>();
        for(Island i:is){
            stu.addAll(i.getStudents());
        }
        assertEquals(stu,pelago.getStudents());
        Student s=new Student(Type_Student.FROG);
        pelago.addStudent(s);
        assertTrue(pelago.getStudents().contains(s));
        int in=0;
        for(Island i: is){
            in=in+i.getInfluence(Type_Student.DRAGON);
        }
        assertEquals(in,pelago.getInfluence(Type_Student.DRAGON));
    }
}