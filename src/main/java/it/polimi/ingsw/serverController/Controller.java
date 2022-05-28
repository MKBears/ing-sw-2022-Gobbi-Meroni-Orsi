package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import javax.management.timer.Timer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The main class of the server side of the game Eriantys
 */
public class Controller extends Thread{
    private int state;
    private final int playersNum;
    private int connectedPlayers;
    private final ClientHandler[] players;
    private Match match;
    private final boolean expertMatch;
    ArrayList<Wizards> wizards;
    private int firstPlayer;
    private int currentPlayer;
    private boolean playing;
    private boolean paused;
    private final ArrayList<AssistantCard> playedAssistants;
    private final ArrayList<Cloud> chosenClouds;
    private CharacterCard[] characters;
    private ClientHandler winner;
    private GameRecap gameRecap;
    private String endExplanation;

    public Controller (ClientHandler creator, int playersNum, boolean expertMatch){
        state = 0;
        this.playersNum = playersNum;
        this.expertMatch = expertMatch;
        players = new ClientHandler[playersNum];
        players[0] = creator;
        creator.setColor(Colors.WHITE);
        connectedPlayers = 1;
        playedAssistants = new ArrayList<>(playersNum);
        chosenClouds = new ArrayList<>(playersNum);
        playing = true;
        paused = false;
        firstPlayer = 0;

        if (expertMatch){
            characters = new CharacterCard[3];
        }
        wizards = new ArrayList<>(Arrays.asList(Wizards.values()));
    }

