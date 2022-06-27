package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.Ch_1;
import it.polimi.ingsw.model.characterCards.Ch_11;

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


    public Controller (ClientHandler first,GameSaved gamesaved){
        this.gameSaved=gamesaved;
        state = gameSaved.getState();
        this.playersNum = gameSaved.getPlayers_num();
        this.expertMatch = gameSaved.isExpert_match();
        players = new ClientHandler[gameSaved.getPlayers_num()];
        int i=0;
        for (String u: gameSaved.getUsernames()) {
           if(u.equals(first.getUserName())){
              players[i]=first;
              first.setController(this);
              players[i].setMatch(gameSaved.getMatch());
              players[i].setAvatar(gameSaved.getMatch().getPlayer()[i]);
              players[i].setPlayedAssistant(gameSaved.getMatch().getPlayer()[i].getPlayedCard());
           }
           i++;
        }
        switch (gameSaved.getState()){
            case 1 -> first.setState(1);
            case 2 -> first.setState(2);
            case 4 -> first.setState(3);
        }
        playedAssistants=new ArrayList<>();
        playing = true;
        paused = false;
        connectedPlayers=1;
        match=gameSaved.getMatch();
        this.firstPlayer=gameSaved.getFirstPlayer();
        game_from_memory=true;
        paused=false;
        playing=true;
        go=true;
    }


    public void run() throws IllegalArgumentException {
        System.out.println("Controller partito");
        //Start match
        if(isGame_from_memory()) {
            for (ClientHandler p : this.players) {
                switch (this.state) {
                    case 1 -> p.setState(1);
                    case 2 -> p.setState(2);
                    case 4 -> p.setState(3);
                }
            }
        }
        while (!paused && state!=6) {
            try {
                changeState();
                System.out.println("State = "+state);
            } catch (InterruptedException e) {
                notifyDeletion("Qualcosa non ha funzionato aspettando che un giocatore muovesse ("+e.getMessage()+")");
            } catch (Exception e) {
                notifyDeletion("Errore nel riempire gli ingressi ("+e.getMessage()+")");
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
                        System.out.println("Contoller: faccio mandare il match al player "+players[i].getUserName());
                        players[i].notifyAll();
                    }
                    entrance.clear();
                }
                System.out.println("Match impostato a tutti i player");
                //sleep(1000);
                state = 1;
                save();
            }
            case 1 -> {
                //PLANNING phase: all the clouds are filled with 3 or 4 students
                try {
                    fillClouds(match.getCloud());
                } catch (Exception e) {
                    notifyFinishedStudents();
                }
                System.out.println("Nuvole riempite");
                currentPlayer = firstPlayer;

                do {
                    synchronized (players[currentPlayer]) {
                        players[currentPlayer].notifyAll();
                        System.out.println("Controller: sveglio player "+players[currentPlayer].getUserName());
                    }
                    synchronized (this) {
                        wait();
                    }
                    System.out.println("muovo il giocatore");
                    moveCurrentPlayer();
                } while (currentPlayer != firstPlayer);
                System.out.println("uscito dal while");
                state = 2;
                save();
            }
            case 2 -> {
                //PLANNING phase: each player plays an assistant card
                currentPlayer = firstPlayer;

                do {
                    notifyTurn("pianificazione");
                    System.out.println("Player "+players[currentPlayer].getUserName()+" in pianificazione");
                    synchronized (players[currentPlayer]) {
                        players[currentPlayer].notify();
                        System.out.println("Player "+players[currentPlayer].getUserName()+" svegliato");
                    }

                    synchronized (this) {
                        wait();
                        System.out.println("So sveglio");
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
                /*synchronized (players[firstPlayer]){
                    if(currentPlayer!=firstPlayer) {
                        wait();
                        System.out.println(players[currentPlayer].getAvatar().getUserName()+": uscito dalla wait che ho costruito io");
                    }
                }*/
                synchronized (this) {
                    do {
                        notifyTurn("azione");

                        synchronized (players[currentPlayer]) {
                            players[currentPlayer].notifyAll();
                            System.out.println("Controller: "+players[currentPlayer].getUserName()+" in fase di azione");
                        }
                        sleep(100);
                        go = false;
                        wait();
                        moveCurrentPlayer();
                        go = true;

                        if (!playing)
                            if (endExplanation.equals("Ha costruito tutte le torri"))
                                break;
                    } while (currentPlayer != firstPlayer);
                }
                if (playing) {
                    state = 1;
                    save();
                } else {
                    state = 6;
                }
                playedAssistants.clear();

                for (Cloud c : match.getCloud()) {
                    c.clearStudents();
                    c.reset();
                }

                /*synchronized (players[firstPlayer]) {
                    System.out.println("didididididididi");
                    players[firstPlayer].wait();
                }*/
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
                    player.setState(6);
                    player.notify();
                }
                delete();
                state = 6;
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
            if (player != null) {
                userNames.add(player.getUserName());
            }
        }
        return userNames;
    }

    public int getCurrentPlayer(){
        return this.currentPlayer;
    }

    public int getFirstPlayer(){
        return this.firstPlayer;
    }

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

    public boolean readyToStart() {
        return connectedPlayers==playersNum && match!=null;
    }

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
        System.out.println("Controller: match creato");
        System.out.println("Faccio partire la partita");
        start();
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
        if (currentPlayer < playersNum-1) {
            currentPlayer++;
        }
        else {
            currentPlayer = 0;
        }
    }

    public boolean isMyTurn(ClientHandler player) {
        for (int i=0; i<playersNum; i++) {
            if (players[i] == null) {
                return false;
            }
        }
        return players[currentPlayer].equals(player) && go;
    }

    private void notifyTurn (String phase) throws InterruptedException {
        for (ClientHandler player : players) {
            if (player != players[currentPlayer]) {
                synchronized (player) {
                    do {
                        player.getOutputStream().sendNextTurn(players[currentPlayer].getAvatar(), phase, this);
                        player.wait();
                    } while (player.getNack());
                }
            }
        }
    }

    public void notifyDeletion(String cause) {
        for (ClientHandler player : players){
            if (player.isConnected()){
                player.getOutputStream().sendGenericError("Partita eliminata: \n"+cause);
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

    public void playAssistantCard (AssistantCard assistant, ClientHandler player) throws InterruptedException {
        playedAssistants.add(assistant);

        for (ClientHandler p : players) {
            if (p != player) {
                do {
                    synchronized (p) {
                        p.getOutputStream().sendNotifyChosenCard(assistant, player.getAvatar());
                        System.out.println("Aspetto "+p.getUserName());
                        p.wait();
                        System.out.println("Bellaaaaa");
                    }
                } while (p.getNack());
            }
        }

        synchronized (this) {
            System.out.println("Mi sveglio");
            notify();
        }
    }

    public void chooseCloud (Cloud cloud, ClientHandler player) throws InterruptedException {
        cloud.choose();
        System.out.println("Mando notifychosenCloud dicendo che "+player.getUserName()+" ha seclto la nuvola: "+cloud.toString());
        for (ClientHandler p : players) {
            if (p != player) {
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

    public void notifyMovedStudent(ClientHandler player, Student student, int position) throws InterruptedException {
        for (ClientHandler p: players){
            if (p != player){
                synchronized (p) {
                    System.out.println("mando notifymovedstudents");
                    if (position == 12) {
                        do {
                            p.getOutputStream().sendNotifyMoveStudent(student, player.getAvatar().getBoard(), player.getUserName());
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

    public void notifyMovedMN (ClientHandler player, int steps) throws InterruptedException {
        ArrayList<Land> lands = match.getLands();
        System.out.println("notifico lo spostamento\n"+lands.toString());
        for (ClientHandler p: players){
            synchronized (p) {
                do {
                    p.getOutputStream().sendNotifyMovementMN(steps, lands);
                    p.wait();
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

                        if (dominant.getBoard().hasNoTowersLeft())
                            for (ClientHandler winner : players)
                                if (winner.getAvatar().equals(dominant))
                                    synchronized (winner) {
                                        do {
                                            notifyBuiltLastTower(winner);
                                            winner.wait();
                                        } while (winner.getNack());
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
            }
        }
    }

    private ArrayList<Type_Student> getControlledProfessors  (Player player) {
        ArrayList<Type_Student> professors = new ArrayList<>();
        //System.out.println(match.getProfessors());
        for (Type_Student t : match.getProfessors().keySet()) {
            if (match.getProfessors().get(t).equals(player)) {
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
                    player.wait();
                }
            } while (player.getNack());
        }
    }

    public void notifyChanges () throws Exception {
        Land position = match.getMotherNature().getPosition();
        ArrayList<Tower> previousTowers = null;
        String player1 = null;
        ClientHandler player2 = null;
        ArrayList<Tower> t=position.getPreviousTowers();

        if (t != null) { /////////////
            if (!t.isEmpty())
                previousTowers = t;
        }

        for (ClientHandler p : players) {
            if (position.getTower().getBoard() == p.getAvatar().getBoard()) {
                player1 = p.getUserName();
                break;
            }

            if(previousTowers!=null){
                if (previousTowers.get(0).getBoard() == p.getAvatar().getBoard()) {
                    player2 = p;
                }
            }
        }

        for (ClientHandler p : players) {
            synchronized (p) {
                do {
                    p.getOutputStream().sendNotifyTowers(position.getAllTowers(), position, player1);
                    p.wait();
                } while (p.getNack());
                if(previousTowers!=null) {
                    do {
                        p.getOutputStream().sendNotifyTowers(position.getAllTowers(), player2.getAvatar().getBoard(), player2.getUserName());
                        p.wait();
                    } while (p.getNack());
                }
            }
        }
    }

    public void notifyFinishedStudents() {
        for (ClientHandler player: players){
            player.getOutputStream().sendNoMoreStudents();
            player.endMatch();
        }
        endExplanation = "sono finiti gli studenti del sacchetto";
        playing = false;
    }

    public void notifyEndedAssistants (ClientHandler player) {
        for (ClientHandler p: players){
            if (p != player){
                p.getOutputStream().sendFinishedAssistants(player.getAvatar());
            }
            p.endMatch();
        }
        endExplanation = player.getUserName()+" ha finito le carte assistente";
        playing = false;
    }

    public void notifyBuiltLastTower (ClientHandler player) {
        for (ClientHandler p: players){
            if (p != player){
                p.getOutputStream().sendLastTower(player.getAvatar());
            }
            p.endMatch();
        }
        endExplanation = "Ha costruito tutte le torri";
        state = 5;
        playing = false;
    }

    public Player getWinner() throws Exception {
        if (playing){
            throw new Exception("Partita ancora in corso");
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

    /**
     * notify to the players the character card played to update the client
     * @throws InterruptedException
     */
    public void notifyCh() throws InterruptedException {
        for (ClientHandler player : players) {
            do {
                switch (players[currentPlayer].getChosenCh()) {
                    case "Ch_1" -> {
                        for (int i = 0; i < 3; i++) {
                            if (((Expert_Match) match).getCard()[i] instanceof Ch_1){
                                player.getOutputStream().sendNotifyCh_1(players[currentPlayer].getCh_1_land(),((Ch_1)(((Expert_Match) match).getCard()[i])).copy(),
                                        players[currentPlayer].getCh_1_Student(),players[currentPlayer].getAvatar().getUserName());
                            }
                        }
                    }
                    case "Ch_2" -> {
                        player.getOutputStream().sendNotifyCh_2(match.getProfessors(),players[currentPlayer].getAvatar().getUserName());
                    }
                    case "Ch_8" -> {
                        player.getOutputStream().sendNotifyCh_8(players[currentPlayer].getAvatar().getUserName());
                    }
                    case "Ch_4" -> {
                        player.getOutputStream().sendNotifyCh_4(players[currentPlayer].getAvatar().getUserName());
                    }
                    case "Ch_5" -> {
                        player.getOutputStream().sendNotifyCh_5(players[currentPlayer].getCh_5_land(),players[currentPlayer].getAvatar().getUserName());
                    }
                    case "Ch_10" -> {
                        player.getOutputStream().sendNotifyCh_10(players[currentPlayer].getAvatar().getUserName(),
                                players[currentPlayer].getCh_10_students(),players[currentPlayer].getCh_10_types());
                    }
                    case "Ch_11" -> {
                        for (int i = 0; i < 3; i++) {
                            if (((Expert_Match) match).getCard()[i] instanceof Ch_11)
                                player.getOutputStream().sendNotifyCh_11(((Ch_11)((Expert_Match) match).getCard()[i]).copy(),
                                        players[currentPlayer].getCh_11_student(), players[currentPlayer].getAvatar().getUserName());
                        }
                    }
                    case "Ch_12" -> {
                        player.getOutputStream().sendNotifyCh_12(players[currentPlayer].getCh_12_type(),players[currentPlayer].getAvatar().getUserName());
                    }
                }
                synchronized (player) {
                    player.wait();
                }
            } while (player.getNack());
        }
    }

    public void save(){
        File file=new File("src/main/resources/matches/"+match.getPlayer()[0].getUserName()+".txt");
        file.delete();
        FileOutputStream f;
        ObjectOutputStream out;
        try {
            file.createNewFile();
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

    public void delete(){
        File file=new File("src/main/resources/matches/"+match.getPlayer()[0].getUserName()+".txt");
        file.delete();
    }

    public boolean isGame_from_memory() {
        return game_from_memory;
    }

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
        player.setMatch(gameSaved.getMatch());
        player.setExpertMatch(gameSaved.isExpert_match());
        player.setAvatar(gameSaved.getMatch().getPlayer()[j]);
        player.setPlayedAssistant(gameSaved.getMatch().getPlayer()[j].getPlayedCard());
        switch (this.state){
            case 1 -> player.setState(1);
            case 2 -> player.setState(2);
            case 4 -> player.setState(3);
        }
        if(connectedPlayers==gameSaved.getPlayers_num()){
            this.start();
        }
        System.out.println(players[0].getAvatar().getUserName()+" "+players[1].getAvatar().getUserName());
    }
}