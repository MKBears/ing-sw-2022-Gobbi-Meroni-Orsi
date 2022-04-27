package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.model.Colors.*;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {
    private final String u1 = "Pina", u2 = "Gino";
    @Test
    void checkInit(){
        Player pl1,pl2;
        pl1=new Player(u1, Colors.GREY,8,Wizards.WIZARD1, false);
        pl2=new Player(u2, Colors.BLACK,8,Wizards.WIZARD2, false);
        Match a=new Match(pl1,pl2);
        for (int i=0;i<12;i++){
            if(i==0 || i==6)
                assertTrue(a.getLands().get(i).getStudents().size()==0);
            else
                assertTrue(a.getLands().get(i).getStudents().size()==1);
        }
    }
    @Test
    void moveMotherNature() {
        Player pl1,pl2;
        pl1=new Player(u1, Colors.GREY,8,Wizards.WIZARD1, false);
        pl2=new Player(u2, Colors.BLACK,8,Wizards.WIZARD2, false);
        Match a=new Match(pl1,pl2);
        assertSame(a.getLands().get(0), a.getMotherNature().getPosition());
        a.moveMotherNature(5);
        assertSame(a.getLands().get(5), a.getMotherNature().getPosition());
        a.moveMotherNature(10);
        assertSame(a.getLands().get(3), a.getMotherNature().getPosition());
    }

    @Test
    void uniteLandAfter() throws Exception {
        Player pl1,pl2;
        pl1=new Player(u1,GREY,8,Wizards.WIZARD1, false);
        pl2=new Player(u2,BLACK,8,Wizards.WIZARD2, false);
        Match a=new Match(pl1,pl2);
        a.getLands().get(2).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(3).changeTower(pl1.getBoard().removeTower());
        a.uniteLandAfter(2);
        assertEquals(11, a.getLands().size());
        assertSame(GREY, a.getLands().get(2).getTowerColor());
        assertEquals(1, a.getLands().get(1).getID());
        assertEquals(4, a.getLands().get(3).getID());
        assertEquals(2, a.getLands().get(2).size());
    }


    @Test
    void uniteLandAfterLast() throws Exception {
        Player pl1,pl2;
        pl1=new Player(u1,GREY,8,Wizards.WIZARD1, false);
        pl2=new Player(u2,BLACK,8,Wizards.WIZARD2, false);
        Match a=new Match(pl1,pl2);
        a.getLands().get(0).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(11).changeTower(pl1.getBoard().removeTower());
        a.uniteLandAfter(11);
        assertEquals(11, a.getLands().size());
        assertSame(GREY, a.getLands().get(0).getTowerColor());
        assertEquals(1, a.getLands().get(1).getID());
        assertEquals(2, a.getLands().get(2).getID());
        assertEquals(2, a.getLands().get(0).size());
    }

    @Test
    void uniteLandBefore() throws Exception {
        Player pl1,pl2,pl3;
        pl1=new Player(u1,GREY,6,Wizards.WIZARD1, false);
        pl2=new Player(u2,BLACK,6,Wizards.WIZARD2, false);
        pl3=new Player("Franco",WHITE,6,Wizards.WIZARD3, false);
        Match a=new Match(pl1,pl2,pl3);
        a.getLands().get(2).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(3).changeTower(pl1.getBoard().removeTower());
        a.uniteLandBefore(3);
        assertEquals(11, a.getLands().size());
        assertSame(GREY, a.getLands().get(2).getTowerColor());
        assertEquals(1, a.getLands().get(1).getID());
        assertEquals(4, a.getLands().get(3).getID());
        assertEquals(2, a.getLands().get(2).size());
    }
    @Test
    void uniteLandBeforeFirst() throws Exception {
        Player pl1,pl2;
        pl1=new Player(u1,GREY,8,Wizards.WIZARD1, false);
        pl2=new Player(u2,BLACK,8,Wizards.WIZARD2, false);
        Match a=new Match(pl1,pl2);
        a.getLands().get(0).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(11).changeTower(pl1.getBoard().removeTower());
        a.uniteLandBefore(0);
        assertEquals(11, a.getLands().size());
        assertSame(GREY, a.getLands().get(0).getTowerColor());
        assertEquals(1, a.getLands().get(1).getID());
        assertEquals(2, a.getLands().get(2).getID());
        assertEquals(2, a.getLands().get(0).size());
    }

    @Test
    void uniteLandBeforeAndAfter() throws Exception{
        Player pl1,pl2;
        pl1=new Player(u1,GREY,8,Wizards.WIZARD1, false);
        pl2=new Player(u2,BLACK,8,Wizards.WIZARD2, false);
        Match a=new Match(pl1,pl2);
        a.getLands().get(2).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(3).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(4).changeTower(pl1.getBoard().removeTower());
        a.uniteLandBeforeAndAfter(3);
        assertEquals(10, a.getLands().size());
        assertSame(GREY, a.getLands().get(2).getTowerColor());
        assertEquals(1, a.getLands().get(1).getID());
        assertEquals(5, a.getLands().get(3).getID());
        assertEquals(3, a.getLands().get(2).size());
    }

    @Test
    void checkProfessor() {
        Player pl1,pl2;
        ArrayList<Student> entrance = new ArrayList<>();
        pl1=new Player(u1,GREY,8,Wizards.WIZARD1, false);
        pl2=new Player(u2,BLACK,8,Wizards.WIZARD2, false);
        try {
            pl1.getBoard().setEntrance(entrance);
            pl2.getBoard().setEntrance(entrance);
        }catch (Exception e){
            fail();
        }
        Match a=new Match(pl1,pl2);
        Student drago=new Student(Type_Student.DRAGON);
        Student drago2=new Student(Type_Student.DRAGON);
        Student drago3=new Student(Type_Student.DRAGON);
        assertDoesNotThrow(()->a.getPlayer()[1].getBoard().placeStudent(drago));
        assertDoesNotThrow(()->a.getPlayer()[0].getBoard().placeStudent(drago2));
        assertDoesNotThrow(()->a.getPlayer()[1].getBoard().placeStudent(drago3));
        assertSame(a.checkProfessor(Type_Student.DRAGON), pl2);
        assertSame(a.getProfessors().get(Type_Student.DRAGON), pl2);
        Student drago4=new Student(Type_Student.DRAGON);
        Student drago5=new Student(Type_Student.DRAGON);
        assertDoesNotThrow(()->a.getPlayer()[0].getBoard().placeStudent(drago4));
        assertDoesNotThrow(()->a.getPlayer()[0].getBoard().placeStudent(drago5));
        assertSame(pl1, a.checkProfessor(Type_Student.DRAGON));
        assertSame(pl1, a.getProfessors().get(Type_Student.DRAGON));
    }
}