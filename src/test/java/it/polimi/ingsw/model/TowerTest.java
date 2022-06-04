package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.model.Colors.*;
import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    @Test
    void getColor() {
        ArrayList<Student> entrance = new ArrayList<>();
        Board b=new Board(1,BLACK);
        Tower t=new Tower(BLACK,b);
        assertEquals(t.getColor().getName(),"Nero");
        b=new Board(1,GREY);
        t=new Tower(GREY,b);
        assertEquals(t.getColor().getName(),"Grigio");
        b=new Board(1,WHITE);
        t=new Tower(WHITE,b);
        assertEquals(t.getColor().getName(),"Bianco");
    }
}