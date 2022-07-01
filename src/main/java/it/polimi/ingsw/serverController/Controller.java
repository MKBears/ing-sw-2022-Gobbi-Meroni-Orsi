package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.characterCards.Ch_1;
import it.polimi.ingsw.characterCards.Ch_11;

import javax.management.timer.Timer;
import java.io.*;
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
    private boolean go;
    private final ArrayList<AssistantCard> playedAssistants;
    private ClientHandler winner;
    private GameRecap gameRecap;
    private String endExplanation;
    private final boolean game_from_memory;
    private GameSaved gameSaved;

    /**
     * Constructor of class Controller:
     * is sets the creator, the number of players there will be in the match and if is an expert match or not
     * @param creator the player who set the match
     * @param playersNum the number of players which will connect to the match
     * @param expertMatch tells whether this is an expert match or not
     */
    public Controller (ClientHandler creator, int playersNum, boolean expertMatch){
        state = 0;
        this.playersNum = playersNum;
        this.expertMatch = expertMatch;
        players = new ClientHandler[playersNum];
        players[0] = creator;
        creator.setColor(Colors.WHITE);
        connectedPlayers = 1;
        playedAssistants = new ArrayList<>(playersNum);
        playing = true;
        paused = false;
        firstPlayer = 0;
        wizards = new ArrayList<>(Arrays.asList(Wizards.values()));
        game_from_memory=false;
    }

    /**
     * This constructor is used when is necessary to resume a game form the memory
     * @param first the first player that is connected
     * @param gamesaved the informations about the game to resume
     */
    public Controller (ClientHandler first,GameSaved gamesaved){
        this.gameSaved=gamesaved;
        state = gameSaved.state();
        this.playersNum = gameSaved.players_num();
        this.expertMatch = gameSaved.expert_match();
        players = new ClientHandler[gameSaved.players_num()];
        int i=0;
        for (String u: gameSaved.usernames()) {
           if(u.equals(first.getUserName())){
              players[i]=first;
              first.setController(this);
              players[i].setMatch(gameSaved.match());
              players[i].setAvatar(gameSaved.match().getPlayer()[i]);
              players[i].setPlayedAssistant(gameSaved.match().getPlayer()[i].getPlayedCard());
           }
           i++;
        }
        switch (gameSaved.state()){
            case 1 -> first.setState(1);
            case 2 -> first.setState(2);
            case 4 -> first.setState(3);
        }
        playedAssistants=new ArrayList<>();
        playing = true;
        paused = false;
        connectedPlayers=1;
        match=gameSaved.match();
        this.firstPlayer=gameSaved.firstPlayer();
        game_from_memory=true;
        paused=false;
        go=true;
    }


    public void run() throws IllegalArgumentException {
        //Start match
        if(isGame_from_memory()) {
            for (ClientHandler p : this.players) {
                switch (this.state) {
                    case 1 -> p.setState(1);
                    case 2 -> p.setState(2);
                    case 4 -> p.setState(3);
                }
            }
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!paused && state<6) {
            try {
                changeState();
            } catch (InterruptedException e) {
                notifyDeletion("Qualcosa non ha funzionato aspettando che un giocatore muovesse ("+e.getMessage()+")");
            } catch (Exception e) {
                notifyDeletion("Errore nel riempire gli ingressi ("+e.getMessage()+")");
                e.printStackTrace();
            }
        }
        //End match

        //Start connection closure
        for (ClientHandler p : players) {
            try {
                p.closeConnection();
            } catch (Exception e) {
                System.out.println("Closing client socket error. Ending match...");
                throw new RuntimeException(e);
            }
        }
        //End connection closure
    }

    /**
     * Is the finite state machine that controls the whole match.
     * It is made of cases that simulate the states of the ideal machine
     * @throws Exception if something goes wrong when performing some actions
     */
    private void changeState() throws Exception {
        switch (state) {
            case 0 -> {
                //MATCH PREPARATION phase
                ArrayList<Student> entrance = new ArrayList<>(7);
                go = true;

                for (int i=0; i<playersNum; i++) {
                    for (int j=0; j<7+2*(playersNum-2); j++) {
                        entrance.add(match.getBag().getRandomStudent());
                    }
                    players[i].getAvatar().getBoard().setEntrance(entrance);
                    players[i].setMatch(match);

                    synchronized (players[i]) {
                        players[i].notifyAll();
                    }
                    entrance.clear();
                }
                //sleep(1000);
                state = 1;
                save();
            }
            case 1 -> {
                //PLANNING phase: all the clouds are filled with 3 or 4 students
                fillClouds(match.getCloud());

                if (match.getBag().isEmpty())
                    notifyFinishedStudents();
                currentPlayer = firstPlayer;

                do {
                    synchronized (players[currentPlayer]) {
                        players[currentPlayer].notifyAll();
                    }
                    synchronized (this) {
                        wait();
                    }
                    moveCurrentPlayer();
                } while (currentPlayer != firstPlayer);

                state = 2;
                save();
            }
            case 2 -> {
                //PLANNING phase: each player plays an assistant card
                currentPlayer = firstPlayer;

                do {
                    notifyTurn("pianificazione");
                    synchronized (players[currentPlayer]) {
                        players[currentPlayer].notify();
                    }

                    synchronized (this) {
                        wait();
                    }
                    moveCurrentPlayer();
                } while (currentPlayer != firstPlayer);
                state = 3;
            }
            case 3 -> {
                //PLANNING phase: deciding the first player of the following action phase
                firstPlayer = 0;

                for (currentPlayer = 1; currentPlayer < playersNum; currentPlayer++)
                    if (players[currentPlayer].getAvatar().getPlayedCard().getValue() < players[firstPlayer].getAvatar().getPlayedCard().getValue())
                        firstPlayer = currentPlayer;
                state = 4;
                save();
            }
            case 4 -> {
                //ACTION phase
                currentPlayer = firstPlayer;

                synchronized (this) {
                    do {
                        notifyTurn("azione");

                        synchronized (players[currentPlayer]) {
                            players[currentPlayer].notifyAll();
                        }
                        sleep(100);
                        go = false;
                        wait();
                        moveCurrentPlayer();
                        go = true;
                    } while (currentPlayer!=firstPlayer && playing);
                }

                if (playing && !match.getBag().isEmpty()) {
                    state = 1;
                    save();
                    for (ClientHandler p:players) {
                        if(p.isFinished_assistant())
                            state=5;
                    }
                } else {
                    state = 5;
                }
                playedAssistants.clear();

                for (Cloud c : match.getCloud()) {
                    c.clearStudents();
                    c.reset();
                }
            }
            case 5 -> {
                //Match END: determine the winner
                currentPlayer = 0;
                playing=false;
                Player winner;
                winner = match.getWinner();

                for (ClientHandler player : players) {
                    if (player.getAvatar().equals(winner)) {
                        this.winner = player;
                    }
                }
                gameRecap = new GameRecap(players, match);
                for (ClientHandler player : players) {
                    player.setState(6);

                    synchronized (player) {
                        player.notify();
                        player.wait();
                    }
                    moveCurrentPlayer();
                }
                delete();
                state = 6;
            }
        }
    }

    /**
     *
     * @return the number of players in the match
     */
    public int getPlayersNum() {
        return playersNum;
    }

    /**
     *
     * @return the name of the player who created the match
     */
    public String getCreator(){
        return players[0].getUserName();
    }

    /**
     *
     * @param username the username of the player you want to get the ClientHandler
     * @return the ClientHandler instance associated to the specified username
     */
    public ClientHandler getPlayer (String username) {
        for (ClientHandler player : players) {
            if (player.getUserName().equals(username)) {
                return player;
            }
        }
        return null;
    }

    /**
     *
     * @return true if there aren't enough players to start the match
     */
    public boolean isNotFull(){
        int i = 0;

        if (connectedPlayers == playersNum)
            return true;
        else {
            for (ClientHandler player : players)
                i++;

            return i == playersNum;
        }
    }

    /**
     *
     * @return true if the match has been paused
     */
    public boolean isPaused(){
        return paused;
    }

    /**
     *
     * @return the list containing all the ClientHandler instances associated with the remote players
     */
    public ArrayList<String> getPlayers(){
        ArrayList<String> userNames = new ArrayList<>();
        for (ClientHandler player : players){
            if (player != null) {
                userNames.add(player.getUserName());
            }
        }
        return userNames;
    }

    /**
     * Adds a player to the match if it isn't full yet
     * @param player the player to add
     * @throws Exception if the match is already full
     */
    public synchronized void addPlayer (ClientHandler player) throws Exception{
        Colors color;
        if (connectedPlayers < playersNum) {
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
                if (p != null) {
                    if (p != player) {
                        p.getOutputStream().sendNotifyPlayerConnected(player.getUserName(), true);
                    }
                }
            }
        }
        else {
            throw new Exception("Partita gia' al completo");
        }
        sleep (500);
    }

    /**
     *
     * @return true if the match is ready to start
     */
    public boolean readyToStart() {
        return connectedPlayers==playersNum && match!=null;
    }

    /**
     * Connects a player to the match
     * @param player the player to connect
     */
    public synchronized void connectPlayer(ClientHandler player) {
        int index;
        state = 0;

        for (int i=0; i<playersNum; i++){
            if (players[i].getUserName().equals(player.getUserName())){

                if (firstPlayer == 0) {
                    index = playersNum-1;
                }
                else {
                    index = firstPlayer-1;
                }

                do {
                    if (players[index] != null) {
                        if (players[index].isConnected()) {
                            player.setState(players[index].seeState());
                            break;
                        }

                        if (index == 0 && firstPlayer != 0) {
                            index = playersNum - 1;
                        } else {
                            index--;
                        }
                    }
                    else {
                        return;
                    }
                } while (index != firstPlayer-1);

                if (!player.equals(players[firstPlayer])) {
                    reorderPlayers(player, i);
                }

                if (player != players[i]) {
                    players[i] = player;
                }
                player.setMatch(match);
                connectedPlayers++;
                break;
            }
            else {
                players[i].getOutputStream().sendNotifyPlayerConnected(player.getUserName(), true);
            }
        }
    }

    /**
     * Reorders the players when a disconnected player reconnects to the match
     * @param player the reconnected player
     * @param endPosition the position before the player connected
     */
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

    /**
     * Creates the match
     * @throws Exception if there is a suspicious number of players
     */
    public void createMatch() throws Exception{
        for (int i=0; i<playersNum; i++) {
            if (players[i] == null) {
                return;
            }
        }

        switch (playersNum) {
            case 2:
                if (expertMatch) {
                    match = new Expert_Match(players[0].getAvatar(), players[1].getAvatar());
                    ((Expert_Match)match).setCard();
                    for (ClientHandler ch:this.players) {
                        ch.setExpertMatch(true);
                    }
                } else {
                    match = new Match(players[0].getAvatar(), players[1].getAvatar());
                    for (ClientHandler ch:this.players) {
                        ch.setExpertMatch(false);
                    }
                }
                break;
            case 3:
                if (expertMatch) {
                    match = new Expert_Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar());
                    ((Expert_Match)match).setCard();
                    for (ClientHandler ch:this.players) {
                        ch.setExpertMatch(true);
                    }
                } else {
                    match = new Match(players[0].getAvatar(), players[1].getAvatar(), players[2].getAvatar());
                    for (ClientHandler ch:this.players) {
                        ch.setExpertMatch(false);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Numero di giocatori anomalo");
        }
        start();
    }

    /**
     *
     * @return the match
     */
    public Match getMatch() {
        return match;
    }

    /**
     * Notifies alla the connected players when a player disconnects from the match
     * @param player the disconnected player
     * @throws InterruptedException if a wait is interrupted
     */
    public synchronized void notifyPlayerDisconnected(ClientHandler player) throws InterruptedException {
        connectedPlayers--;

        for (ClientHandler p : players) {
            if (p.isConnected()){
                p.getOutputStream().sendNotifyPlayerConnected(player.getUserName(), false);

                if (connectedPlayers == 1) {
                    p.getOutputStream().sendNotifyAllPlayersDisconnected();
                    sleep (Timer.ONE_MINUTE/30);
                    switch (connectedPlayers) {
                        case 0:
                            paused = true;
                            state = 6;
                            break;
                        case 1:
                            if (p.isConnected()){
                                p.setState(6);
                                endExplanation = "Tutti i giocatori sono stati disconnessi per 30 secondi";
                                gameRecap = new GameRecap(players, match);

                                if (!players[currentPlayer].equals(p))
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
                else {
                    if (players[currentPlayer].equals(player))
                        synchronized (this) {
                            this.notify();
                        }
                }
            }
        }
    }

    /**
     * Moves the reference to the current player to one position forward
     */
    private void moveCurrentPlayer() {
        if (currentPlayer < playersNum-1) {
            currentPlayer++;
        }
        else {
            currentPlayer = 0;
        }
        if (!players[currentPlayer].isConnected())
            moveCurrentPlayer();
    }

    /**
     *
     * @param player the player who wants to know if it's their turn
     * @return true if the match has started and it's the player's turn
     */
    public boolean isMyTurn(ClientHandler player) {
        for (int i=0; i<playersNum; i++) {
            if (players[i] == null) {
                return false;
            }
        }
        return (players[currentPlayer].equals(player) && go) || !playing;
    }

    /**
     * Notifies all the connected players (and not the one currently moving)
     * which player is moving and the phase
     * @param phase the current phase of the match
     * @throws InterruptedException if a wait interrupts
     */
    private void notifyTurn (String phase) throws InterruptedException {
        for (ClientHandler player : players) {
            if (player != players[currentPlayer] && player.isConnected()) {
                synchronized (player) {
                    do {
                        player.getOutputStream().sendNextTurn(players[currentPlayer].getAvatar(), phase);
                        player.wait();
                    } while (player.getNack());
                }
            }
        }
    }

    /**
     * Notifies all the connected players if the match has been deleted
     * @param cause the cause of the deletion
     */
    public void notifyDeletion(String cause) {
        for (ClientHandler player : players){
            if (player.isConnected()){
                player.getOutputStream().sendGenericError("Partita eliminata: \n"+cause);
            }
        }
    }

    /**
     *
     * @return the list of available wizards
     */
    public ArrayList<Wizards> getWizards() {
        return wizards;
    }

    /**
     * Removes the chosen wizard from the list of available ones
     * @param wizard the chosen wizard
     */
    public synchronized void chooseWizard (Wizards wizard) {
        wizards.remove(wizard);
    }

    /**
     *
     * @return the list of the played assistants in the current round
     */
    public ArrayList<AssistantCard> getPlayedAssistants(){
        return (ArrayList<AssistantCard>) playedAssistants.clone();
    }

    /**
     * Plays an assistant card and adds it to the list of the played ones
     * @param assistant the played assistant
     * @param player the player who draw the assistant
     * @throws InterruptedException if a wait interrupts
     */
    public void playAssistantCard (AssistantCard assistant, ClientHandler player) throws InterruptedException {
        playedAssistants.add(assistant);

        for (ClientHandler p : players) {
            if (p != player && p.isConnected()) {
                do {
                    synchronized (p) {
                        p.getOutputStream().sendNotifyChosenCard(assistant, player.getAvatar());
                        p.wait();
                    }
                } while (p.getNack());
            }
        }

        synchronized (this) {
            notify();
        }
    }

    /**
     * Chooses the cloud and notifies to all players
     * @param cloud
     * @param player
     * @throws InterruptedException if a wait is interrupted
     */
    public void chooseCloud (Cloud cloud, ClientHandler player) throws InterruptedException {
        cloud.choose();
        for (ClientHandler p : players) {
            if (p != player && p.isConnected()) {
                synchronized (p) {
                    do {
                        p.getOutputStream().sendNotifyChosenCloud(player.getAvatar(), cloud);
                        p.wait();
                    } while (p.getNack());
                }
            }
        }
        cloud.clearStudents();

        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Fills all the clouds with some random students taken from the Bag
     *  (3 if it's a 2/4-players match, 4 if it's a 3-players match)
     * @param clouds
     */
    private void fillClouds (Cloud[] clouds){
        for (Cloud c : clouds){
            try {
                c.clearStudents();
                c.importStudents();
            } catch (Exception e) {
                notifyFinishedStudents();
            }
            //When Bag runs out of students, it throws an Exception which would be propagated to the class Controller
            // through Cloud and this class. Controller will then notify all the remote players that the match would
            // finish at the end of the current Round.
        }
    }

    /**
     * Notifies all players when one of them moves a student from the entrance of their board
     * @param player the player who moved the student
     * @param student the moved student
     * @param position where the player moved the student (the player's board or an island)
     * @throws InterruptedException if a wait is interrupted
     */
    public void notifyMovedStudent(ClientHandler player, Student student, int position) throws InterruptedException {
        for (ClientHandler p: players){
            if (p != player && p.isConnected()){
                synchronized (p) {
                    if (position == 12) {
                        do {
                            p.getOutputStream().sendNotifyMoveStudent(student,  player.getUserName());
                            p.wait();
                        } while (player.getNack());
                    } else {
                        do {
                            p.getOutputStream().sendNotifyMoveStudent(student, position, player.getUserName());
                            p.wait();
                        } while (player.getNack());
                    }
                }
            }
        }
    }

    /**
     * Notifies all the players when one of them moves mn
     * @param steps the number of steps the player moved mn
     * @throws InterruptedException if a wait is interrupted
     */
    public void notifyMovedMN (int steps) throws InterruptedException {
        ArrayList<Land> lands = match.getLands();
        for (ClientHandler p: players){
            if (p.isConnected()) {
                synchronized (p) {
                    do {
                        p.getOutputStream().sendNotifyMovementMN(steps, lands);
                        p.wait();
                    } while (p.getNack());
                }
            }
        }
    }

    /**
     * Finds out which player has more influence
     * on the island on which mn is and performs all the necessary changes on that island
     * @throws Exception if something goes wrong when changing the towers
     */
    public void controlLand() throws Exception {
        Land land;
        Player owner, player, dominant;
        ArrayList<Type_Student> myProfessors, dominantProfessors;
        int myInfluence, dominantInfluence;
        ArrayList<Tower> towers;
        owner = null;
        land = match.getMotherNature().getPosition();
        if(land.getTower()!=null){
            for (ClientHandler remotePlayer : players) {
                if (land.getTower().getColor().equals(remotePlayer.getColor())) {
                    owner = remotePlayer.getAvatar();
                    break;
                }
            }
            dominant = owner;
            dominantProfessors = getControlledProfessors(owner);
            dominantInfluence = land.getInfluence(dominantProfessors);
            if(dominant.isTwo_more_influence()){
                dominantInfluence+=2;
                dominant.setTwo_more_influence(false);
            }
            for (ClientHandler remotePlayer : players) {
                player = remotePlayer.getAvatar();

                if (dominant != player) {
                    myProfessors = getControlledProfessors(player);
                    myInfluence = land.getInfluence(myProfessors);
                    if(player.isTwo_more_influence()){
                        myInfluence+=2;
                        player.setTwo_more_influence(false);
                    }
                    if (myInfluence > dominantInfluence) {
                        dominant = player;
                        dominantInfluence = myInfluence;
                    } else if (myInfluence == dominantInfluence) {
                        dominant = null;
                    }
                }
            }

            if (dominant != owner) {
                towers = new ArrayList<>();

                for (int i = 0; i < land.size(); i++) {
                    try {
                        towers.add(dominant.getBoard().removeTower());

                        if (dominant.getBoard().hasNoTowersLeft()) {
                            for (ClientHandler winner : players)
                                if (winner.getAvatar().getUserName().equals(dominant.getUserName()))
                                    synchronized (winner) {
                                        do {
                                            notifyBuiltLastTower(winner);
                                        } while (winner.getNack());
                                    }
                            break;
                        }
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
                                        p.wait();
                                    } while (p.getNack());
                                }
                            }
                        }
                    }
                }
                land.changeTower(towers);
            }
        }else{
            int max=0,influence;
            Player Pmax=null;
            for (ClientHandler c:players) {
                ArrayList<Type_Student> professors=getControlledProfessors(c.getAvatar());
                influence=land.getInfluence(professors);
                if(c.getAvatar().isTwo_more_influence()){
                    influence+=2;
                    c.getAvatar().setTwo_more_influence(false);
                }
                if(influence>max){
                   max=influence;
                   Pmax=c.getAvatar();
                }
            }
            if(max>0){
                ArrayList<Tower> temp=new ArrayList<>();

                temp.add(Pmax.getBoard().removeTower());
                land.changeTower(temp);

                if (Pmax.getBoard().hasNoTowersLeft()) {
                    for (ClientHandler winner : players)
                        if (winner.getAvatar().getUserName().equals(Pmax.getUserName()))
                            synchronized (winner) {
                                do {
                                    notifyBuiltLastTower(winner);
                                } while (winner.getNack());
                            }
                }
            }
        }
    }

    /**
     *
     * @param player the player to check which professors they control
     * @return which professors the specified player controls
     */
    private ArrayList<Type_Student> getControlledProfessors  (Player player) {
        ArrayList<Type_Student> professors = new ArrayList<>();
        for (Type_Student t : match.getProfessors().keySet()) {
            if (match.getProfessors().get(t).equals(player)) {
                professors.add(t);
            }
        }
        return professors;
    }

    /**
     * Notifies all the connected players when someone controls a new professor
     * @throws InterruptedException if a wait is interrupted
     */
    public void notifyProfessors () throws InterruptedException {
        for (ClientHandler player : players) {
            if (player.isConnected()) {
                do {
                    player.getOutputStream().sendNotifyProfessors(match.getProfessors());
                    synchronized (player) {
                        player.wait();
                    }
                } while (player.getNack());
            }
        }
    }

    /**
     * Notifies all the connected players the changes happened when a player conquered a land
     * @throws Exception if there wasn't any tower on the island which changed before the changes happened
     */
    public void notifyChanges () throws Exception {
        Land position = match.getMotherNature().getPosition();
        ArrayList<Tower> previousTowers = null;
        String player1 = null;
        ClientHandler player2 = null;
        ArrayList<Tower> t=position.getPreviousTowers();

        if (t != null) {
            if (!t.isEmpty())
                previousTowers = t;
        }

        for (ClientHandler p : players) {
            if (position.getTower().getBoard().equals(p.getAvatar().getBoard()) && position.hasChanged()) {
                player1 = p.getUserName();
                break;
            }

            if(previousTowers!=null){
                if (previousTowers.get(0).getBoard().equals(p.getAvatar().getBoard())) {
                    player2 = p;
                }
            }
        }

        for (ClientHandler p : players) {
            if (p.isConnected()) {
                synchronized (p) {
                    do {
                        p.getOutputStream().sendNotifyTowers(position.getAllTowers(), position, player1);
                        p.wait();
                    } while (p.getNack());
                    if (previousTowers != null) {
                        do {
                            p.getOutputStream().sendNotifyTowers(player2.getAvatar().getBoard(), player2.getUserName());
                            p.wait();
                        } while (p.getNack());
                    }
                }
            }
        }
    }

    /**
     * Notifies all the connected players when there are no more students in the bag
     */
    public void notifyFinishedStudents() {
        if (playing) {
            for (ClientHandler player : players) {
                player.getOutputStream().sendNoMoreStudents();
            }
            endExplanation = "sono finiti gli studenti del sacchetto";
        }
    }

    /**
     * Notifies all the connected players when one of them finishes the assistant cards
     * @param player the player who finished the assistant cards
     */
    public void notifyEndedAssistants (ClientHandler player) {
        for (ClientHandler p: players){
            if (p != player){
                p.getOutputStream().sendFinishedAssistants(player.getAvatar());
            }
            p.setFinished_assistant(true);
        }
        endExplanation = player.getUserName()+" ha finito le carte assistente";
        playing = true;
    }

    /**
     * Notifies all the connected players when one of them builds their last tower
     * @param player the player who built their last tower
     */
    public void notifyBuiltLastTower (ClientHandler player) throws InterruptedException {
        for (ClientHandler p: players){
            if (p.isConnected()) {
                p.getOutputStream().sendLastTower(player.getAvatar());
                p.setState(6);
            }
        }
        endExplanation = player.getUserName()+" ha costruito tutte le torri";
        state = 5;
        playing = false;

        synchronized (this) {
            notify();
        }
    }

    /**
     * Notifies all the connected players when there are only three archipelagos
     */
    public void notifyThreeArchipelagos () {
        for (ClientHandler p: players){
            if (p.isConnected()){
                p.getOutputStream().sendThreeArchipelagos();
            }
        }
        endExplanation = "si sono formati " + match.getLands().size() + " gruppi di isole";
        state = 5;
        playing = false;
    }

    /**
     *
     * @return the winner of the match
     * @throws Exception if the match hasn't ended yer
     */
    public Player getWinner() throws Exception {
        if (playing){
            throw new Exception("partita ancora in corso");
        }
        return winner.getAvatar();
    }

    /**
     *
     * @return the reason why the match ended
     */
    public String getEndExplanation() {
        return endExplanation;
    }

    /**
     *
     * @return the recap of the ended match
     */
    public GameRecap getGameRecap() {
        return gameRecap;
    }

    /**
     * notify to the players the character card played to update the client
     * @throws InterruptedException
     */
    public void notifyCh() throws InterruptedException {
        for (ClientHandler player : players) {
            if (player.isConnected()) {
                do {
                    switch (players[currentPlayer].getChosenCh()) {
                        case "Ch_1" -> {
                            for (int i = 0; i < 3; i++) {
                                if (((Expert_Match) match).getCard()[i] instanceof Ch_1) {
                                    player.getOutputStream().sendNotifyCh_1(players[currentPlayer].getCh_1_land(), ((Ch_1) (((Expert_Match) match).getCard()[i])).copy(),
                                            players[currentPlayer].getCh_1_Student(), players[currentPlayer].getAvatar().getUserName());
                                }
                            }
                        }
                        case "Ch_2" -> player.getOutputStream().sendNotifyCh_2(match.getProfessors(), players[currentPlayer].getAvatar().getUserName());
                        case "Ch_8" -> player.getOutputStream().sendNotifyCh_8(players[currentPlayer].getAvatar().getUserName());
                        case "Ch_4" -> player.getOutputStream().sendNotifyCh_4(players[currentPlayer].getAvatar().getUserName());
                        case "Ch_5" ->
                                player.getOutputStream().sendNotifyCh_5(players[currentPlayer].getCh_5_land(), players[currentPlayer].getAvatar().getUserName());
                        case "Ch_10" -> player.getOutputStream().sendNotifyCh_10(players[currentPlayer].getAvatar().getUserName(),
                                players[currentPlayer].getCh_10_students(), players[currentPlayer].getCh_10_types());
                        case "Ch_11" -> {
                            for (int i = 0; i < 3; i++) {
                                if (((Expert_Match) match).getCard()[i] instanceof Ch_11)
                                    player.getOutputStream().sendNotifyCh_11(((Ch_11) ((Expert_Match) match).getCard()[i]).copy(),
                                            players[currentPlayer].getCh_11_student(), players[currentPlayer].getAvatar().getUserName());
                            }
                        }
                        case "Ch_12" ->
                                player.getOutputStream().sendNotifyCh_12(players[currentPlayer].getCh_12_type(), players[currentPlayer].getAvatar().getUserName());
                    }
                    synchronized (player) {
                        player.wait();
                    }
                } while (player.getNack());
            }
        }
    }

    /**
     * save the match into the file with the name of the first player
     */
    public void save(){
        File file=new File("matches/"+match.getPlayer()[0].getUserName()+".txt");
        File directory = new File("matches");
        FileOutputStream f;
        ObjectOutputStream out;
        file.delete();

        if (! directory.exists()){
            directory.mkdir();
        }

        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            file.setWritable(true);
            f=new FileOutputStream(file);
            out=new ObjectOutputStream(f);
            ArrayList<String> usernames=new ArrayList<>();
            for (Player p: match.getPlayer()) {
                usernames.add(p.getUserName());
            }
            GameSaved data=new GameSaved(match,state,playersNum,firstPlayer,expertMatch,usernames);
            out.writeObject(data);
            out.close();
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes the file of the game from the memory
     */
    public void delete(){
        File file=new File("matches/"+match.getPlayer()[0].getUserName());
        file.delete();
    }


    /**
     *
     * @return if the match is from the memory
     */
    public boolean isGame_from_memory() {
        return game_from_memory;
    }


    /**
     * check if there are the right number of player to restart
     * @param player the client handler of the player to be added
     */
    public void restartMatch(ClientHandler player){
        int j=-1;
        for (int i=0;i< match.getPlayersNum();i++) {
            if(match.getPlayer()[i].getUserName().equals(player.getUserName())){
                players[i]=player;
                connectedPlayers++;
                j=i;
            }
        }
        player.setController(this);
        player.setMatch(gameSaved.match());
        player.setExpertMatch(gameSaved.expert_match());
        player.setAvatar(gameSaved.match().getPlayer()[j]);
        player.setPlayedAssistant(gameSaved.match().getPlayer()[j].getPlayedCard());
        switch (this.state){
            case 1 -> player.setState(1);
            case 2 -> player.setState(2);
            case 4 -> player.setState(3);
        }
        if(connectedPlayers==gameSaved.players_num()){
            this.start();
        }
    }
}