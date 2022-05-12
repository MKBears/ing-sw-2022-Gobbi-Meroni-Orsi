package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Student;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents the phase of planning of the match: fills all clouds with the correct number of students
 * (if there are enough in the bag) and makes all players play an assistant card
 */
public class PlanningPhase {

    /**
     * Fills all the clouds with some students
     * @param clouds
     * @throws Exception if there aren't enough students to fill a cloud
     */
    public static ArrayList<Student> fillClouds (Cloud[] clouds) throws Exception{
        ArrayList<Student> students = new ArrayList<Student>();
        for (Cloud c : clouds){
            students.addAll(Arrays.asList(c.importStudents()));
            //When Bag runs out of students, it throws an Exception which would be propagated to the class Controller
            // through Cloud and this class. Controller will then notify all the remote players that the match would
            // finish at the end of the current Round.
        }
        return students;
    }

    /**
     * For each player (remote controller) asks to play an Assistant Card
     * and chooses the first player of the next action phase
     * @param players
     * @param first the first player of the current phase (same as the one in the previous action phase)
     * @return the first player of the next action phase
     */
    public static ClientHandler playAssistantCards(ArrayList<ClientHandler> players, ClientHandler first){
        int i, offset;
        int minimumIndex;
        int playersNum;
        ClientHandler current;
        int[] playedCards;

        i = players.indexOf(first);
        playersNum = players.size();
        current = first;
        playedCards = new int[playersNum];
        minimumIndex = 0;

        for (int j=0; j<playersNum; j++){
            playedCards[j] = 0;
        }
        offset = i;

        do{
            playedCards[i-offset] = current.playAssistant(playedCards);

            if (playedCards[i-offset]<playedCards[minimumIndex]){
                minimumIndex = i;
            }
            i++;

            if (i==playersNum){
                i = 0;
            }
            current = players.get(i);
        }while (!current.equals(first));

        return players.get(minimumIndex);
    }
}