    public void run() throws IllegalArgumentException {


        //Start match
        while (!paused) {
            try {
                changeState();
            } catch (InterruptedException e) {
                notifyDeletion("Something went wrong waiting for a player to act");
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
        switch (state) {
            case 0 -> {
                //MATCH PREPARATION phase
                switch (playersNum) {
                    case 2:
                        if (expertMatch) {
                            match = new Expert_Match(players[0].getAvatar(), players[1].getAvatar());
                        } else {
                            match = new Match(players[0].getAvatar(), players[1].getAvatar());
                        }
                        break;
                    case 3:
                        if (expertMatch) {
                            match = new Expert_Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar());
                        } else {
                            match = new Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Suspicious number of players");
                }
                for (ClientHandler player : players) {
                    player.setMatch(match);
                }
                state = 1;
            }
            case 1 -> {
                //PLANNING phase: all the clouds are filled with 3 or 4 students
                currentPlayer = firstPlayer;
                try {
                    fillClouds(match.getCloud());
                } catch (Exception e) {
                    notifyFinishedStudents();
                }
                do {
                    synchronized (players[currentPlayer]) {
                        players[currentPlayer].notify();
                    }
                    moveCurrentPlayer();
                } while (currentPlayer != firstPlayer);
                state = 2;
            }
            case 2 -> {
                //PLANNING phase: each player plays an assistant card
                currentPlayer = firstPlayer;
                synchronized (this) {
                    do {
                        notifyTurn("Planning phase");
                        synchronized (players[currentPlayer]) {
                            players[currentPlayer].notify();
                        }
                        moveCurrentPlayer();
                        wait();
                    } while (currentPlayer != firstPlayer);
                }
                state = 3;
            }
            case 3 -> {
                //PLANNING phase: deciding the first player of the following action phase
                firstPlayer = 0;
                for (currentPlayer = 1; currentPlayer < playersNum; currentPlayer++) {
                    if (playedAssistants.get(currentPlayer).getValue() < playedAssistants.get(firstPlayer).getValue()) {
                        firstPlayer = currentPlayer;
                    }
                }
                state = 4;
            }
            case 4 -> {
                //ACTION phase
                currentPlayer = firstPlayer;
                synchronized (this) {
                    do {
                        notifyTurn("Action phase");
                        synchronized (players[currentPlayer]) {
                            players[currentPlayer].notify();
                        }
                        wait();

                        if (endExplanation.equals("Built all their towers")) {
                            break;
                        }
                    } while (currentPlayer != firstPlayer);
                }
                if (playing) {
                    state = 1;
                } else {
                    state = 5;
                }
                while (!playedAssistants.isEmpty()) {
                    playedAssistants.remove(0);
                }
            }
            case 5 -> {
                //Match END: determine the winner
                Player winner;
                winner = match.getWinner();
                for (ClientHandler player : players) {
                    if (player.getAvatar().equals(winner)) {
                        this.winner = player;
                    }
                }
                gameRecap = new GameRecap(players, match);
                for (ClientHandler player : players) {
                    player.notify();
                }
            }
        }
    }

    public int getPlayersNum() {
        return playersNum;
    }

    public String getCreator(){
        return players[0].getUserName();
    }

    public ClientHandler getPlayer (String username) {
        for (ClientHandler player : players) {
            if (player.getUserName().equals(username)) {
                return player;
            }
        }
        return null;
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

            if  (connectedPlayers == 2) {
                if (playersNum == 3) {
                    color = Colors.GREY;
                } else {
                    color = Colors.WHITE;
                }
            }
            else {
                color = Colors.BLACK;
            }
            player.setColor(color);
            connectedPlayers++;

            for (ClientHandler p : players) {
                if (p != player) {
                    p.getOutputStream().sendNotifyPlayerConnected(player.getUserName(), true);
                }
            }
        }
        else {
            throw new Exception("This match is already full");
        }
    }

    public boolean readyToStart() {
        return connectedPlayers == playersNum;
    }

    public synchronized void connectPlayer(ClientHandler player) {
        int state;

        for (int i=0; i<playersNum; i++){
            if (players[i].getUserName().equals(player.getUserName())){
                if (paused) {
                    state = players[i].seeState();
                }
                else {
                    if (firstPlayer != 0) {
                        state = players[firstPlayer-1].seeState();
                    }
                    else {
                        state = players[playersNum-1].seeState();
                    }

                    if (!player.equals(players[firstPlayer])) {
                        reorderPlayers (player, i);
                    }

                    if (player != players[i]) {
                        players[i] = player;
                    }
                }
                player.setState(state);
                player.setMatch(match);
                connectedPlayers++;
                break;
            }
            else {
                players[i].getOutputStream().sendNotifyPlayerConnected(player.getUserName(), true);
            }
        }
    }

    private void reorderPlayers (ClientHandler player, int endPosition) {
        int position = firstPlayer;
        ClientHandler removed = players[firstPlayer];
        ClientHandler toPlace = player;

        do {
            players[position] = toPlace;
            toPlace = removed;

            if (position==playersNum-1 && endPosition!=playersNum-1) {
                position = 0;
            }
            else {
                position++;
            }
            removed = players[position];
        } while(position <= endPosition);

        if (firstPlayer == playersNum-1) {
            firstPlayer = 0;
        }
        else {
            firstPlayer++;
        }
    }

    public Match getMatch() {
        return match;
    }

    public synchronized void notifyPlayerDisconnected(ClientHandler player) throws InterruptedException {
        connectedPlayers--;

        for (ClientHandler p : players) {
            if (p.isConnected()){
                p.getOutputStream().sendNotifyPlayerConnected(player.getUserName(), false);

                if (connectedPlayers == 1) {
                    p.getOutputStream().sendNotifyAllPlayersDisconnected();
                    sleep (Timer.ONE_MINUTE);
                    switch (connectedPlayers) {
                        case 0:
                            paused = true;
                            //salva la partita in memoria
                            break;
                        case 1:
                            if (p.isConnected()){
                                p.setState(6);

                                synchronized (p) {
                                    p.notify();
                                }
                            }
                            break;
                        default:
                            synchronized (this) {
                                notify();
                            }
                            break;
                    }
                }
            }
        }
    }

    private void moveCurrentPlayer() {
        if (currentPlayer <= playersNum-1) {
            currentPlayer++;
        }
        else {
            currentPlayer = 0;
        }
    }

    public boolean isMyTurn(ClientHandler player) {
        return players[currentPlayer] == player;
    }

    private void notifyTurn (String phase) throws InterruptedException {
        for (ClientHandler player : players) {
            if (player != players[currentPlayer]) {
                synchronized (player) {
                    do {
                        player.getOutputStream().sendNextTurn(players[currentPlayer].getAvatar(), phase);
                        wait();
                    } while (player.getNack());
                }
            }
        }
    }

    public void notifyDeletion(String cause) {
        for (ClientHandler player : players){
            if (player.isConnected()){
                player.getOutputStream().sendGenericError("Match has been deleted: \n"+cause);
            }
        }
    }

    public ArrayList<Wizards> getWizards() {
        return wizards;
    }

    public synchronized void chooseWizard (Wizards wizard) {
        wizards.remove(wizard);
        System.out.println("Rimosso "+wizard.toString());
    }

    public ArrayList<AssistantCard> getPlayedAssistants(){
        return (ArrayList<AssistantCard>) playedAssistants.clone();
    }

    public synchronized void playAssistantCard (AssistantCard assistant, ClientHandler player) throws InterruptedException {
        playedAssistants.add(assistant);

        for (ClientHandler p : players) {
            if (p != player) {
                do {
                    p.getOutputStream().sendNotifyChosenCard(assistant, player.getAvatar());
                    wait();
                } while (p.getNack());
            }
        }
        notify();
    }

    public ArrayList<Cloud> getChosenClouds() {
        return (ArrayList<Cloud>) chosenClouds.clone();
    }

    public void chooseCloud (Cloud cloud, ClientHandler player) throws InterruptedException {
        chosenClouds.add(cloud);

        for (ClientHandler p : players) {
            if (p != player) {
                synchronized (p) {
                    do {
                        p.getOutputStream().sendNotifyChosenCloud(player.getAvatar(), cloud);
                        wait();
                    } while (p.getNack());
                }
            }
        }
        notify();
    }

    /**
     * Fills all the clouds with some random students taken from the Bag
     *  (3 if it's a 2/4-players match, 4 if it's a 3-players match)
     * @param clouds
     */
    private void fillClouds (Cloud[] clouds){
        for (Cloud c : clouds){
            try {
                c.importStudents();
            } catch (Exception e) {
                notifyFinishedStudents();
            }
            //When Bag runs out of students, it throws an Exception which would be propagated to the class Controller
            // through Cloud and this class. Controller will then notify all the remote players that the match would
            // finish at the end of the current Round.
        }
    }

    public void notifyMovedStudent(ClientHandler player, Student student, int position) throws InterruptedException {
        for (ClientHandler p: players){
            if (p != player){
                synchronized (p) {
                    if (position == 12) {
                        do {
                            p.getOutputStream().sendNotifyMoveStudent(student, p.getAvatar().getBoard(), player.getUserName());
                            wait();
                        } while (player.getNack());
                    } else {
                        do {
                            p.getOutputStream().sendNotifyMoveStudent(student, position, player.getUserName());
                            wait();
                        } while (player.getNack());
                    }
                }
            }
        }
    }

    public void notifyMovedMN (ClientHandler player, int steps) throws InterruptedException {
        ArrayList<Land> lands = match.getLands();
        for (ClientHandler p: players){
            synchronized (p) {
                do {
                    p.getOutputStream().sendNotifyMovementMN(steps, lands);
                    wait();
                } while (p.getNack());
            }
        }
    }

    public void controlLand() throws Exception {
        Land land;
        Player owner, player, dominant;
        ArrayList<Type_Student> myProfessors, dominantProfessors;
        int myInfluence, dominantInfluence;
        ArrayList<Tower> towers;
        owner = null;
        land = match.getMotherNature().getPosition();

        for (ClientHandler remotePlayer : players) {
            if (land.getTower().getColor().equals(remotePlayer.getColor())) {
                owner = remotePlayer.getAvatar();
                break;
            }
        }
        dominant = owner;
        dominantProfessors = getControlledProfessors(owner);
        dominantInfluence = land.getInfluence(dominantProfessors);

        for (ClientHandler remotePlayer : players) {
            player = remotePlayer.getAvatar();

            if (dominant != player) {
                myProfessors = getControlledProfessors(player);
                myInfluence = land.getInfluence(myProfessors);

                if (myInfluence > dominantInfluence) {
                    dominant = player;
                    dominantInfluence = myInfluence;
                }
            }
        }

        if (dominant != owner) {
            towers = new ArrayList<>();

            for (int i=0; i<land.size(); i++) {
                try {
                    towers.add(dominant.getBoard().removeTower());
                } catch (Exception e) {
                    for (ClientHandler p : players) {
                        if (p.getAvatar().equals(dominant)) {
                            synchronized (this) {
                                try {
                                    wait();
                                } catch (InterruptedException ex) {
                                    for (ClientHandler pl : players) {
                                        pl.getOutputStream().sendGenericError("Internal server error");
                                    }
                                    throw new RuntimeException(ex);
                                }
                            }
                            synchronized (p) {
                                do {
                                    notifyBuiltLastTower(p);
                                    wait();
                                } while (p.getNack());
                            }
                        }
                    }
                }
            }
            land.changeTower(towers);
        }
    }

    private ArrayList<Type_Student> getControlledProfessors  (Player player) {
        ArrayList<Type_Student> professors = new ArrayList<>(5);

        for (Type_Student t : Type_Student.values()) {
            if (match.checkProfessor(t).equals(player)) {
                professors.add(t);
            }
        }
        return professors;
    }

    public void notifyProfessors () throws InterruptedException {
        for (ClientHandler player : players) {
            do {
                player.getOutputStream().sendNotifyProfessors(match.getProfessors());
                synchronized (player) {
                    wait();
                }
            } while (player.getNack());
        }
    }

    public void notifyChanges () throws Exception {
        Land position = match.getMotherNature().getPosition();
        ArrayList<Tower> previousTowers = position.getPreviousTowers();
        String player1 = null;
        ClientHandler player2 = null;

        for (ClientHandler p : players) {
            if (position.getTower().getBoard() == p.getAvatar().getBoard()) {
                player1 = p.getUserName();
                break;
            }

            if (previousTowers.get(0).getBoard() == p.getAvatar().getBoard()) {
                player2 = p;
            }
        }

        for (ClientHandler p : players) {
            synchronized (p) {
                do {
                    p.getOutputStream().sendNotifyTowers(position.getAllTowers(), position, player1);
                    wait();
                } while (p.getNack());

                do {
                    p.getOutputStream().sendNotifyTowers(position.getAllTowers(), player2.getAvatar().getBoard(), player2.getUserName());
                    wait();
                } while (p.getNack());
            }
        }
    }

    private void notifyFinishedStudents() {
        for (ClientHandler player: players){
            player.getOutputStream().sendNoMoreStudents();
            player.endMatch();
        }
        endExplanation = "There are no more students in the bag";
        playing = false;
    }

    public void notifyEndedAssistants (ClientHandler player) {
        for (ClientHandler p: players){
            if (p != player){
                p.getOutputStream().sendFinishedAssistants(player.getAvatar());
            }
            p.endMatch();
        }
        endExplanation = player.getUserName()+" ended their assistants";
        playing = false;
    }

    public void notifyBuiltLastTower (ClientHandler player) {
        for (ClientHandler p: players){
            if (p != player){
                p.getOutputStream().sendLastTower(player.getAvatar());
            }
            p.setState(6);
            p.endMatch();
        }
        endExplanation = "Built all their towers";
        playing = false;
    }

    public Player getWinner() throws Exception {
        if (playing){
            throw new Exception("The match hasn't finished yet");
        }
        return winner.getAvatar();
    }

    public String getEndExplanation() {
        return endExplanation;
    }

    public GameRecap getGameRecap() {
        return gameRecap;
    }

    public void resumeMatch () {

    }
}