package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;
import it.polimi.ingsw.serverController.GameRecap;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The client side of the controller
 */
public class ClientGui  extends Thread {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Message4Server server;
    private Match match;
    private final Gui view;
    private Action action;
    private String username;
    private Player me;
    private boolean end;
    private int counter;
    private int tow;
    private AssistantCard ass;
    private boolean pm;
    private ArrayList<String> join;
    private ArrayList<String> resume;
    private List<Wizards> willy;
    private List<AssistantCard> cards;
    private List<Cloud> clouds;
    private AssistantCard card;
    private Player pl2;
    private Student stu;
    private int id;
    private String use;
    private Student s;
    private Board b;
    private String usern;
    private int movement;
    private ArrayList<Land> lands;
    private Map<Type_Student, Player> prof;
    private Player p;
    private Cloud cl;
    private ArrayList<Tower> towers;
    private Land land;
    private String f;
    private ArrayList<Tower> towers1;
    private Board board;
    private String u;
    private Player play;
    private String phase;
    private String n;
    private boolean connected;
    private MessageFromServer mfs;
    private String received;
    private String response;
    private Wizards wizard;
    private Player finish;
    private String error;
    private CharacterCard[] ch;
    private List<Student> ss;
    private Map<Type_Student, Player> profs;
    private ArrayList<Student> classroom;
    private ArrayList<Type_Student> type;
    private Type_Student ty;
    private boolean noch;
    private GameRecap gr;

    /**
     * Constructor of the class Client
     *
     * @param view the instance of the view (it can be Cli or Gui)
     */
    public ClientGui(Gui view) {
        match=null;
        username=null;
        me=null;
        end=false;
        counter=0;
        this.view = view;
        wizard = null;
        ass = null;
        pm = false;
        noch=false;
        gr=null;
    }

    /**
     * Sets the username of the player
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the message received from the server (the first is always a string)
     * @param received
     */
    public void setReceived(String received) {
        this.received = received;
    }

