package it.polimi.ingsw.ServerController;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Colors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Controller{
    private static final int port = 4096;
    private static int playersNum;
    private static boolean expertMatch;

    public static void main(String[] args) {
        ServerSocket sSocket;
        Socket socket;
        ClientHandler player;
        ArrayList<ClientHandler> players;
        int i = 0;
        boolean[] wizards = new boolean[4];
        playersNum = 0;

        try {
            sSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server ready");
        players = new ArrayList<>();

        do {
            try {
                socket = sSocket.accept();
                player = new ClientHandler(i, socket);
                player.start();
                players.add(player);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Connected with player " + (i + 1));

            if (playersNum == 0){
                playersNum = player.choosePlayersNum();
                players.notifyAll();
                expertMatch = player.expertMatch();
            }
            i++;
        }while (i<playersNum);

        for (i=0; i<playersNum; i++){
            wizards[players.get(i).setWizard(wizards)] = false;
        }

        for (i=0; i<playersNum; i++){
            players.get(i).createAvatar(Colors.values()[i], playersNum, expertMatch);
        }
    }

    public static int getPlayersNum() {
        return playersNum;
    }

    public void fillClouds (ArrayList<Cloud> clouds){
        for (Cloud c : clouds){
            c.importStudents();
        }
    }

    public void playAssistantCards(ArrayList<AssistantCard> played){
        for (int i=0; i<played.size()-1; i++){
            for (int j=i+1; j<played.size(); j++){
                if (played.get(j).getValue()==played.get(i).getValue()){
                    //pija le carte in mano al player j e controlla se ne ha almeno una che non é stata giocata in questa mano,
                    //se non ce ne ha nemmeno una, allora va bene la carta che ha giocato
                }
            }
        }
    }

}
