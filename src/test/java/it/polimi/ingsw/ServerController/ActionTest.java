package it.polimi.ingsw.ServerController;

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
            assertTrue(match.getLands().indexOf(match.getMotherNature().getPosition())==0);
        }
        try{
            a.cardAndMoveMN(card,3);
        }catch(IllegalArgumentException e){
            System.out.println("error");
        }finally {
            assertTrue(match.getLands().indexOf(match.getMotherNature().getPosition())==3);
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
        match.getPlayer()[0].getBoard().importStudents(array);
        match.getPlayer()[0].getBoard().placeStudent(student);
        student=new Student(Type_Student.FAIRIE);
        array.remove(0);
        array.add(student);
        match.getPlayer()[1].getBoard().importStudents(array);
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
        assertTrue(match.getProfessors().get(Type_Student.DRAGON)==match.getPlayer()[0]);
        assertTrue(match.getProfessors().get(Type_Student.FAIRIE)==match.getPlayer()[1]);
        assertTrue(match.getProfessors().get(Type_Student.FROG)==match.getPlayer()[0]);
        assertTrue(match.getProfessors().get(Type_Student.GNOME)==match.getPlayer()[1]);
        assertTrue(match.getProfessors().get(Type_Student.UNICORN)==match.getPlayer()[1]);
    }

    @Test
    void controlLand() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        ArrayList<Student> array=new ArrayList<>();
        Student student= new Student(Type_Student.DRAGON);
        array.add(student);
        match.getPlayer()[0].getBoard().importStudents(array);
        match.getPlayer()[0].getBoard().placeStudent(student);
        Student s1=new Student(Type_Student.DRAGON);
        match.getMotherNature().getPosition().addStudent(s1);
        a.controlLand(pl1);
        assertTrue(match.getLands().get(0).getTower().getColor()==Colors.BLACK);
        assertTrue(pl1.getBoard().getTowersNum()==7);
        student= new Student(Type_Student.DRAGON);
        array.add(student);
        match.getPlayer()[1].getBoard().importStudents(array);
        match.getPlayer()[1].getBoard().placeStudent(student);
        match.getPlayer()[1].getBoard().placeStudent(student);
        a.controlLand(pl2);
        assertTrue(match.getLands().get(0).getTower().getColor()==Colors.WHITE);
        assertTrue(pl2.getBoard().getTowersNum()==7);
        assertTrue(pl1.getBoard().getTowersNum()==8);
    }

    @Test
    void uniteLands() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        match.getLands().get(11).changeTower(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(0).changeTower(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(1).changeTower(match.getPlayer()[0].getBoard().removeTower());
        a.uniteLands();
        assertTrue(match.getLands().size()==10);
        assertTrue(match.getLands().get(0).getTower().getColor()==Colors.BLACK);
    }

    @Test
    void uniteLands1() throws Exception {
        Player pl1=new Player("franco", Colors.BLACK,8, Wizards.WIZARD1,FALSE);
        Player pl2=new Player("alberto", Colors.WHITE,8, Wizards.WIZARD2,FALSE);
        Match match=new Match(pl1,pl2);
        Action a=new Action(match);
        match.getLands().get(10).changeTower(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(11).changeTower(match.getPlayer()[0].getBoard().removeTower());
        match.getLands().get(0).changeTower(match.getPlayer()[0].getBoard().removeTower());
        match.getMotherNature().setPosition(match.getLands().get(11));
        a.uniteLands();
        assertTrue(match.getLands().size()==10);
        assertTrue(match.getLands().get(0).getTower().getColor()==Colors.BLACK);
        assertTrue(match.getMotherNature().getPosition()==match.getLands().get(0));
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
        match.getPlayer()[0].getBoard().setEntrance(array);
        a.moveFromIngressToLand(match.getPlayer()[0],match.getPlayer()[0].getBoard().getEntrance().get(0),match.getLands().get(0));
        assertTrue(pl1.getBoard().getEntrance().isEmpty()==TRUE);
        assertTrue(match.getLands().get(0).getStudents().get(0)==student);
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
        match.getPlayer()[0].getBoard().setEntrance(array);
        a.moveFromIngressToBoard(match.getPlayer()[0],match.getPlayer()[0].getBoard().getEntrance().get(0));
        assertTrue(pl1.getBoard().getEntrance().isEmpty()==TRUE);
        assertTrue(pl1.getBoard().getStudentsOfType(Type_Student.DRAGON)==1);
    }
}