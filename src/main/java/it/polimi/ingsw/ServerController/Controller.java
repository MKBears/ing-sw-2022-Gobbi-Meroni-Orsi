package it.polimi.ingsw.ServerController;

import it.polimi.ingsw.model.Colors;
import it.polimi.ingsw.model.Match;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class of the server side of the game Eriantys
 */
public class Controller{
    private static final int port = 4096;
    private static int playersNum;
    private static boolean expertMatch;
    private static Match match;
    private static ClientHandler firstPlayer;

    public static void main(String[] args) {
        ServerSocket sSocket;
        Socket socket;
        ClientHandler player;
        ArrayList<ClientHandler> players;
        boolean seePhase;
        boolean playing;
        int i;
        boolean[] wizards;
        Scanner in;

        playersNum = 0;
        playing = true;
        i = 0;
        wizards = new boolean[4];
        in = new Scanner(System.in);

        try {
            sSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Unable to start the socket. Shutting down...");
            throw new RuntimeException(e);
        }
        System.out.println("Server ready");
        System.out.println("Notify each phase? [true/false]");
        seePhase = in.nextBoolean();
        players = new ArrayList<>();

    //Start connection setup
        do {
            try {
                if (seePhase){
                    System.out.println("Waiting for a connection...");
                }
                socket = sSocket.accept();
            } catch (IOException e) {
                System.out.println("Server cannot connect with a client. Shutting down...");
                throw new RuntimeException(e);
            }
            player = new ClientHandler(socket);
            System.out.println("Connected with player " + (i + 1) + ": " + player.getUserName());
            players.add(player);

            //Asks the first player to connect how many players will play and if they want an expert match
            if (playersNum == 0) {
                playersNum = player.choosePlayersNum();
                players.notifyAll();
                expertMatch = player.expertMatch();

                if (seePhase){
                    System.out.println("Number of players: " + playersNum);
                    System.out.println("Expert match: " + expertMatch);
                    System.out.println("---------------------------------------------");
                }
            }
            i++;
        } while (i < playersNum);
    //End connection setup

    //Start match preparation phase
        if (seePhase){
            System.out.println("Instantiating match");
        }
        switch (playersNum) {
            case 2:
                match = new Match(players.get(0).getAvatar(), players.get(1).getAvatar());
            case 3:
                match = new Match(players.get(0).getAvatar(), players.get(1).getAvatar(), players.get(2).getAvatar());
        }

        for (i = 0; i < playersNum; i++) {
            if (seePhase){
                System.out.println("Asking player "+(i+1)+" to choose a wizard");
            }
            wizards[players.get(i).setWizard(wizards)] = false;
        }

        for (i = 0; i < playersNum; i++) {
            if (seePhase){
                System.out.println("Creating avatar "+(i+1));
            }
            players.get(i).createAvatar(Colors.values()[i], playersNum, expertMatch);
        }
        firstPlayer = players.get(0);
    //End match preparation phase

    //Start match
        if (seePhase){
            System.out.println("---------------------------------------------");
            System.out.println("Match started");
            System.out.println();
        }
        while (playing) {

        //Start planning phase
            try {
                if (seePhase){
                    System.out.println("Filling clouds");
                }
                PlanningPhase.fillClouds(match.getCloud());
            } catch (Exception e) {
                if (seePhase){
                    System.out.println("Bag is empty: the match ends at the end of this round");
                }
                for (ClientHandler p : players) {
                    p.notifyEndMatch(true);
                }
                playing = false;
            } finally {
                if (seePhase){
                    System.out.println("Waiting for players to play an assistant card...");
                    System.out.println();
                }
                firstPlayer = PlanningPhase.playAssistantCards(players, firstPlayer);
                i = 0;

                //Checking if at least one player has no more assistant cards to play
                while (i<players.size() && i>=0 && playing){
                    if (players.get(i).getAvatar().hasNoCardsLeft()){
                        for(ClientHandler p : players){
                            p.notifyEndMatch(true);
                        }
                        playing = false;
                        i = -1;
                    }
                    i++;
                }

                if (seePhase){
                    System.out.println("Done");
                    System.out.println("---------------------------------------------");
                }
        //End planning phase

        //Start action phase
                while (true) {

                }
            }
        //End action phase
        }
    //End match

    //Start connection closure
        for (ClientHandler p : players) {
            try {
                p.closeConnection();
            } catch (Exception e) {
                System.out.println("Closing client socket error. Shutting down...");
                throw new RuntimeException(e);
            }
        }
    //End connection closure

        try {
            sSocket.close();
        } catch (IOException e) {
            System.out.println("Closing server socket error. Shutting down...");
            throw new RuntimeException(e);
        }

    }

}