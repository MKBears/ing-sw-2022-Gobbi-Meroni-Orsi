package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private static Player player;
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
        player = new Player(id, color, 8, wizard);
    }

    @Test
    public void instanceTest(){
        assertFalse (player.hasNoCardsLeft());
        assertSame(player.getPlayerID(), id);
        assertSame(player.getColor(), color);
    }

    @Test
    public void deckTest(){
        AssistantCard card;

        for (int i=0; i<10; i++){

            card = player.draw();
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
        assertTrue(player.hasNoCardsLeft());
    }

    @Test
    public void wizardTest() {
        assertSame(player.getWizard(), wizard);
    }

}