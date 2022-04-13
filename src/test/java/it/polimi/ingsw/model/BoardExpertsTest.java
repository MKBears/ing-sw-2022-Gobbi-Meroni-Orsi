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

    @BeforeEach
    public void instantiate(){
        towersNum = 6;
        color = Colors.WHITE;
        board = new Board_Experts(towersNum, color);
    }

    @Test
    public void instanceTest(){
        assertSame(0, board.getStudentsOfType(Type_Student.DRAGON));
        assertSame(0, board.getStudentsOfType(Type_Student.GNOME));
        assertSame(0, board.getStudentsOfType(Type_Student.FAIRIE));
        assertSame(0, board.getStudentsOfType(Type_Student.UNICORN));
        assertSame(0, board.getStudentsOfType(Type_Student.FROG));
        assertSame(1, board.getCoinsNumber());
        assertSame(towersNum, board.getTowersNum());
        assertFalse(board.hasNoTowersLeft());
        assertTrue(board.getEntrance().isEmpty());
    }

    @Test
    public void entranceTest(){
        ArrayList<Student> students = new ArrayList<>();

        for (Type_Student t : Type_Student.values()){
            students.add(new Student(t));
        }
        board.setEntrance(students);
        assertEquals(students, board.getEntrance());

        for (Student s : students){
            try {
                board.placeStudent(s);
            }catch (Exception e){
                fail();
            }
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
        CharacterCard c = new Ch_2();

        for(int i=0; i<dragons; i++){
            students.add(new Student(Type_Student.DRAGON));
        }

        board.setEntrance(students);

        for(Student s : students) {
            try {
                board.placeStudent(s);
            } catch (Exception e) {
                fail();
            }
        }
        assertSame(4, board.getCoinsNumber());
        assertSame(dragons, board.getStudentsOfType(Type_Student.DRAGON));
        assertTrue(board.getEntrance().isEmpty());

        student = new Student(Type_Student.DRAGON);
        assertThrows(Exception.class, ()->board.placeStudent(student));
        try {
            board.addStudent(student);
        }catch (Exception e){
            fail();
        }
        assertThrows(Exception.class, ()->board.placeStudent(student));
        board.removeStudent(student);
        assertFalse(board.getEntrance().contains(student));

        try {
            board.playCharacter(c);
            assertTrue(c.hasBeenActivated());
        }catch (Exception e){
            fail();
        }
        assertSame(2, board.getCoinsNumber());
        assertThrows(Exception.class, ()->board.playCharacter(new Ch_3()));
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