    public void run(){

        try {
            boolean condition = false;
            byte[] addr = InetAddress.getLocalHost().getAddress();
            addr[3]=(byte)255;
            DatagramSocket dSokk = new DatagramSocket();
            dSokk.setSoTimeout(5000);
            byte[] buf = new byte[1];
            DatagramPacket starting = new DatagramPacket(buf, 0, buf.length, InetAddress.getByAddress(addr), 4898);
            DatagramPacket packet;
            do {
                dSokk.send(starting);
                buf = new byte[1];
                packet = new DatagramPacket(buf, buf.length);
                try{
                    dSokk.receive(packet);
                }
                catch (SocketTimeoutException e){
                    counter ++;
                    if(counter==5){
                        view.printNotification("Connessione fallita. Far ripartire il client");
                        dSokk.close();
                        throw new RuntimeException();
                    }
                }
                if(packet.getData()[0]==1){
                    condition =true;
                }
            }while(!condition);

            InetAddress ip= packet.getAddress();
            int port= 2836;
            socket= new Socket(ip.getHostAddress(),port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in= new ObjectInputStream(socket.getInputStream());
            server=new Message4Server(out);
            view.setServer(server);
            received = "base";
            dSokk.close();
            synchronized (view) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        view.getTitolo();
                    }
                });
                sleep(2000);
            }
            while (!end) {
                synchronized (in) {
                    switch (received) {
                        case "base":
                            do {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.getUsername();
                                    }
                                });
                                synchronized (this) {
                                    this.wait();
                                }
                                response = (String) in.readObject();
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.printNotification(response);
                                    }
                                });
                            } while (response.equals("LoginFailed"));
                            received = "ok";
                            mfs = new MessageFromServer(in, server, this);
                            mfs.start();
                            break;
                        case "ListOfGames":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.chooseMatch(join, resume);
                                }
                            });
                            synchronized (this) {
                                this.wait();
                            }
                        case "ACK":
                            break;
                        case "NACK":
                            break;
                        case "Wizard":
                            view.setWilly(willy);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.getWizard(willy);
                                }
                            });
                            synchronized (this) {
                                this.wait();
                            }
                            wizard = view.getW();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.showLoading();
                                }
                            });
                            break;
                        case "Creation":
                            action = new Action(match);
                            for (int i = 0; i < match.getPlayer().length; i++) {
                                if (match.getPlayer()[i].getUserName().equals(username))
                                    me = match.getPlayer()[i];
                                if (match instanceof Expert_Match){
                                    view.setCharacters(((Expert_Match)match).getCard());
                                }
                            }
                            view.setMe(me);
                            view.setMatch(match);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                }
                            });
                            server.sendACK();
                            break;
                        case "RefillClouds":
                            noch=false;
                            Platform.runLater(new Runnable() {
                                @Override
                            public void run() {
                                    view.printMatch(match);
                                    if (match instanceof Expert_Match)
                                        view.setCharacters(((Expert_Match) match).getCard());
                                }
                            });
                            sleep(4500);
                            server.sendACK();
                            break;
                        case "ChooseCard":
                            view.setCards(cards);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Platform.setImplicitExit(false);
                                        view.printMatch(match);
                                        view.getAssistantCard();
                                    }
                                });
                            synchronized (this) {
                                this.wait();
                            }
                            ass = view.getAssistant();
                            break;
                        case "MoveStudents":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.moveStudent();
                                    view.printMatch(match);
                                }
                            });
                            synchronized (this) {
                                this.wait();
                                sleep(500);
                            }
                            break;
                        case "MoveMN":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.moveMN();
                                    view.printMatch(match);
                                }
                            });
                            synchronized (this) {
                                this.wait();
                                sleep(500);
                            }
                            break;
                        case "ChooseCloud":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.getCloud();
                                    view.printMatch(match);
                                }
                            });
                            synchronized (this) {
                                this.wait();
                                sleep(500);
                            }
                            break;
                        case "NotifyChosenCard":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(pl2.getUserName() +
                                            " ha giocato la carta:" + card.getValue());
                                    int r=0;
                                    for(Player p: match.getPlayer()){
                                        r++;
                                        if(p.getUserName().equals(play.getUserName())){
                                            break;
                                        }
                                    }
                                    view.showCard(r-1, card);
                                }
                            });
                            for (int i = 0; i < match.getPlayer().length; i++) {
                                if (match.getPlayer()[i].getUserName().equals(pl2.getUserName())) {
                                    match.getPlayer()[i].draw(card);
                                }
                            }
                            server.sendACK();
                            break;
                        case "NotifyMoveStudents (id)":
                            for (Land e : match.getLands()) {
                                if (id == e.getID())
                                    e.addStudent(stu);
                            }
                            for (int i = 0; i < match.getPlayer().length; i++) {
                                if (match.getPlayer()[i].getUserName().equals(use)) {
                                    match.getPlayer()[i].getBoard().removeStudent(stu);
                                }
                            }
                            action.checkAllProfessors();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(use + " ha spostato uno studente nell'isola " + (id+1));
                                    view.wakeUp("Start");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyMoveStudents (board)":
                            for (int i = 0; i < match.getPlayer().length; i++) {
                                if (match.getPlayer()[i].getUserName().equals(usern)) {
                                    match.getPlayer()[i].getBoard().placeStudent(s);
                                }
                            }
                            action.checkAllProfessors();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(usern + " ha spostato uno studente nella sua sala");
                                    view.wakeUp("Start");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyMovementMN":
                            int idLand;
                            match.moveMotherNature(movement);
                            match.setLands(lands);
                            idLand = match.getMotherNature().getPosition().getID();
                            for (Land l:match.getLands()) {
                                if(l.getID()==idLand){
                                    match.getMotherNature().setPosition(l);
                                }
                            }
                            if (match.getMotherNature().getPosition().isThereNoEntry()) {
                                match.getMotherNature().getPosition().setNoEntry(false);
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification("Madre Natura é stata spostata di " + movement
                                            + " passi nell'isola " + (idLand+1));
                                    view.wakeUp("Start");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyProfessors":
                            match.setProfessors(prof);
                            server.sendACK();
                            break;
                        case "NotifyChosenCloud":
                            for (int j = 0; j < match.getPlayersNum(); j++) {
                                if (match.getPlayer()[j].getUserName().equals(p.getUserName())) {
                                    p.getBoard().importStudents(cl.getStudents());
                                }
                            }
                            for (Cloud e : match.getCloud()) {
                                if (cl.equals(e)) {
                                    e.clearStudents();
                                    e.choose();
                                    break;
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                    view.printNotification(p.getUserName()
                                            + " ha scelto la nuvola");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyTowers (land)":
                            for (Land e : match.getLands()) {
                                for (int j = 0; j < match.getPlayersNum(); j++) {
                                    if (e.getID() == land.getID() && towers.get(0).getColor().equals(match.getPlayer()[j].getColor())) {
                                        ArrayList<Tower> tower = new ArrayList<>();
                                        for (int i = 0; i < tow; i++) {
                                            tower.add(match.getPlayer()[j].getBoard().removeTower());
                                        }
                                        for (int i = tow; i < e.size(); i++) {
                                            tower.add(new Tower(match.getPlayer()[j].getColor(), match.getPlayer()[j].getBoard()));
                                        }
                                        e.changeTower(tower);
                                        if (tow < e.size()) {
                                            for (int z = tow; z < e.size(); z++) {
                                                match.getPlayer()[j].getBoard().removeTower();
                                            }
                                        }
                                    }
                                }
                            }

                            action.uniteLands();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyTowers (board)":

                            for (int i = 0; i < match.getPlayer().length; i++) {
                                if (match.getPlayer()[i].getUserName().equals(u))
                                    match.getPlayer()[i].setBoard(board);
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                }
                            });
                            server.sendACK();
                            break;
                        case "EndGame":
                            end = true;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printEndGame(p, n, gr);
                                }
                            });
                            server.sendACK();
                            break;
                        case "LastTower":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(p.getColor() + p.getUserName() + " ha costruito tutte le torri");
                                }
                            });
                            end = true;
                            server.sendACK();
                            break;
                        case "NoMoreStudents":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification("Sono finiti gli studenti nel sacchetto! Questo sarà l'ultimo round\n");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NextTurn":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printTurn(play, phase);
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyPlayerConnected":
                            if (connected) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.playerConnected(n);
                                    }
                                });
                            } else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.playerDisconnected(n);
                                    }
                                });
                            }
                            server.sendACK();
                            break;
                        case "NotifyAllPlayersDisconnected":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.playerDisconnectedAll();
                                }
                            });
                            server.sendACK();
                            break;
                        case "FinishedAssistants":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(finish.getUserName()+" ha finito le carte assistente: ultimo turno");
                                }
                            });
                            break;
                        case "GenericError":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(error);
                                }
                            });
                            server.sendACK();
                            break;
                        case "Ch":
                            noch=false;
                            Board_Experts becs=(Board_Experts) me.getBoard();
                            for (CharacterCard c: ch) {
                                if (becs.getCoinsNumber() >= c.getPrice()){
                                    noch=true;
                                }
                            }
                            if (noch){
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        noch=true;
                                        view.getCharacter(ch);
                                    }
                                });
                            } else {
                                noch=false;
                                server.sendNoCh();
                            }
                            break;
                        case "NotifyCh_1":
                            for (int i = 0; i < 3; i++) {
                                if (((Expert_Match) match).getCard()[i] instanceof Ch_1)
                                    ((Ch_1) ((Expert_Match) match).getCard()[i]).setStudents((ArrayList<Student>) ss);
                            }
                            for (Land l : match.getLands()) {
                                if (l.getID() == land.getID()) {
                                    l.addStudent(stu);
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_1) {
                                    for (Player player : match.getPlayer()) {
                                        if (player.getUserName().equals(u)) {
                                            ((Board_Experts) player.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                    view.printNotification("Il player "+u+" ha giocato la carta personaggio 1");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyCh_2":
                            match.setProfessors(profs);
                            for (Player e : match.getPlayer()) {
                                if (e.getUserName().equals(u)) {
                                    for (Type_Student t : match.getProfessors().keySet()) {
                                        if (match.getProfessors().get(t).getBoard().getStudentsOfType(t) == e.getBoard().getStudentsOfType(t)) {
                                            match.getProfessors().replace(t, e);
                                        }
                                    }
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_2){
                                    for (Player pa: match.getPlayer()) {
                                        if(pa.getUserName().equals(u)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                    view.printNotification("Il player "+u+" ha giocato la carta personaggio 2");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyCh_4":
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_4){
                                    for (Player pa: match.getPlayer()) {
                                        if(pa.getUserName().equals(u)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification("Il giocatore " + u + " ha giocato la carta personaggio 4");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyCh_5":
                            if (!me.getUserName().equals(u)) {
                                for (Land l : match.getLands()) {
                                    if (l.getID() == land.getID()) {
                                        l.setNoEntry(true);
                                    }
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_5) {
                                    for (Player player : match.getPlayer()) {
                                        if (player.getUserName().equals(u)) {
                                            ((Board_Experts) player.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                    view.printNotification("Il player "+u+" ha giocato la carta personaggio 5");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyCh_11":
                            for (Player player:match.getPlayer()) {
                                if(player.getUserName().equals(u)){
                                    player.getBoard().ch_11_effect(s);
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_11){
                                    for (Player pa:match.getPlayer()) {
                                        if(pa.getUserName().equals(u)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            match.checkProfessor(s.type());
                            for (int i = 0; i < 3; i++) {
                                if(((Expert_Match)match).getCard()[i] instanceof Ch_11) {
                                    ((Ch_11) ((Expert_Match) match).getCard()[i]).setStudents(classroom);
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                    view.printNotification("Il player "+u+" ha giocato la carta personaggio 11");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyCh_10":
                            for (int i = 0; i < classroom.size(); i++) {
                                for (Player player : match.getPlayer()) {
                                    if (player.getUserName().equals(u)) {
                                        player.getBoard().ch_10_effect(classroom.get(i), type.get(i));
                                    }
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_10) {
                                    for (Player player : match.getPlayer()) {
                                        if (player.getUserName().equals(u)) {
                                            ((Board_Experts) player.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            action.checkAllProfessors();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                    view.printNotification("Il player "+u+" ha giocato la carta personaggio 10");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyCh_12":
                            for (Player pa:match.getPlayer()) {
                                pa.getBoard().ch_12_effect(ty);
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_12){
                                    for (Player pa:match.getPlayer()) {
                                        if(pa.getUserName().equals(u)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printMatch(match);
                                    view.printNotification("Il player "+u+" ha giocato la carta personaggio 12");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyCh_8":
                            for (int i = 0; i < 3; i++) {
                                if(((Expert_Match)match).getCard()[i] instanceof Ch_8){
                                    for (Player pa:match.getPlayer()) {
                                        if(pa.getUserName().equals(u)){
                                            ((Board_Experts)pa.getBoard()).subCoin(((Expert_Match)match).getCard()[i].getPrice());
                                            ((Expert_Match)match).getCard()[i].setActivated();
                                        }
                                    }
                                }
                            }
                            Platform.runLater(new Runnable() {
                            @Override
                                public void run() {
                                    view.printNotification("Il player " + u + " ha giocato la carta personaggio 8");
                                }
                            });
                            server.sendACK();
                            break;
                        case "NotifyThreeArchipelagos":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification("Si sono formati tre gruppi di isole");
                                }
                            });
                            break;
                    }
                }
                synchronized (this) {
                    wait();
                }
                if (end)
                    break;
            }
        } catch (IOException e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
            view.printNotification("Non trovo il server\n"+e.getMessage());
                }
            });
        } catch (ClassNotFoundException e) {
            server.sendNACK();
        } catch (Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    view.printNotification("Errore interno: ");
                }
            });
            e.printStackTrace();
        }
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the ArrayList join and the ArrayList resume
     * @param join the games to join
     * @param resume the games to resume
     */
    public void setJoinandResume(ArrayList<String> join, ArrayList<String> resume) {
        this.join = join;
        this.resume = resume;
    }

    /**
     * Sets the wizard between the player can choose
     * @param willy the ArrayList of wizards
     */
    public void setWilly(ArrayList<Wizards> willy) {
        this.willy = willy;
    }

    /**
     * Sets the Match, match
     * @param match the Match
     */
    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * Sets the students in the clouds when a "RefillClouds" message is received
     */
    public void setStudentsClouds() {
        ArrayList<Student> studen;
        for (Cloud clo : match.getCloud()) {
            clo.clearStudents();
            clo.reset();
        }
        for (int i = 0; i < match.getPlayersNum(); i++) {
            try {
                studen = (ArrayList<Student>) in.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            match.getCloud()[i].setStudents(studen);
        }
    }

    /**
     * Sets the AssistantCards between which the player can choose when a "ChooseAssistant" message is received
     * @param cards the List of cards
     */
    public void setCards(List<AssistantCard> cards) {
        this.cards =(List<AssistantCard>) cards;
    }

    /**
     * Sets the chosen AssistantCard and the Player that choose it then a "NotifyChosenCard" mesage is received
     * @param card the chosen card
     * @param pl2
     */
    public void setChosenCard(AssistantCard card, Player pl2){
        this.card=card;
        this.pl2=pl2;
    }

    /**
     *Sets the parameters that we use in this class when a "NotifyMovedStudent(id)" message is received
     * @param stu the moved student
     * @param id the id of the land in which he is moved
     * @param use the username of the player that made this choice
     */
    public void setNotifyMovedStudentId(Student stu, int id, String use){
        this.stu=stu;
        this.id=id;
        this.use=use;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyMovedStudent(board)" message is received
     * @param s the moved student
     * @param usern the username of the player that made this choice
     */
    public void setNotifyMovedStudentBoard(Student s, String usern){
        this.s=s;
        this.usern=usern;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyMovementMN" message is received
     * @param movement the number of steps of Mother Nature
     * @param lands the land at the end of the movement od Mother Nature
     */
    public void setNotifyMovementMN(int movement, ArrayList<Land> lands){
        this.movement=movement;
        this.lands=lands;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyProfessors" message is received
     * @param prof the new gained professor
     */
    public void setProf(Map<Type_Student,Player> prof){
        this.prof=prof;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyChosenCloud" message is received
     * @param p the player that chooses the cloud
     * @param cl the chosen cloud
     */
    public void setNotifyChosenCLoud(Player p, Cloud cl){
        this.p=p;
        this.cl=cl;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyTowers (land)" message is received
     * @param towers the ArryList of towers that are moving
     * @param land the land in which they have to end
     * @param f the username of the player that made the choice
     */
    public void setNotifyTowersLand(ArrayList<Tower> towers, Land land, String f){
        this.towers=towers;
        this.land=land;
        this.f=f;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyTowers (board)" message is received
     *
     * @param board the board in wich they have to end
     * @param u the username of the player that made the choice
     */
    public  void setNotifyTowersBoard( Board board, String u){
        this.board=board;
        this.u=u;
    }

    /**
     * Sets the parameters that we use in this class when a "NextTurn" message is received
     * @param play the player that has to start the turn
     * @param phase the phase of the fame the payer has to start
     */
    public void setNextTurn(Player play, String phase){
        this.play=play;
        this.phase=phase;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyPlayerConnected" message is received
     * @param n the message to print
     * @param connected the boolean parameter that tells if it is connected or not
     */
    public void setNotifyPlayerConnected(String n, boolean connected){
        this.n=n;
        this.connected=connected;
    }

    /**
     * Sets the parameters that we use in this class when a "FinishedAssistants" message is received
     * @param finish the player that has finished the AssistantCards
     */
    public void setFinish(Player finish){
        this.finish=finish;
    }

    /**
     * Sets the parameters that we use in this class when a "GenericError" message is received
     * @param error the message to print
     */
    public void setError(String error){
        this.error=error;
    }

    /**
     * Sets the parameters that we use in this class when a "Ch" message is received
     * @param ch the CharacterCards updated
     */
    public void setCh(CharacterCard[] ch){
        this.ch=ch;
        view.setCharacters(ch);
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_1" message is received
     * @param l the land interested
     * @param s the List of students
     * @param stu the chosen student
     * @param user the username of the player that played the card
     */
    public void setCh_1(Land l, List<Student> s, Student stu, String user){
        this.land=l;
        this.ss=s;
        this.stu=stu;
        this.u=user;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_2" message is received
     * @param profs the List of professors to use
     * @param u the username of the player that played the card
     */
    public void setCh_2(Map<Type_Student,Player>profs, String u){
        this.profs=profs;
        this.u=u;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_4" message is received
     * @param u the username of the player that played the card
     */
    public void setCh_4(String u){
        this.u=u;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_5" message is received
     * @param lala the land chosen
     * @param name the username of the player that played the card
     */
    public void setCh_5(Land lala, String name){
        this.land=lala;
        this.u=name;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_10" message is received
     * @param neim the username of the player that played the card
     * @param classroom the ArrayList of the chosen students
     * @param type the ArrayList of the chosen type of students
     */
    public void setCh_10(String neim, ArrayList<Student> classroom, ArrayList<Type_Student> type){
        this.u=neim;
        this.classroom=classroom;
        this.type=type;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_11" message is received
     * @param card the ArrayList of the chosen students
     * @param n the username of the player that played the card
     * @param ss the chosen student
     */
    public void setCh_11( ArrayList<Student> card, String n, Student ss){
        this.classroom=card;
        this.u=n;
        this.s=ss;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_12" message is received
     * @param ty the chose type of students
     * @param usrnm the username of the player that played the card
     */
    public void setCh_12(Type_Student ty, String usrnm){
        this.ty=ty;
        this.u=usrnm;
    }

    /**
     * Sets the parameters that we use in this class when a "NotifyCh_8" message is received
     * @param usr the username of the player that played the card
     */
    public void setCh_8(String usr){
        this.u=usr;
    }

    /**
     * Gets the boolean that tells if the player has chosen to use or not the CharacterCard
     * @return the boolean value
     */
    public boolean getNoCh(){
        return noch;
    }

    /**
     * Sets the boolean that tells if the player has chosen to use or not the CharacterCard
     * @param v the boolean value
     */
    public void setNoCh(boolean v){
        this.noch=v;
    }

    /**
     * Sets the parameters that we use in this class when a "EndGame" message is received
     * @param winner the winner player
     * @param explanation the explaination of his win
     * @param gameRecap the parameter GameRecap
     */
    public void setEndGame(Player winner, String explanation,GameRecap gameRecap){
        this.p=winner;
        this.n=explanation;
        this.gr=gameRecap;
    }

    /**
     * Sets the parameters that we use in this class when a "LastTower" message is received
     * @param p the player that played his last tower
     */
    public void setLastTower(Player p){
        this.p=p;
    }

}