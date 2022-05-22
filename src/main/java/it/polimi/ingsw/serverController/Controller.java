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
    String endExplanation;

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
        wizards.addAll(Arrays.asList(Wizards.values()));
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
        int i;
        int index;
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
                state = 1;
                break;
            case 1:
                //PLANNING phase: all the clouds are filled with 3 or 4 students
                currentPlayer = firstPlayer;

                try {
                    fillClouds(match.getCloud());
                } catch (Exception e) {
                    notifyFinishedStudents();
                }

                for (ClientHandler player : players) {
                    synchronized (player) {
                        player.notify();
                    }
                }
                state = 2;
                break;
            case 2:
                //PLANNING phase: each player plays an assistant card
                synchronized (this) {
                    for (i=0; i<playersNum; i++) {
                        index = (i+firstPlayer)%playersNum;

                        synchronized (players[index]) {
                            players[index].notify();
                        }
                        wait();
                    }
                }
                state = 3;
                break;
            case 3:
                //PLANNING phase: deciding the first player of the following action phase
                firstPlayer = 0;

                for (i=1; i<playersNum; i++){
                    if (playedAssistants.get(i).getValue()<playedAssistants.get(firstPlayer).getValue()){
                        firstPlayer = i;
                    }
                }
                state = 4;
                break;
            case 4:
                //ACTION phase
                currentPlayer = firstPlayer;

                synchronized (this) {
                    for (i = 0; i < playersNum; i++) {
                        index = (i + firstPlayer) % playersNum;

                        synchronized (players[index]) {
                            players[index].notify();
                        }
                        wait();

                        if (endExplanation.equals("Built all their towers")) {
                            break;
                        }
                    }
                }

                if (playing){
                    state = 1;
                }
                else {
                    state = 5;
                }

                for (i=playersNum-1; i>=0; i--){
                    playedAssistants.remove(i);
                }
                break;
            case 5:
                //Match END: determine the winner
                Player winner;
                winner = match.getWinner();

                for (ClientHandler player : players) {
                    if (player.getAvatar().equals(winner)){
                        this.winner = player;
                    }
                }
                gameRecap = new GameRecap(players, match);

                for (ClientHandler player : players) {
                    notify();
                }
                break;
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

    public void notifyDeletion(String cause) {
        for (ClientHandler player : players){
            if (player.isConnected()){
                player.getOutputStream().sendGenericError("Match has been deleted");
            }
        }
    }

    public ArrayList<Wizards> getWizards() {
        return wizards;
    }

    public synchronized void chooseWizard (Wizards wizard) {
        wizards.remove(wizard);
    }

    public ArrayList<AssistantCard> getPlayedAssistants(){
        return (ArrayList<AssistantCard>) playedAssistants.clone();
    }

    public synchronized void playAssistantCard (AssistantCard assistant, ClientHandler player){
        playedAssistants.add(assistant);

        for (ClientHandler p : players) {
            if (p != player) {
                p.getOutputStream().sendNotifyChosenCard(assistant, player.getAvatar());
            }
        }
        notify();
    }

    public ArrayList<Cloud> getChosenClouds() {
        return (ArrayList<Cloud>) chosenClouds.clone();
    }

    public synchronized void chooseCloud (Cloud cloud, ClientHandler player) {
        chosenClouds.add(cloud);

        for (ClientHandler p : players) {
            if (p != player) {
                p.getOutputStream().sendNotifyChosenCloud(player.getAvatar(), cloud);
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

    public void notifyMovedStudent(ClientHandler player, Student student, int position) {
        for (ClientHandler p: players){
            if (p != player){
                if (position == 12) {
                    p.getOutputStream().sendNotifyMoveStudent(student, p.getAvatar().getBoard(), player.getUserName());
                }
                p.getOutputStream().sendNotifyMoveStudent(student, position, player.getUserName());
            }
        }
    }

    public void notifyMovedMN (ClientHandler player, int steps) {
        ArrayList<Land> lands = match.getLands();
        for (ClientHandler p: players){
            if (p != player){
                p.getOutputStream().sendNotifyMovementMN (steps, lands);
            }
        }
    }

    public void controlLand(){
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
                            notifyBuiltLastTower(p);
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

    public void notifyProfessors () {
        for (ClientHandler player : players) {
            do {
                player.getOutputStream().sendNotifyProfessors(match.getProfessors());
                // synchronized (player) da fare non solo qui
            } while (player.getNack());
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