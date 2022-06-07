package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Type_Student.*;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
    void getType() {
        Student a=new Student(FROG);
        assertEquals(a.type(),FROG);
        a=new Student(DRAGON);
        assertEquals(a.type(),DRAGON);
        a=new Student(FAIRY);
        assertEquals(a.type(),FAIRY);
        a=new Student(GNOME);
        assertEquals(a.type(),GNOME);
        a=new Student(UNICORN);
        assertEquals(a.type(),UNICORN);
    }
}