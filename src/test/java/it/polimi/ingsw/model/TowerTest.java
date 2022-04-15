package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.model.Colors.*;
import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    @Test
    void getColor() {
        ArrayList<Student> entrance = new ArrayList<>();
        Board b=new Board(1,BLACK, entrance);
        Tower t=new Tower(BLACK,b);
        assertEquals(t.getColor().getColor(),"black");
        b=new Board(1,GREY, entrance);
        t=new Tower(GREY,b);
        assertEquals(t.getColor().getColor(),"grey");
        b=new Board(1,WHITE, entrance);
        t=new Tower(WHITE,b);
        assertEquals(t.getColor().getColor(),"white");
    }
}