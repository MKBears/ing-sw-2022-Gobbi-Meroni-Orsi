package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Colors.*;
import static org.junit.jupiter.api.Assertions.*;

class TowerTest {

    @Test
    void getColor() {
        Tower t=new Tower(BLACK);
        assertEquals(t.getColor().getColor(),"black");
        t=new Tower(GREY);
        assertEquals(t.getColor().getColor(),"grey");
        t=new Tower(WHITE);
        assertEquals(t.getColor().getColor(),"white");
    }
}