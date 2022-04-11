package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Colors.BLACK;
import static it.polimi.ingsw.model.Colors.GREY;
import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void moveMotherNature() {
        Player pl1,pl2;
        pl1=new Player(1,GREY,8,Wizards.WIZARD1);
        pl2=new Player(2,BLACK,8,Wizards.WIZARD2);
        Match a=new Match(pl1,pl2);
        assertTrue(a.getMothernature().getPosition()==a.getLands().get(0));
        a.moveMotherNature(5);
        assertTrue(a.getMothernature().getPosition()==a.getLands().get(5));
        a.moveMotherNature(10);
        assertTrue(a.getMothernature().getPosition()==a.getLands().get(3));
    }

    @Test
    void uniteLandAfter() throws Exception {
        Player pl1,pl2;
        pl1=new Player(1,GREY,8,Wizards.WIZARD1);
        pl2=new Player(2,BLACK,8,Wizards.WIZARD2);
        Match a=new Match(pl1,pl2);
        a.getLands().get(2).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(3).changeTower(pl1.getBoard().removeTower());
        a.uniteLandAfter(2);
        assertTrue(a.getLands().size()==11);
        assertTrue(a.getLands().get(2).getTowerColor()== GREY);
        assertTrue(a.getLands().get(1).getID()==1);
        assertTrue(a.getLands().get(3).getID()==4);
        assertTrue(a.getLands().get(2).size()==2);
    }


    @Test
    void uniteLandAfterLast() throws Exception {
        Player pl1,pl2;
        pl1=new Player(1,GREY,8,Wizards.WIZARD1);
        pl2=new Player(2,BLACK,8,Wizards.WIZARD2);
        Match a=new Match(pl1,pl2);
        a.getLands().get(0).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(11).changeTower(pl1.getBoard().removeTower());
        a.uniteLandAfter(11);
        assertTrue(a.getLands().size()==11);
        assertTrue(a.getLands().get(0).getTowerColor()== GREY);
        assertTrue(a.getLands().get(1).getID()==1);
        assertTrue(a.getLands().get(2).getID()==2);
        assertTrue(a.getLands().get(0).size()==2);
    }

    @Test
    void uniteLandBefore() throws Exception {
        Player pl1,pl2;
        pl1=new Player(1,GREY,8,Wizards.WIZARD1);
        pl2=new Player(2,BLACK,8,Wizards.WIZARD2);
        Match a=new Match(pl1,pl2);
        a.getLands().get(2).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(3).changeTower(pl1.getBoard().removeTower());
        a.uniteLandBefore(3);
        assertTrue(a.getLands().size()==11);
        assertTrue(a.getLands().get(2).getTowerColor()== GREY);
        assertTrue(a.getLands().get(1).getID()==1);
        assertTrue(a.getLands().get(3).getID()==4);
        assertTrue(a.getLands().get(2).size()==2);
    }
    @Test
    void uniteLandBeforeFirst() throws Exception {
        Player pl1,pl2;
        pl1=new Player(1,GREY,8,Wizards.WIZARD1);
        pl2=new Player(2,BLACK,8,Wizards.WIZARD2);
        Match a=new Match(pl1,pl2);
        a.getLands().get(0).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(11).changeTower(pl1.getBoard().removeTower());
        a.uniteLandBefore(0);
        assertTrue(a.getLands().size()==11);
        assertTrue(a.getLands().get(0).getTowerColor()== GREY);
        assertTrue(a.getLands().get(1).getID()==1);
        assertTrue(a.getLands().get(2).getID()==2);
        assertTrue(a.getLands().get(0).size()==2);
    }

    @Test
    void uniteLandBeforeAndAfter() throws Exception{
        Player pl1,pl2;
        pl1=new Player(1,GREY,8,Wizards.WIZARD1);
        pl2=new Player(2,BLACK,8,Wizards.WIZARD2);
        Match a=new Match(pl1,pl2);
        a.getLands().get(2).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(3).changeTower(pl1.getBoard().removeTower());
        a.getLands().get(4).changeTower(pl1.getBoard().removeTower());
        a.uniteLandBeforeAndAfter(3);
        assertTrue(a.getLands().size()==10);
        assertTrue(a.getLands().get(2).getTowerColor()== GREY);
        assertTrue(a.getLands().get(1).getID()==1);
        assertTrue(a.getLands().get(3).getID()==5);
        assertTrue(a.getLands().get(2).size()==3);
    }

    @Test
    void checkProfessor() {
        Player pl1,pl2;
        pl1=new Player(1,GREY,8,Wizards.WIZARD1);
        pl2=new Player(2,BLACK,8,Wizards.WIZARD2);
        Match a=new Match(pl1,pl2);
        Student drago=new Student(Type_Student.DRAGON);
        Student drago2=new Student(Type_Student.DRAGON);
        Student drago3=new Student(Type_Student.DRAGON);
        a.getPlayer()[1].getBoard().addStudent(drago);
        a.getPlayer()[0].getBoard().addStudent(drago2);
        a.getPlayer()[1].getBoard().addStudent(drago3);
        assertTrue(a.checkProfessor(Type_Student.DRAGON)==pl2);
        assertTrue(a.getProfessors().get(Type_Student.DRAGON)==pl2);
        Student drago4=new Student(Type_Student.DRAGON);
        Student drago5=new Student(Type_Student.DRAGON);
        a.getPlayer()[0].getBoard().addStudent(drago4);
        a.getPlayer()[0].getBoard().addStudent(drago5);
        assertTrue(a.checkProfessor(Type_Student.DRAGON)==pl1);
        assertTrue(a.getProfessors().get(Type_Student.DRAGON)==pl1);
    }
}