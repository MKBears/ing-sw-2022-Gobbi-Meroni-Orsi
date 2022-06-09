package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardExpertsTest {
    private int towersNum;
    private Colors color;
    private Board_Experts board;
    private ArrayList<Student> entrance;

    @BeforeEach
    public void instantiate(){
        towersNum = 6;
        color = Colors.WHITE;
        entrance = new ArrayList<>();

        for (Type_Student t : Type_Student.values()){
            entrance.add(new Student(t));
        }
        board = new Board_Experts(towersNum, color);
    }

    @Test
    public void instanceTest(){
        for (Type_Student t : Type_Student.values()) {
            assertSame(0, board.getStudentsOfType(t));
        }
        assertSame(1, board.getCoinsNumber());
        assertSame(towersNum, board.getTowersNum());
        assertFalse(board.hasNoTowersLeft());
        assertThrows(NullPointerException.class, ()->board.getEntrance());
    }

    @Test
    public void entranceTest(){
        ArrayList<Student> students = new ArrayList<>();

        for (Type_Student t : Type_Student.values()){
            students.add(new Student(t));
        }
        try {
            board.setEntrance(students);
        } catch (Exception e) {
            fail();
        }
        assertThrows(Exception.class, ()->board.setEntrance(students));

        for (Student s : students){
            assertTrue(board.getEntrance().contains(s));
            try {
                board.placeStudent(s);
            }catch (Exception e){
                fail();
            }
        }

        try{
            board.importStudents(entrance);
        }catch (Exception e){
            fail();
        }

        for (Student s : entrance){
            board.removeStudent(s);
            assertFalse(board.getEntrance().contains(s));
        }
        assertTrue(board.getEntrance().isEmpty());

        for (Type_Student t : Type_Student.values()){
            assertSame(1, board.getStudentsOfType(t));
        }
        assertSame(1, board.getCoinsNumber());
    }

    @Test
    public void tablesTest(){
        ArrayList<Student> students = new ArrayList<>();
        Student student;
        final int dragons = 10;
        Player pl1=new Player("ale",Colors.GREY,8,Wizards.WIZARD1,true);
        Player pl2=new Player("ale",Colors.BLACK,8,Wizards.WIZARD2,true);
        Match match=new Expert_Match(pl1,pl2);
        CharacterCard c = new Ch_2(match);

        try {
            board.setEntrance(new ArrayList<>());
        } catch (Exception e) {
            fail();
        }

        for(int i=0; i<dragons; i++){
            students.add(new Student(Type_Student.DRAGON));
        }

        board.importStudents(students);

        for(Student s : students) {
            try {
                board.placeStudent(s);
            } catch (Exception e) {
                fail();
            }
        }
        assertSame(4, board.getCoinsNumber());
        assertSame(dragons, board.getStudentsOfType(Type_Student.DRAGON));

        student = new Student(Type_Student.DRAGON);
        assertThrows(Exception.class, ()->board.placeStudent(student));

        assertThrows(NullPointerException.class, ()->board.playCharacter(c));
        assertTrue(c.hasBeenActivated());
        assertSame(2, board.getCoinsNumber());
        assertThrows(Exception.class, ()->board.playCharacter(new Ch_9()));
    }

    @Test
    public void towersTest(){
        Tower temp;

        for (int i=0; i<towersNum; i++){
            try {
                temp = board.removeTower();
                assertSame(color, temp.getColor());
                assertEquals(board, temp.getBoard());
                assertFalse(board.getTowers().contains(temp));
            }catch(Exception e){
                fail();
            }
        }
        assertThrows(Exception.class, ()-> board.removeTower());
        assertTrue(board.hasNoTowersLeft());

        temp = new Tower(color, board);
        board.returnTower(temp);
        assertFalse(board.hasNoTowersLeft());
        assertSame(1, board.getTowersNum());
        assertTrue(board.getTowers().contains(temp));
    }

}