package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.Ch_2;
import it.polimi.ingsw.model.characterCards.Ch_4;
import it.polimi.ingsw.model.characterCards.Ch_5;
import org.junit.jupiter.api.Test;

import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;

class Expert_MatchTest {

    @Test
    void getCard() {
        Player pl1=new Player("Gino",Colors.GREY,8,Wizards.WIZARD1, TRUE);
        Player pl2=new Player("Pina",Colors.BLACK,8,Wizards.WIZARD2, TRUE);
        Expert_Match eMatch=new Expert_Match(pl1,pl2);
    }

    @Test
    void getCard3Playes() {
        Player pl1=new Player("Gino",Colors.GREY,6,Wizards.WIZARD1, TRUE);
        Player pl2=new Player("Pina",Colors.BLACK,6,Wizards.WIZARD2, TRUE);
        Player pl3=new Player("Franco",Colors.WHITE,6,Wizards.WIZARD4,TRUE);
        Expert_Match eMatch=new Expert_Match(pl1,pl2);
    }
}