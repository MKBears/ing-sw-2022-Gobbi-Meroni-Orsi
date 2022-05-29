package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ArchipelagoTest {

    public static Colors color;
    public static Board board;
    public static Tower t;
    public static ArrayList<Tower> tow;
    public static ArrayList<Island> is;
    public static Type_Student type;
    public static Student s;
    public static Archipelago pelago;
    public static int c_torri;
    public static int e;
    public static Island i;

    @BeforeAll
    public static void setUp() {
        i=new Island(8888);
        e=0;
        c_torri=0;
        color=Colors.GREY;
        board=new Board(20, color);
        t=new Tower(color, board);
        tow=new ArrayList<>();
        tow.add(t);
        is= new ArrayList<>();
        type=Type_Student.DRAGON;
        s=new Student(type);
        for (int i = 0; i < 20; i++) {
            Island island = new Island(i);
            island.changeTower(tow);
            c_torri++;
            island.addStudent(s);
            is.add(island);
        }
        pelago=new Archipelago(is);
        assertEquals(pelago.size(),20);
        assertEquals(pelago.getIslands(),is);
    }

    @Test
    public void initializationTest() {
        ArrayList<Type_Student> t=new ArrayList<>();
        t.add(Type_Student.DRAGON);
        if (e == 1) {
            is.add(i);
            assertEquals(is, pelago.getIslands());
            assertEquals(is.size(), pelago.size());
            assertEquals(is.get(0), pelago.getHead());
            assertEquals(is.get(0).getTower().getColor(), pelago.getTowerColor());
            int h = 0;
            for (Island i : is) {
                h = h + i.getInfluence(t);
            }
            assertEquals(h, pelago.getInfluence(t));
            assertEquals(is.get(0).getID(), pelago.getID());
        } else {
            assertEquals(is, pelago.getIslands());
            assertEquals(is.size(), pelago.size());
            assertEquals(is.get(0), pelago.getHead());
            assertEquals(is.get(0).getTower().getColor(), pelago.getTowerColor());
            int h = 0;
            for (Island i : is) {
                h = h + i.getInfluence(t);
            }
            assertEquals(h, pelago.getInfluence(t));
            assertEquals(is.get(0).getID(), pelago.getID());
        }
    }

    @Test
    public void towersTest() throws Exception {
        int x=20;
        c_torri=0;
        if(e==1){
            x++;
        }
        assertEquals(t,pelago.getTower());
        Colors c=Colors.BLACK;
        Board b=new Board(20,c);
        Tower to=new Tower(c,b);
        ArrayList<Tower> tt=new ArrayList<>();
        for(Island i: pelago.getIslands()) {
            tt.add(to);
        }
        c_torri=board.getTowersNum();
        assertEquals(tt.size(),pelago.size());
        pelago.changeTower(tt);
        //assertEquals(pelago.getPreviousTowers().size(),pelago.size());
        c_torri=c_torri+pelago.getPreviousTowers().size();
        assertFalse(board.hasNoTowersLeft());
        assertEquals(c_torri,board.getTowersNum());
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
        ArrayList<Type_Student> t=new ArrayList<>();
        t.add(Type_Student.DRAGON);
        int in=0;
        for(Island i: is){
            in=in+i.getInfluence(t);
        }
        assertEquals(in,pelago.getInfluence(t));
    }

    @Test
    public void entryTest(){
        assertFalse(pelago.isThereNoEntry());
        try {
            pelago.setNoEntry(true);
        } catch (Exception e) {
            fail();
        }
        assertTrue(pelago.isThereNoEntry());
        try {
            pelago.setNoEntry(false);
        } catch (Exception e) {
            fail();
        }
        assertThrows(Exception.class,()->pelago.setNoEntry(false));
    }

    @Test
    public void unionTest() throws Exception {
        e=1;
        i.changeTower(tow);
        for (int j = 0; j < 19; j++) {
            tow.add(t);
        }
        pelago.changeTower(tow);
        Board bb=new Board(3,Colors.WHITE);
        Board h=new Board(3,Colors.WHITE);
        Tower toww=new Tower(Colors.WHITE,bb);
        Island isa=new Island(88);
        ArrayList<Tower> ttt=new ArrayList<>();
        ttt.add(toww);
        isa.changeTower(ttt);
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
            fail();
        }
        Tower y=new Tower(Colors.WHITE,h);
        ArrayList<Tower> ll=new ArrayList<>();
        ll.add(y);
        i.changeTower(ll);
        assertThrows(Exception.class,()->pelago.uniteIslands(i));
        i.changeTower(ttt);
        ArrayList<Island> arr = new ArrayList<>(pelago.getIslands());
        //arr.add(i);
        for (Island isl:a.getIslands()){
            assertTrue(arr.contains(isl));
        }
        assertEquals(arr.size(),a.size());
        assertEquals(pelago.getID(),a.getID());
    }
@Test
    public void toStringTest(){
        ArrayList<Island>isles=new ArrayList<>();
        Tower tw=new Tower(Colors.BLACK,board);
        Island isa= new Island(9999);
        ArrayList<Tower>hehe=new ArrayList<>();
        hehe.add(tw);
        isa.changeTower(hehe);
        Student gobbi=new Student(Type_Student.DRAGON);
        isa.addStudent(gobbi);
        isles.add(isa);
        pelago=new Archipelago(isles);
        String s="Arcipelago di 1 isole{ \nisola 9999 con studenti: ["+gobbi+"] e torre di colore Nero \n}\n";
        assertEquals(pelago.toString(),s);
    }
@Test
    public void hasChangedTest() throws Exception {
        pelago=new Archipelago(is);
        assertEquals(pelago.size(),is.size());
        assertFalse(pelago.hasChanged());
        ArrayList<Tower> k=new ArrayList<>();
        ArrayList<Tower> l=new ArrayList<>();
        Tower cami=tow.get(0);
        while(tow.size()<pelago.size()){
            tow.add(cami);
        }
        for (int i = 0; i < pelago.size(); i++) {
            k.add(tow.get(i));
        }
        l=(ArrayList<Tower>) k.clone();
        pelago.changeTower(k);
        assertTrue(pelago.hasChanged());
        ArrayList<Tower> tt=new ArrayList<>();
        Tower h=new Tower(Colors.BLACK, board);
        for(int i=0; i<pelago.size(); i++){
            tt.add(h);
        }
        pelago.changeTower(tt);
        String s=l.toString();
        assertEquals(pelago.getPreviousTowers().toString(),s);
    }
}