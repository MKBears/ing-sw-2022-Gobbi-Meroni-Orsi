package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;
import it.polimi.ingsw.serverController.GameRecap;

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
public class Client  extends Thread{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DatagramSocket dSokk;
    private byte[] addr;
    private DatagramPacket starting;
    private DatagramPacket packet;
    private boolean condition;
    private String received;
    private String send;
    private String response;
    private Message4Server server;
    private Match match;
    private View view;
    private Action action;
    private String username;
    private Player me;
    private boolean end;
    private int counter;
    private Boolean nack;
    private int tow;

    /**
     * Constructor of the class Client
     * @param view the instance of the view (it can be Cli or Gui)
     */
    public Client(View view) {
        match=null;
        username=null;
        me=null;
        end=false;
        counter=0;
        this.view = view;
    }

    public void run(){

        try {
            nack=false;
            condition=false;
            addr= InetAddress.getLocalHost().getAddress();
            addr[3]=(byte)255;
            dSokk=new DatagramSocket();
            dSokk.setSoTimeout(5000);
            //System.out.println("Client: Inizializzato");
            byte[] buf = new byte[1];
            starting= new DatagramPacket(buf, 0, buf.length, InetAddress.getByAddress(addr), 4898);
            do { //Ho messo il timeout per la ricezione dei messaggi
                dSokk.send(starting);
                //System.out.println("Client: Ho mandato richiesta, ora vediamo di ricevere...");
                buf = new byte[1];
                packet = new DatagramPacket(buf, buf.length);
                try{
                    dSokk.receive(packet);
                }
                catch (SocketTimeoutException e){
                    counter ++;
                    if(counter==3){
                        view.printNotification("Connessione fallita. Far ripartire il client");
                        dSokk.close();
                        throw new RuntimeException();
                    }
                }
                if(packet.getData()[0]==1){ //inizializza a 1 nel server
                    condition=true;
                }
            }while(!condition);

            InetAddress ip= packet.getAddress();
            int port= 2836;
            socket= new Socket(ip.getHostAddress(),port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in= new ObjectInputStream(socket.getInputStream());
            server=new Message4Server(out);
            view.setServer(server);
            received="base";
            dSokk.close();
            view.getTitolo();
            while (true){
                if(!received.equals("base")) {
                    received = (String) in.readObject();
                }
                if(!received.equals("Ping")) {
                    System.out.println("Ricevuto: " + received);
                }
                switch (received) {
                    case "base": //login
                        do {
                            if (view.chooseLogin().equals("si")) {
                                username = view.getUsername();
                                server.sendRegistration(username);
                            } else {
                                username = view.getUsername();
                                server.sendLogin(username);
                            }//Nella view facciamo due pulsanti: nuovo account o accedi al tuo account, in base a ciò decide il server se la login è succeeded o failed

                            response = (String) in.readObject();
                            view.printNotification(response);
                        } while (response.equals("LoginFailed"));
                        received = "ok";
                        break;
                    case "ListOfGames":
                        ArrayList<String> join;
                        join = (ArrayList<String>) in.readObject();
                        ArrayList<String> resume;
                        resume = (ArrayList<String>) in.readObject();
                        view.chooseMatch(join, resume);
                    case "ACK":
                        //view.wakeUp("MoveStudents");
                        break;
                    case "NACK":
                        view.setNack();
                        break;
                    case "Wizard":
                        List<Wizards> willy;
                        willy = (ArrayList<Wizards>) in.readObject();
                        view.setWilly(willy);
                        view.wakeUp(received);
                        break;
                    case "Creation":
                        match = (Match) in.readObject();
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
                        ArrayList<Student> studen;
                        for (Cloud clo : match.getCloud()) {
                            clo.clearStudents();
                            clo.reset();
                        }
                        for (int i = 0; i < match.getPlayersNum(); i++) {
                            studen = (ArrayList<Student>) in.readObject();
                            match.getCloud()[i].setStudents(studen);
                        }
                        server.sendACK();
                        break;
                    case "ChooseCard":
                        List<AssistantCard> cards;
                        cards = (ArrayList<AssistantCard>) in.readObject();
                        view.setCards(cards);
                        view.wakeUp("ChooseCard");
                        break;
                    case "MoveStudents":
                    case "MoveMN": //DA MODIFICARE IL PROTOCOLLO
                        System.out.println("Ricevuto " + received);
                        view.wakeUp(received);
                        System.out.println("View svegliata");
                        break;
                    //nella nuova versione non è previsto ACK o NACK
                    case "ChooseCloud":
                        List<Cloud> clouds;
                        clouds = (ArrayList<Cloud>) in.readObject();
                        view.setClouds(clouds);
                        view.wakeUp("ChooseCloud");
                        break;
                    case "NotifyChosenCard":
                        AssistantCard card = (AssistantCard) in.readObject();
                        Player pl2 = (Player) in.readObject();
                        view.printNotification(pl2.getColor() + pl2.getUserName() +
                                "\u001b[0m ha giocatola carta:" + card.toString());

                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if (match.getPlayer()[i].getUserName().equals(pl2.getUserName())) {
                                match.getPlayer()[i].draw(card);
                            }
                        }
                        server.sendACK();
                        break;
                    case "NotifyMoveStudents (id)":
                        Student stu = (Student) in.readObject(); //lo studente stesso
                        int id = (int) in.readObject(); //id della Land
                        String user = (String) in.readObject();
                        for (Land e : match.getLands()) {
                            if (id == e.getID())
                                e.addStudent(stu);
                        }
                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if (match.getPlayer()[i].getUserName().equals(user)) {
                                match.getPlayer()[i].getBoard().removeStudent(stu);
                            }
                        }
                        action.checkAllProfessors();
                        view.printNotification(user + " ha spostato lo studente " + stu.toString() + " nell'isola " + id);
                        server.sendACK();
                        break;
                    case "NotifyMoveStudents (board)":
                        Student s = (Student) in.readObject(); //lo studente stesso
                        Board b = (Board) in.readObject();
                        String usern = (String) in.readObject();
                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if (match.getPlayer()[i].getUserName().equals(usern)) {
                                match.getPlayer()[i].getBoard().placeStudent(s);
                            }
                        }
                        action.checkAllProfessors();
                        view.printNotification(usern + " ha spostato lo studente " + s.toString() + " nella sua sala");
                        server.sendACK();
                        break;
                    case "NotifyMovementMN":
                        int movement = (int) in.readObject();
                        int idLand;
                        match.moveMotherNature(movement);
                        idLand = match.getMotherNature().getPosition().getID();
                        ArrayList<Land> lands = (ArrayList<Land>) in.readObject();
                        //System.out.println(lands);
                        match.setLands(lands);
                        for (Land l : match.getLands()) {
                            if (l.getID() == match.getMotherNature().getPosition().getID())
                                match.getMotherNature().setPosition(l);
                        }
                        if (match.getMotherNature().getPosition().isThereNoEntry()) {
                            match.getMotherNature().getPosition().setNoEntry(false);
                        }
                        view.printNotification("Madre Natura é stata spostata di " + movement
                                + " passi nell'isola " + idLand);
                        server.sendACK();
                        break;
                    case "NotifyProfessors":
                        Map<Type_Student, Player> prof = (Map<Type_Student, Player>) in.readObject();
                        match.setProfessors(prof);
                        server.sendACK();
                        break;
                    case "NotifyChosenCloud":
                        Player p = (Player) in.readObject();
                        Cloud cl = (Cloud) in.readObject();
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
                        view.printNotification(p.getColor().toString() + p.getUserName()
                                + "\u001b[0m ha scelto la nuvola:\n" + cl.toString());
                        server.sendACK();
                        break;
                    case "NotifyTowers (land)":
                        ArrayList<Tower> towers = (ArrayList<Tower>) in.readObject();
                        Land land = (Land) in.readObject();
                        String f = (String) in.readObject();
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
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "NotifyTowers (board)":
                        ArrayList<Tower> towers1 = (ArrayList<Tower>) in.readObject();
                        Board board = (Board) in.readObject();
                        String us = (String) in.readObject();
                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if (match.getPlayer()[i].getUserName().equals(us))
                                match.getPlayer()[i].setBoard(board);
                        }
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "EndGame":
                        Player winner = (Player) in.readObject();
                        String ex = (String) in.readObject(); //spiegazione di perchè ha vinto
                        GameRecap recap = (GameRecap) in.readObject();
                        view.getWinner(winner);
                        view.printMatch(match);
                        view.printNotification(recap.toString());
                        view.wakeUp("EndGame");
                        end = true;
                        server.sendACK();
                        break;
                    case "LastTower":
                        Player pl = (Player) in.readObject();
                        view.getWinner(pl);
                        view.wakeUp("EndGame");
                        end = true;
                        server.sendACK();
                        break;
                    case "NoMoreStudents":
                        view.lastRound();
                        server.sendACK();
                        break;
                    case "NextTurn":
                        Player play = (Player) in.readObject();
                        String phase = (String) in.readObject();
                        view.printTurn(play, phase);
                        server.sendACK();
                        //System.out.println("Mandato ack");
                        break;
                    case "Ping":
                        server.sendPONG();
                        break;
                    case "NotifyPlayerConnected":
                        String u = (String) in.readObject();
                        boolean connected = (boolean) in.readObject();
                        if (connected) {
                            view.playerConnected(u);
                        } else {
                            view.playerDisconnected(u);
                        }
                        server.sendACK();
                        break;
                    case "NotifyAllPlayersDisconnected":
                        view.playerDisconnectedAll();
                        server.sendACK();
                        break;
                    case "FinishedAssistants":
                        Player who = (Player) in.readObject();
                        view.finishedAC(who);
                        server.sendACK();
                        break;
                    case "GenericError":
                        String error = (String) in.readObject();
                        view.printNotification(error);
                        server.sendACK();
                        break;
                    case "Ch":
                        CharacterCard[] ch = (CharacterCard[]) in.readObject();
                        for (int i = 0; i < 3; i++) {
                            if (ch[i] instanceof Ch_11) {
                                System.out.println(((Ch_11) ch[i]).getStudents());
                            }
                        }
                        view.setCharacters(ch);
                        view.wakeUp("Ch");
                        break;
                    case "NotifyCh_1":
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
                        view.printNotification("Il player [nome] ha giocato la carta personaggio 1");
                        server.sendACK();
                        break;
                    case "NotifyCh_2":
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
                    case "NotifyCh_4":
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
                    case "NotifyCh_5":
                        Land land2 = (Land) in.readObject();
                        String us5=(String) in.readObject();
                        for (Land l : match.getLands()) {
                            if (l.getID() == land2.getID()) {
                                l.setNoEntry(true);
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
                    case "NotifyCh_10":
                        String usa = (String) in.readObject();
                        ArrayList<Student> studes = (ArrayList<Student>) in.readObject();
                        ArrayList<Type_Student> types = (ArrayList<Type_Student>) in.readObject();
                        for (int i = 0; i < 2; i++) {
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
                        view.printMatch(match);
                        view.printNotification("giocatore "+usa+" ha giocato la carta personaggio 10");
                        server.sendACK();
                        break;
                    case "NotifyCh_11":
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
                        for (int i = 0; i < 3; i++) {
                            if(((Expert_Match)match).getCard()[i] instanceof Ch_11) {
                                ((Ch_11) ((Expert_Match) match).getCard()[i]).setStudents(ch11);
                            }
                        }
                        view.printMatch(match);
                        view.printNotification("Il player "+usernam+" ha giocato la carta personaggio 11");
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
                    default: server.sendNACK();
                }
                if(end)
                    break;
            }
        } catch (IOException e) {
            view.printNotification("Non trovo il server\n"+e.getMessage());
        } catch (ClassNotFoundException e) {
            server.sendNACK();
        } catch (Exception e) {
            view.printNotification("Errore interno: ");
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

}
