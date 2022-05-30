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
        assertFalse(island.hasChanged());
    }

    @Test //
    public void studentsTest(){
        ArrayList<Student> stu=new ArrayList<>();
        Bag bag=new Bag();
        for(int i=0; i<10; i++){
            try {
                stu.add(bag.getRandomStudent());
            } catch (Exception e) {
                fail();
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
        assertThrows(Exception.class,()->d.getTowerColor());
        Colors c=Colors.BLACK;
        Board b= new Board(2,c);
        Tower t=new Tower(c,b);
        ArrayList<Tower> old=new ArrayList<>();
        old.add(t);
        ArrayList<Tower> h=b.getTowers();
        assertNull(island.getTower());
        ArrayList<Tower> jj=new ArrayList<>();
        jj.add(t);
        island.changeTower(jj);
        assertEquals(h,island.getTower().getBoard().getTowers()); //dovrebbe essere uguale perchè null
        assertEquals(t,island.getTower());
        try {
            assertEquals(t.getColor(),island.getTowerColor());
        } catch (Exception e) {
            fail();
        }
        assertEquals(b,island.getTower().getBoard());
        assertEquals(old,island.getAllTowers());
        Colors colo=Colors.GREY;
        Board boa= new Board(2,colo);
        Tower tow=new Tower(colo,boa);
        ArrayList<Tower> rr=new ArrayList<>();
        rr.add(tow);
        island.changeTower(rr);
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
                fail();
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
        ArrayList<Type_Student> a=new ArrayList<>();
        a.add(Type_Student.FROG);
        assertEquals(fr,island.getInfluence(a));
        a.remove(0);
        a.add(Type_Student.GNOME);
        assertEquals(g,island.getInfluence(a));
        a.remove(0);
        a.add(Type_Student.DRAGON);
        assertEquals(d,island.getInfluence(a));
        a.remove(0);
        a.add(Type_Student.FAIRIE);
        assertEquals(fa,island.getInfluence(a));
        a.remove(0);
        a.add(Type_Student.UNICORN);
        assertEquals(u,island.getInfluence(a));
    }

    @Test
    public void entryTest(){
        assertFalse(island.isThereNoEntry());
        try {
            island.setNoEntry(true);
        } catch (Exception e) {
            fail();
        }
        assertTrue(island.isThereNoEntry());
        try {
            island.setNoEntry(false);
        } catch (Exception e) {
            fail();
        }
        assertFalse(island.isThereNoEntry());
        assertThrows(Exception.class,()->island.setNoEntry(false));
    }

    @Test
    public void unionTest(){
        Colors c=Colors.BLACK;
        Board b= new Board(3,c);
        Tower t=new Tower(c,b);
        ArrayList<Land>lands=new ArrayList<>();
        Island isa=new Island(9999);
        Board bruh=new Board(3,Colors.GREY);
        Tower tow=new Tower(Colors.GREY,bruh);
        ArrayList<Tower> bella=new ArrayList<>();
        bella.add(tow);
        isa.changeTower(bella);
        ArrayList<Tower> gg=new ArrayList<>();
        gg.add(t);
        for(int h=2; h<7; h++) {
            Island i = new Island(h);
            i.changeTower(gg);
            if(h>3)
            {
                ArrayList<Island>isles=new ArrayList<>();
                for(int z=h+3; z<(h+22);z++){
                    Island isl=new Island(z);
                    isl.changeTower(gg);
                    isles.add(isl);
                }
                Archipelago arch=new Archipelago(isles);
                lands.add(arch);
            }
            lands.add(i);
        }

        ArrayList<Island>is=new ArrayList<>();
        island.changeTower(gg);
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
                fail();
            }
        }
        assertThrows(Exception.class,()->island.uniteIslands(isa));
    }

    @Test
    public void toStringTest(){
        assertThrows(Exception.class,()->island.getPreviousTowers());
        String s="isola "+island.getID()+" con studenti: "+island.getStudents()+" non ha torri";
        Island f=new Island(88);
        Board on=new Board(6,Colors.WHITE);
        Tower bu= new Tower(Colors.WHITE, on);
        ArrayList<Tower> gio=new ArrayList<>();
        gio.add(bu);
        f.changeTower(gio);
        try {
            f.setNoEntry(true);
        } catch (Exception e) {
            fail();
        }
        String rno="isola "+f.getID()+" con studenti: [] e torre di colore Bianco  entrata chiusa";
        assertEquals(rno, f.toString());
        assertEquals(s,island.toString());
        assertFalse(island.hasChanged());
        Board bo=new Board(55, Colors.BLACK);
        Tower j=new Tower(Colors.BLACK, bo);
        ArrayList<Tower> ho=new ArrayList<>();
        ho.add(j);
        island.changeTower(ho);
        Board fa=new Board(55, Colors.GREY);
        Tower me=new Tower(Colors.GREY, fa);
        ho.clear();
        ho.add(me);
        island.changeTower(ho);
        assertTrue(island.hasChanged());
        ho.clear();
        ho.add(j);
        island.changeTower(ho);
        assertEquals(56,bo.getTowersNum()); //QUI FUNZIONA MALEDETTO
        ho.clear();
        ho.add(me);
        try {  //LANCIA L'ECCEZIONE
            assertEquals(ho, island.getPreviousTowers());
        }catch (Exception e){
            fail();
        }
    }
}

