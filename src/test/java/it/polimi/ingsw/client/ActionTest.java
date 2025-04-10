package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.lang.Boolean.*;
import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    @Test
     void cardAndMoveMN() {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        AssistantCard card=new AssistantCard(3,3);
        try{
            a.cardAndMoveMN(card,4);
        }catch(IllegalArgumentException e){
            assertEquals(0, match.getLands().indexOf(match.getMotherNature().getPosition()));
        }
        try{
            a.cardAndMoveMN(card,3);
        }catch(IllegalArgumentException e){
            System.out.println("error");
        }finally {
            assertEquals(3, match.getLands().indexOf(match.getMotherNature().getPosition()));
        }

    }

    @Test
    void checkAllProfessors() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Student> array=new ArrayList<>();
        Student student= new Student(Type_Student.DRAGON);
        array.add(student);
        match.getPlayer()[0].getBoard().setEntrance(array);
        match.getPlayer()[0].getBoard().placeStudent(student);
        student=new Student(Type_Student.FAIRY);
        array.remove(0);
        array.add(student);
        match.getPlayer()[1].getBoard().setEntrance(array);
        match.getPlayer()[1].getBoard().placeStudent(student);
        student= new Student(Type_Student.FROG);
        array.remove(0);
        array.add(student);
        match.getPlayer()[0].getBoard().importStudents(array);
        match.getPlayer()[0].getBoard().placeStudent(student);
        student=new Student(Type_Student.GNOME);
        array.remove(0);
        array.add(student);
        match.getPlayer()[1].getBoard().importStudents(array);
        match.getPlayer()[1].getBoard().placeStudent(student);
        student=new Student(Type_Student.UNICORN);
        array.remove(0);
        array.add(student);
        match.getPlayer()[1].getBoard().importStudents(array);
        match.getPlayer()[1].getBoard().placeStudent(student);
        a.checkAllProfessors();
        assertSame(match.getProfessors().get(Type_Student.DRAGON), match.getPlayer()[0]);
        assertSame(match.getProfessors().get(Type_Student.FAIRY), match.getPlayer()[1]);
        assertSame(match.getProfessors().get(Type_Student.FROG), match.getPlayer()[0]);
        assertSame(match.getProfessors().get(Type_Student.GNOME), match.getPlayer()[1]);
        assertSame(match.getProfessors().get(Type_Student.UNICORN), match.getPlayer()[1]);
    }


    @Test
    void uniteLands() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Tower> t=new ArrayList<>();
        t.add(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(11).changeTower(t);
        match.getLands().get(0).changeTower(t);
        match.getLands().get(1).changeTower(t);
        a.uniteLands();
        assertEquals(10, match.getLands().size());
        assertSame(match.getLands().get(0).getTower().getColor(), Colors.BLACK);
    }

    @Test
    void uniteLands1() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Tower> t=new ArrayList<>();
        t.add(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(10).changeTower(t);
        match.getLands().get(11).changeTower(t);
        match.getLands().get(0).changeTower(t);
        match.getMotherNature().setPosition(match.getLands().get(11));
        a.uniteLands();
        assertEquals(10, match.getLands().size());
        assertSame(match.getLands().get(0).getTower().getColor(), Colors.BLACK);
        assertSame(match.getMotherNature().getPosition(), match.getLands().get(0));
    }

    @Test
    void uniteLands2() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Tower> t=new ArrayList<>();
        t.add(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(10).changeTower(t);
        match.getLands().get(0).changeTower(t);
        t.remove(0);
        t.add(pl2.getBoard().removeTower());
        match.getLands().get(11).changeTower(t);
        match.getMotherNature().setPosition(match.getLands().get(11));
        a.uniteLands();
        assertEquals(12, match.getLands().size());
        assertSame(match.getLands().get(0).getTower().getColor(), Colors.BLACK);
        assertSame(match.getMotherNature().getPosition(), match.getLands().get(11));
    }

    @Test
    void uniteLands3() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Tower> t=new ArrayList<>();
        t.add(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(5).changeTower(t);
        match.getLands().get(6).changeTower(t);
        t.remove(0);
        match.getMotherNature().setPosition(match.getLands().get(5));
        a.uniteLands();
        assertEquals(11, match.getLands().size());
        assertSame(match.getLands().get(5).getTower().getColor(), Colors.BLACK);
        assertSame(match.getMotherNature().getPosition(), match.getLands().get(5));
    }

    @Test
    void uniteLands4() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Tower> t=new ArrayList<>();
        t.add(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(5).changeTower(t);
        match.getLands().get(6).changeTower(t);
        t.remove(0);
        match.getMotherNature().setPosition(match.getLands().get(6));
        a.uniteLands();
        assertEquals(11, match.getLands().size());
        assertSame(match.getLands().get(5).getTower().getColor(), Colors.BLACK);
        assertSame(match.getMotherNature().getPosition(), match.getLands().get(5));
    }


    @Test
    void moveFromIngressToLand() {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Student> array=new ArrayList<>();
        Student student= new Student(Type_Student.DRAGON);
        array.add(student);

        try {
            match.getPlayer()[0].getBoard().setEntrance(array);
        } catch (Exception e) {
            fail();
        }
        a.moveFromIngressToLand(match.getPlayer()[0],match.getPlayer()[0].getBoard().getEntrance().get(0),match.getLands().get(0));
        assertTrue(pl1.getBoard().getEntrance().isEmpty());
        assertSame(match.getLands().get(0).getStudents().get(0), student);
    }

    @Test
    void moveFromIngressToBoard() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Student> array=new ArrayList<>();
        Student student= new Student(Type_Student.DRAGON);
        array.add(student);
        student=new Student(Type_Student.DRAGON);
        array.add(student);
        match.getPlayer()[0].getBoard().setEntrance(array);
        System.out.println(match.getPlayer()[0].getBoard());
        a.moveFromIngressToBoard(match.getPlayer()[0],match.getPlayer()[0].getBoard().getEntrance().get(0));
        assertFalse(pl1.getBoard().getEntrance().isEmpty());
        assertEquals(1,pl1.getBoard().getEntrance().size());
        assertEquals(1, pl1.getBoard().getStudentsOfType(Type_Student.DRAGON));
        System.out.println(match.getPlayer()[0].getBoard());
    }
}