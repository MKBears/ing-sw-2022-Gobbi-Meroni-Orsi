package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanningPhaseTest {

    @Test
    public void FillCloudsTest() {
        Cloud[] clouds = new Cloud[4];
        Bag bag = new Bag();
        Student garbage;
        int movedStudents = 0;
        int i;

        for (i=0; i<4; i++){
            try {
                clouds[i] = new Cloud(bag, 4);
            } catch (Exception e) {
                fail();
            }
        }

        System.out.print("Scartando spazzatura: ");
        for (i=0; i<4; i++){
            try {
                garbage = bag.getRandomStudent();
                movedStudents++;
                System.out.print(movedStudents+ "  ");
            } catch (Exception e) {
                fail();
            }
        }
        System.out.println();

        for (i=0; i<9; i++){
            try {
                PlanningPhase.fillClouds(clouds);
                movedStudents += 12;
            } catch (Exception e) {
                fail();
            }

            for(Cloud c : clouds){
                assertSame(3, c.getStudents().size());
            }
        }
        assertThrows(Exception.class, ()->PlanningPhase.fillClouds(clouds));
        assertSame(3, clouds[0].getStudents().size());
        assertSame(3, clouds[1].getStudents().size());
        assertSame(2, clouds[2].getStudents().size());
        assertSame(0, clouds[3].getStudents().size());
    }

    //playAssistantCardsTest will be written in ClientHandlerTest
}