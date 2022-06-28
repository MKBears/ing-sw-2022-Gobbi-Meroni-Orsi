package it.polimi.ingsw.client;

import it.polimi.ingsw.client.guiControllers.LoginController;
import it.polimi.ingsw.client.guiControllers.MatchController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;
import it.polimi.ingsw.serverController.GameRecap;
import javafx.application.Platform;
import javafx.scene.input.SwipeEvent;

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
    }

    public Gui getView(){
        return view;
    }

    public void setUsername(String username) {
        this.username = username;
        System.out.println("Ho aggiornato lo username: " + username);
    }

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
            System.out.println("Client inizializzato");
            byte[] buf = new byte[1];
            DatagramPacket starting = new DatagramPacket(buf, 0, buf.length, InetAddress.getByAddress(addr), 4898);
            DatagramPacket packet;
            System.out.println("Connessione in corso");
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
            //sleep(2000);
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
                /*if(!received.equals("base")) {
                    received = (String) in.readObject();
                }*/
                synchronized (in) {
                    System.out.println(received);
                    switch (received) {
                        case "base":
                            do {
                                //sleep(2000);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.getUsername();
                                    }
                                });
                                synchronized (this) {
                                    this.wait();
                                }
                                System.out.println("Ho lo username: " + username);
                                //username= view.getUs();
                                response = (String) in.readObject();
                                System.out.println("Ricevuto in CG: " + response);
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.printNotification(response);
                                    }
                                });
                                //this.sleep(2000);
                            } while (response.equals("LoginFailed"));
                            received = "ok";
                            mfs = new MessageFromServer(in, server, this);
                            mfs.start();
                            /*synchronized (this){
                                this.wait();
                            }*/
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
                            //view.wakeUp("MoveStudents");
                            break;
                        case "NACK":
                            //view.setNack();
                            break;
                        case "Wizard":
                            System.out.println("Sono in WIZARD");
                            view.setWilly(willy);  //un po' inutile
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.getWizard(willy);//manda lui il wizard scelto
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
                            }
                            view.setMe(me);
                            view.setMatch(match);
                            if (match instanceof Expert_Match)
                                view.setCharacters(((Expert_Match) match).getCard());
                            server.sendACK();
                            break;
                        case "RefillClouds":
                            //if(!pm){
                            Platform.runLater(new Runnable() {
                                @Override
                            public void run() {
                                    view.printMatch(match);
                                }
                            });

                            //synchronized (this) {
                            //    this.wait();
                            //}
                            sleep(3500);
                            server.sendACK();
                            //pm=false;
                            //}
                            break;
                        case "ChooseCard":
                            view.setCards(cards);
                            //System.out.println("Sono in ChooseAssistant");
                            //synchronized (in) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Platform.setImplicitExit(false);
                                        //System.out.println("Sono nella run si ChooseCard");
                                        view.getAssistantCard();
                                    }
                                });
                            //}
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
                                }
                            });
                            synchronized (this) {
                                this.wait();
                            }
                            break;
                        case "MoveMN":
                            System.out.println("Ricevuto " + received);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.moveMN();
                                }
                            });
                            synchronized (this) {
                                this.wait();
                            }
                            //System.out.println("View svegliata");
                            break;
                        //nella nuova versione non è previsto ACK o NACK
                        case "ChooseCloud":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.getCloud();
                                }
                            });
                            synchronized (this) {
                                this.wait();
                            }
                            break;
                        case "NotifyChosenCard":
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(pl2.getUserName() +
                                            " ha giocato la carta:" + card.getValue());
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
                                    //view.clearStudentFromBoard(stu.type());
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
                            //System.out.println(lands);
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
                                            + " passi nell'isola " + idLand);
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
                            view.printMatch(match);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
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
                        case "EndGame": //da mettere a posto
                            Player winner = (Player) in.readObject();
                            String ex = (String) in.readObject(); //spiegazione di perchè ha vinto
                            GameRecap recap = (GameRecap) in.readObject();
                            view.getWinner(winner);
                            view.printMatch(match);
                            view. printNotification(winner.getColor()+winner.getUserName()+" ha vinto perché "+ex);
                            view.printNotification(recap.toString());
                            sleep(5000);
                            view.wakeUp("EndGame");
                            end = true;
                            server.sendACK();
                            break;
                        case "LastTower":  //da mettere a posto
                            Player pl = (Player) in.readObject();
                            view.getWinner(pl);
                            view.wakeUp("EndGame");
                            end = true;
                            server.sendACK();
                            break;
                        case "NoMoreStudents": //da mettere a posto
                            view.lastRound();
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
                            //System.out.println("Mandato ack");
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
                        case "FinishedAssistants": //da mettere a posto
                            Player who = (Player) in.readObject();
                            view.finishedAC(who);
                            server.sendACK();
                            break;
                        case "GenericError":  //da mettere a posto
                            String error = (String) in.readObject();
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    view.printNotification(error);
                                }
                            });
                            server.sendACK();
                            break;
                        case "Ch": //da mettere a posto
                            CharacterCard[] ch = (CharacterCard[]) in.readObject();
                            view.setCharacters(ch);
                            view.wakeUp("Ch");
                            break;
                        case "NotifyCh_1": //da mettere a posto
                            Land land3 = (Land) in.readObject();
                            ArrayList<Student> c1 = (ArrayList<Student>) in.readObject();
                            Student s2 = (Student) in.readObject();
                            String us6=(String) in.readObject();
                            for (int i = 0; i < 3; i++) {
                                if (((Expert_Match) match).getCard()[i] instanceof Ch_1)
                                    ((Ch_1) ((Expert_Match) match).getCard()[i]).setStudents(c1);
                            }
                            for (Land l : match.getLands()) {
                                if (l.getID() == land3.getID()) {
                                    l.addStudent(s2);
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_1) {
                                    for (Player player : match.getPlayer()) {
                                        if (player.getUserName().equals(us6)) {
                                            ((Board_Experts) player.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            view.printMatch(match);
                            view.printNotification("Il player "+us6+" ha giocato la carta personaggio 1");
                            server.sendACK();
                            break;
                        case "NotifyCh_2": //da mettere a posto
                            Map<Type_Student, Player> pro = (Map<Type_Student, Player>) in.readObject();
                            String use = (String) in.readObject();
                            match.setProfessors(pro);
                            for (Player e : match.getPlayer()) {
                                if (e.getUserName().equals(use)) {
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
                                        if(pa.getUserName().equals(use)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            view.printMatch(match);
                            view.printNotification("Il player " + use + " ha giocato la carta personaggio 2");
                            server.sendACK();
                            break;
                        case "NotifyCh_4": //da mettere a posto
                            String userna = (String) in.readObject();
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_4){
                                    for (Player pa: match.getPlayer()) {
                                        if(pa.getUserName().equals(userna)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            view.printNotification("giocatore " + userna + " ha giocato la carta personaggio 4");
                            server.sendACK();
                            break;
                        case "NotifyCh_5": //da mettere a posto
                            Land land2 = (Land) in.readObject();
                            String us5=(String) in.readObject();
                            if (!me.getUserName().equals(us5)) {
                                for (Land l : match.getLands()) {
                                    if (l.getID() == land2.getID()) {
                                        l.setNoEntry(true);
                                    }
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_5) {
                                    for (Player player : match.getPlayer()) {
                                        if (player.getUserName().equals(us5)) {
                                            ((Board_Experts) player.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            view.printMatch(match);
                            view.printNotification("Il player "+us5+" ha giocato la carta personaggio 5");
                            server.sendACK();
                            break;
                        case "NotifyCh_11": //da mettere a posto
                            ArrayList<Student> ch11=(ArrayList<Student>) in.readObject();
                            String usernam=(String) in.readObject();
                            Student s3=(Student) in.readObject();
                            for (Player player:match.getPlayer()) {
                                if(player.getUserName().equals(usernam)){
                                    player.getBoard().ch_11_effect(s3);
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_11){
                                    for (Player pa:match.getPlayer()) {
                                        if(pa.getUserName().equals(usernam)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            match.checkProfessor(s3.type());
                            for (int i = 0; i < 3; i++) {
                                if(((Expert_Match)match).getCard()[i] instanceof Ch_11) {
                                    ((Ch_11) ((Expert_Match) match).getCard()[i]).setStudents(ch11);
                                }
                            }
                            view.printMatch(match);
                            view.printNotification("Il player "+usernam+" ha giocato la carta personaggio 11");
                            server.sendACK();
                            break;
                        case "NotifyCh_10": //da mettere a posto
                            String usa = (String) in.readObject();
                            ArrayList<Student> studes = (ArrayList<Student>) in.readObject();
                            ArrayList<Type_Student> types = (ArrayList<Type_Student>) in.readObject();
                            for (int i = 0; i < studes.size(); i++) {
                                for (Player player : match.getPlayer()) {
                                    if (player.getUserName().equals(usa)) {
                                        player.getBoard().ch_10_effect(studes.get(i), types.get(i));
                                    }
                                }
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_10) {
                                    for (Player player : match.getPlayer()) {
                                        if (player.getUserName().equals(usa)) {
                                            ((Board_Experts) player.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            action.checkAllProfessors();
                            view.printMatch(match);
                            view.printNotification("giocatore "+usa+" ha giocato la carta personaggio 10");
                            server.sendACK();
                            break;
                        case "NotifyCh_12":
                            Type_Student type=(Type_Student)in.readObject();
                            String s4=(String) in.readObject();
                            for (Player pa:match.getPlayer()) {
                                pa.getBoard().ch_12_effect(type);
                            }
                            for (CharacterCard c:((Expert_Match)match).getCard()) {
                                if(c instanceof Ch_12){
                                    for (Player pa:match.getPlayer()) {
                                        if(pa.getUserName().equals(s4)){
                                            ((Board_Experts)pa.getBoard()).subCoin(c.getPrice());
                                            c.setActivated();
                                        }
                                    }
                                }
                            }
                            view.printMatch(match);
                            view.printNotification("Il player "+s4+" ha giocato la carta personaggio 12");
                            server.sendACK();
                            break;
                        case "NotifyCh_8":
                            String us1=(String) in.readObject();
                            for (int i = 0; i < 3; i++) {
                                if(((Expert_Match)match).getCard()[i] instanceof Ch_8){
                                    for (Player pa:match.getPlayer()) {
                                        if(pa.getUserName().equals(us1)){
                                            ((Board_Experts)pa.getBoard()).subCoin(((Expert_Match)match).getCard()[i].getPrice());
                                            ((Expert_Match)match).getCard()[i].setActivated();
                                        }
                                    }
                                }
                            }
                            view.printNotification("Il player "+us1+" ha giocato la carta personaggio 12");
                            server.sendACK();
                            break;
                        //:
                        //server.sendNACK();
                    }
                }
                synchronized (this) {
                    System.out.println("In wait CG");
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

    public void setJoinandResume(ArrayList<String> join, ArrayList<String> resume) {
        this.join = join;
        this.resume = resume;
    }

    public void setWilly(ArrayList<Wizards> willy) {
        this.willy = willy;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

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

    public void setCards(List<AssistantCard> cards) {
        System.out.println("Sto settando le carte");
        this.cards =(List<AssistantCard>) cards;
    }

    public void setClouds(ArrayList<Cloud> clouds) {
        this.clouds=clouds;
    }

    public void setChosenCard(AssistantCard card, Player pl2){
        this.card=card;
        this.pl2=pl2;
    }

    public void setNotifyMovedStudentId(Student stu, int id, String use){
        this.stu=stu;
        this.id=id;
        this.use=use;
    }

    public void setNotifyMovedStudentBoard(Student s, Board b, String usern){
        this.s=s;
        this.b=b;
        this.usern=usern;
    }

    public void setNotifyMovementMN(int movement, ArrayList<Land> lands){
        this.movement=movement;
        this.lands=lands;
    }

    public void setProf(Map<Type_Student,Player> prof){
        this.prof=prof;
    }

    public void setNotifyChosenCLoud(Player p, Cloud cl){
        this.p=p;
        this.cl=cl;
    }

    public void setNotifyTowersLand(ArrayList<Tower> towers, Land land, String f){
        this.towers=towers;
        this.land=land;
        this.f=f;
    }

    public  void setNotifyTowersBoard(ArrayList<Tower> toewrs1, Board board, String u){
        this.towers1=toewrs1;
        this.board=board;
        this.u=u;
    }

    public void setNextTurn(Player play, String phase){
        this.play=play;
        this.phase=phase;
    }

    public void setNotifyPlayerConnected(String n, boolean connected){
        this.n=n;
        this.connected=connected;
    }

    public String getReceived(){
        return received;
    }
}