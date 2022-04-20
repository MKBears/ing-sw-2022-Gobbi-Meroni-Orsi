package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;

class Expert_MatchTest {

    @Test
    void getCard() {
        ArrayList<Student> entrance = new ArrayList<>();
        Player pl1=new Player(1,Colors.GREY,8,Wizards.WIZARD1,TRUE,entrance);
        entrance=new ArrayList<>();
        Player pl2=new Player(2,Colors.BLACK,8,Wizards.WIZARD2,TRUE,entrance);
        CharacterCard[] a=new CharacterCard[3];
        a[0]=new Ch_2();
        a[1]=new Ch_4();
        a[2]=new Ch_5();
        Expert_Match eMatch=new Expert_Match(pl1,pl2,a);
        assertEquals(a[2],eMatch.getCard()[2]);
    }
}