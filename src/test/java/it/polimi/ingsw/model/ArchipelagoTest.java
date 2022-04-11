package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArchipelagoTest {

    public static Colors color;
    public static Board board;
    public static Tower t;
    public static ArrayList<Island> is;
    public static Type_Student type;
    public static Student s;
    public static Archipelago pelago;
    public static int c_torri;

    @BeforeAll
    public static void setUp() {
        c_torri=0;
        color=Colors.GRAY;
        board=new Board(20, color);
        t=new Tower(color, board);
        is= new ArrayList<>();
        type=Type_Student.DRAGON;
        s=new Student(type);
        for (int i = 0; i < 20; i++) {
            Island island = new Island(i);
            for(int j=0; j<20; j++){
                island.changeTower(t);
                c_torri++;
                island.addStudent(s);
            }
            is.add(island);
        }
        pelago=new Archipelago(is);
    }

    @Test
    public void initializationTest(){
        assertEquals(is,pelago.getIslands());
        assertEquals(is.size(),pelago.size());
        assertEquals(is.get(0),pelago.getHead());
        assertEquals(is.get(0).getTower().getColor(),pelago.getTowerColor());
        int h=0;
        for(Island i: is){
            h=h+i.getInfluence(Type_Student.DRAGON);
        }
        assertEquals(h,pelago.getInfluence(Type_Student.DRAGON));
        assertEquals(is.get(0).getID(),pelago.getID());
    }

    @Test
    public void towersTest(){
        assertEquals(t,pelago.getTower());
        Colors c=Colors.BLACK;
        //Board b=new Board(20,c);
        Tower to=new Tower(c,board);
        pelago.changeTower(to);
        c_torri=c_torri+20;
        assertFalse(board.hasNoTowersLeft());
        assertEquals((int)c_torri,(int)board.getTowers());
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

    @Test
    public void entryTest(){
        assertFalse(pelago.isThereNoEntry());
        try {
            pelago.setNoEntry(true);
        } catch (DuplicateValueException e) {
            e.printStackTrace();
        }
        assertTrue(pelago.isThereNoEntry());
        try {
            pelago.setNoEntry(false);
        } catch (DuplicateValueException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unionTest(){

    }
}