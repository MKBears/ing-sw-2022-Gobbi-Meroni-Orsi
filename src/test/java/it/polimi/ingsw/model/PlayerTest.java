package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private static Player player1, player2;
    private static String id1, id2;
    private static Colors color;
    private static Wizards wizard;

    @BeforeAll
    public static void instantiate() {
        id1 = "Abcd";
        id2 = "MK00";
        color = Colors.BLACK;
        wizard = Wizards.WIZARD1;
        player1 = new Player(id1, color, 8, wizard, false);
        player2 = new Player(id2, color, 8, wizard, true);
    }

    @Test
    public void instanceTest(){
        assertFalse (player1.hasNoCardsLeft());
        assertEquals(id1, player1.getUserName());
        assertEquals(id2, player2.getUserName());
        assertSame(color, player1.getColor());
        assertNotNull(player1.getBoard());
        assertTrue(player2.getBoard() instanceof Board_Experts);
    }

    @Test
    public void deckTest(){
        int card;
        ArrayList<AssistantCard> deck;

        for (int i=1; i<=10; i++){
            player1.draw(player1.getDeck().get(i));
            assertSame(i, player1.getPlayedCard().getValue());
            assertSame((i+1)/2, player1.getPlayedCard().getMNSteps());
            deck = player1.getDeck();

            for (AssistantCard c : deck){
                assertNotSame(i, c.getValue());
            }
        }
        assertTrue(player1.hasNoCardsLeft());
    }

    @Test
    public void wizardTest() {
        assertSame(wizard, player1.getWizard());
    }

}