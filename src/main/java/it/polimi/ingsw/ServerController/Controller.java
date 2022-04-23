package it.polimi.ingsw.ServerController;

import it.polimi.ingsw.model.Colors;
import it.polimi.ingsw.model.Match;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Controller{
    private static final int port = 4096;
    private static int playersNum;
    private static boolean expertMatch;
    private static Match match;

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

        switch (playersNum){
            case 2: match = new Match(players.get(0).getAvatar(), players.get(1).getAvatar());
            case 3: match = new Match(players.get(0).getAvatar(), players.get(1).getAvatar(), players.get(2).getAvatar());
        }

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

}
