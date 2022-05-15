package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The main class of the server side of the game Eriantys
 */
public class Controller {
    private int state;
    private final int playersNum;
    private int connectedPlayers;
    private final ClientHandler[] players;
    private Match match;
    private boolean expertMatch;
    private int firstPlayer;
    private int currentPlayer;
    private boolean playing;
    private boolean paused;
    private final AssistantCard[] playedAssistants;
    private int numOfPlayedAssistants;
    private CharacterCard[] characters;

    public Controller (ClientHandler creator, int playersNum, boolean expertMatch){
        state = 0;
        this.playersNum = playersNum;
        this.expertMatch = expertMatch;
        players = new ClientHandler[playersNum];
        playedAssistants = new AssistantCard[playersNum];
        players[0] = creator;
        creator.setColor(Colors.WHITE);
        connectedPlayers = 1;
        playing = true;
        paused = false;
        firstPlayer = 0;

        if (expertMatch){
            characters = new CharacterCard[3];
        }
    }

    public void run() throws InterruptedException, IllegalArgumentException {


        //Start match
        while (playing) {



            currentPlayer = firstPlayer;
            //ACTION phase:
            for (i=0; i<playersNum; i++){
                notifyAll();

                if (currentPlayer<playersNum-1){
                    currentPlayer++;
                }
                else {
                    currentPlayer = 0;
                }
            }
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
    }

    private void changeState() throws InterruptedException {
        int i;
        switch (state){
            case 0:
                //MATCH PREPARATION phase
                switch (playersNum) {
                    case 2:
                        if (expertMatch){
                            match = new Expert_Match(players[0].getAvatar(), players[1].getAvatar());
                        }
                        else {
                            match = new Match(players[0].getAvatar(), players[1].getAvatar());
                        }
                        break;
                    case 3:
                        if (expertMatch){
                            match = new Expert_Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar());
                        }
                        else {
                            match = new Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar());
                        }
                        break;
            /*case 4:
                if (expertMatch){
                    match = new Expert_Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar(), players[3].getAvatar());
                }
                else {
                    match = new Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar(), players[3].getAvatar());
                }
                break;*/
                    default:
                        throw new IllegalArgumentException("Suspicious number of players");
                }
                for (ClientHandler player : players){
                    player.setMatch(match);
                }
                break;
            case 1:
                //PLANNING phase: all the clouds are filled with 3 or 4 students
                numOfPlayedAssistants = 0;
                currentPlayer = firstPlayer;
                try {
                    fillClouds(match.getCloud());
                } catch (Exception e) {
                    //Notifica il player remoto che sono finiti gli studenti
                }
                notifyAll();
                break;
            case 2:
                //PLANNING phase: each player plays an assistant card
                notifyAll();
                wait();
                numOfPlayedAssistants++;
                break;
            case 3:
                //PLANNING phase: deciding the first player of the following action phase
                firstPlayer = 0;

                for (i=1; i<playersNum; i++){
                    if (playedAssistants[i].getValue()<playedAssistants[firstPlayer].getValue()){
                        firstPlayer = i;
                    }
                }
                break;
            case 4:
                //ACTION phase:
        }
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public String getCreator(){
        return players[0].getUserName();
    }

    public boolean isNotFull(){
        return connectedPlayers != playersNum;
    }

    public boolean isPaused(){
        return paused;
    }

    public ArrayList<String> getPlayers(){
        ArrayList<String> userNames = new ArrayList<>();
        for (ClientHandler player : players){
            userNames.add(player.getUserName());
        }
        return userNames;
    }

    public synchronized void addPlayer (ClientHandler player) throws Exception{
        Colors color;
        if (connectedPlayers<playersNum) {
            players[connectedPlayers] = player;

            switch (connectedPlayers){
                case 2:
                    if (playersNum == 3){
                        color = Colors.GREY;
                    }
                    else {
                        color = Colors.WHITE;
                    }
                    break;
                default:
                    color = Colors.BLACK;
                    break;
            }
            player.setColor(color);
            connectedPlayers++;
        }
        else {
            throw new Exception("This match is already full");
        }
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }

    public boolean readyToStart() {
        return connectedPlayers == playersNum;
    }

    public synchronized void connectPlayer(ClientHandler player) {
        for (int i=0; i<playersNum; i++){
            if (players[i].getUserName().equals(player.getUserName())){
                player.setState(players[i].seeState());
                players[i] = player;
                connectedPlayers++;
                break;
            }
        }
    }

    public synchronized void playerDisconnected() {
        connectedPlayers--;
    }

    public boolean isMyTurn (ClientHandler player) {
        return players[currentPlayer] == player;
    }

    public void moveCurrentPlayer() {
        if (currentPlayer < playersNum - 1) {
            currentPlayer++;
        } else {
            currentPlayer = 0;
        }
    }

    public void notifyDeletion() {
        for (ClientHandler player : players){
            if (player.isConnected()){
                player.matchHasBeenDeleted(getCreator());
            }
        }
    }

    public AssistantCard[] getPlayedAssistants(){
        return playedAssistants;
    }

    public synchronized void playAssistantCard (AssistantCard assistant){
        playedAssistants[numOfPlayedAssistants] = assistant;
    }

    /**
     * Fills all the clouds with some students
     * @param clouds
     * @throws Exception if there aren't enough students to fill a cloud
     */
    private static ArrayList<Student> fillClouds (Cloud[] clouds) throws Exception{
        ArrayList<Student> students = new ArrayList<Student>();
        for (Cloud c : clouds){
            students.addAll(Arrays.asList(c.importStudents()));
            //When Bag runs out of students, it throws an Exception which would be propagated to the class Controller
            // through Cloud and this class. Controller will then notify all the remote players that the match would
            // finish at the end of the current Round.
        }
        return students;
    }
}