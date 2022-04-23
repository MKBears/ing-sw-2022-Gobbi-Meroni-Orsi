package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MotherNatureTest {

    Island i= new Island(1);
    MotherNature mn=new MotherNature(i);

    @Test
    public void Test(){
        assertEquals(i,mn.getPosition());
        Island s=new Island(3);
        mn.setPosition(s);
        assertEquals(s,mn.getPosition());
    }
}