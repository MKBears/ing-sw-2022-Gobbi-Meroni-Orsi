package it.polimi.ingsw.ServerController;

import it.polimi.ingsw.ServerController.ClientHandler;
import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Cloud;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class PreparationPhase {

    public static void fillClouds (Cloud[] clouds) throws Exception{
        for (Cloud c : clouds){
            c.importStudents();
            //Quando finiscono gli studenti, Bag lancia un'eccezione, che viene propagata prima da Cloud e poi da qui:
            //  nel controller viene raccolta e si invia a ogni controller client il messaggio che gli studenti sono finiti,
            //  e che quindi la partita terminera' alla fine di questo turno
        }
    }

    public static int playAssistantCards(ArrayList<ClientHandler> players, ClientHandler first){
        int i, j;
        int minimumIndex;
        int playersNum;
        ClientHandler current;
        int[] playedCards;

        i = players.indexOf(first);
        playersNum = players.size();
        current = first;
        playedCards = new int[playersNum];
        minimumIndex = 0;

        for (j=0; j<playersNum; j++){
            playedCards[j] = 0;
        }
        j = 0;

        do{
            playedCards[j] = current.playAssistant(playedCards);
            if (playedCards[j]<playedCards[minimumIndex]){
                minimumIndex = i;
            }
            j++;
            i++;

            if (i==playersNum){
                i = 0;
            }
            current = players.get(i);
        }while (!current.equals(first));

        return minimumIndex;
    }
}
