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
    public static int e;
    public static Island i;

    @BeforeAll
    public static void setUp() {
        ArrayList<Student>si=new ArrayList<>();
        Student gg= new Student(Type_Student.GNOME);
        si.add(gg);
        i=new Island(8888);
        e=0;
        c_torri=0;
        color=Colors.GREY;
        board=new Board(20, color,si);
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
    public void initializationTest() {
        if (e == 1) {
            is.add(i);
            assertEquals(is, pelago.getIslands());
            assertEquals(is.size(), pelago.size());
            assertEquals(is.get(0), pelago.getHead());
            assertEquals(is.get(0).getTower().getColor(), pelago.getTowerColor());
            int h = 0;
            for (Island i : is) {
                h = h + i.getInfluence(Type_Student.DRAGON);
            }
            assertEquals(h, pelago.getInfluence(Type_Student.DRAGON));
            assertEquals(is.get(0).getID(), pelago.getID());
        } else {
            assertEquals(is, pelago.getIslands());
            assertEquals(is.size(), pelago.size());
            assertEquals(is.get(0), pelago.getHead());
            assertEquals(is.get(0).getTower().getColor(), pelago.getTowerColor());
            int h = 0;
            for (Island i : is) {
                h = h + i.getInfluence(Type_Student.DRAGON);
            }
            assertEquals(h, pelago.getInfluence(Type_Student.DRAGON));
            assertEquals(is.get(0).getID(), pelago.getID());
        }
    }

    @Test
    public void towersTest(){
        int x=20;
        if(e==1){
            c_torri=c_torri+22;
            x++;
        }
        assertEquals(t,pelago.getTower());
        Colors c=Colors.BLACK;
        ArrayList<Student>s=new ArrayList<>();
        Student gg= new Student(Type_Student.GNOME);
        s.add(gg);
        Board b=new Board(20,c,s);
        Tower to=new Tower(c,b);
        pelago.changeTower(to);
        c_torri=c_torri+20; ////////
        assertFalse(board.hasNoTowersLeft());
        assertEquals(c_torri,board.getTowers().size());
        assertEquals(to,pelago.getTower());
        assertEquals(x,pelago.getAllTowers().size());
    }

    @Test
    public void studentsTest(){
        ArrayList<Student>stu=new ArrayList<>();
        for(Island p:is){
            stu.addAll(p.getStudents());
        }
        //if(e==1){
        //    stu.addAll(i.getStudents());
        //}
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(pelago.isThereNoEntry());
        try {
            pelago.setNoEntry(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pelago.setNoEntry(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void unionTest(){
        e=1;
        i.changeTower(t);
        pelago.changeTower(t);
        ArrayList<Student>s=new ArrayList<>();
        Student gg= new Student(Type_Student.GNOME);
        s.add(gg);
        Board bb=new Board(3,Colors.WHITE,s);
        Board h=new Board(3,Colors.WHITE,s);
        Tower tow=new Tower(Colors.WHITE,bb);
        Island isa=new Island(88);
        isa.changeTower(tow);
        ArrayList<Island> k= new ArrayList<>();
        k.add(isa);
        Archipelago a= new Archipelago(k);
        for (int j=0; j<3; j++){
            Student stud=new Student(Type_Student.GNOME);
            i.addStudent(stud);
        }
        try {
            a=pelago.uniteIslands(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tower y=new Tower(Colors.WHITE,h);
        i.changeTower(y);
        try {
            a=pelago.uniteIslands(i);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        i.changeTower(t);
        ArrayList<Island> arr = new ArrayList<>(pelago.getIslands());
        //arr.add(i);
        for (Island isl:a.getIslands()){
            assertTrue(arr.contains(isl));
        }
        assertEquals(arr.size(),a.size());
        assertEquals(pelago.getID(),a.getID());
    }
}