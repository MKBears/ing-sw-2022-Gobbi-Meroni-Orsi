package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private static Player player1, player2;
    private static int id;
    private static boolean[] values;
    private static Colors color;
    private static Wizards wizard;

    @BeforeAll
    public static void initialize() {
        id = 0;
        values = new boolean[10];
        color = Colors.BLACK;
        wizard = Wizards.WIZARD1;
        player1 = new Player(id, color, 8, wizard, false);
        player2 = new Player(id, color, 8, wizard, true);
    }

    @Test
    public void instanceTest(){
        assertFalse (player1.hasNoCardsLeft());
        assertSame(player1.getPlayerID(), id);
        assertSame(player1.getColor(), color);
        assertNotNull(player1.getBoard());
        assertTrue(player2.getBoard() instanceof Board_Experts);
    }

    @Test
    public void deckTest(){
        AssistantCard card;

        for (int i=0; i<10; i++){

            card = player1.draw();
            if (card == null){
                fail();
            }

            if (values[card.getValue()-1]){
                fail();
            }
            else{
                values[card.getValue()-1] = true;
            }
            assertSame(card.getMNSteps(), (card.getValue()+1)/2);
        }
        assertTrue(player1.hasNoCardsLeft());
    }

    @Test
    public void wizardTest() {
        assertSame(player1.getWizard(), wizard);
    }

}