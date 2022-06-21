package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characterCards.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            System.out.println("Client: Inizializzato");
            byte[] buf = new byte[1];
            starting= new DatagramPacket(buf, 0, buf.length, InetAddress.getByAddress(addr), 4898);
            do { //Ho messo il timeout per la ricezione dei messaggi
                dSokk.send(starting);
                System.out.println("Client: Ho mandato richiesta, ora vediamo di ricevere...");
                buf = new byte[1];
                packet = new DatagramPacket(buf, buf.length);
                try{
                    dSokk.receive(packet);
                }
                catch (SocketTimeoutException e){
                    counter ++;
                    if(counter==3){
                        System.out.println("Connessione fallita. Far ripartire il client"); //fai eccezione
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
            view.getTitolo();
            while (true){
                if(received!="base") {
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
                            System.out.println(response);
                        } while(response.equals("LoginFailed"));
                        received="ok";
                        break;
                    case "ListOfGames":
                        ArrayList<String> join=new ArrayList<>();
                        join=(ArrayList<String>) in.readObject();
                        ArrayList<String> resume=new ArrayList<>();
                        resume=(ArrayList<String>) in.readObject();
                        view.chooseMatch(join,resume);
                    case "ACK":
                        //view.wakeUp("MoveStudents");
                        break;
                    case  "NACK":
                        view.setNack();
                        break;
                    case "Wizard":
                        List<Wizards> willy;
                        willy = (ArrayList<Wizards>)in.readObject();
                        view.setWilly(willy);
                        view.wakeUp(received);
                        break;
                    case "Creation":
                        match=(Match) in.readObject();
                        action=new Action(match);
                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if(match.getPlayer()[i].getUserName().equals(username))
                                me=match.getPlayer()[i];
                        }
                        view.setMe(me);
                        view.setMatch(match);
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "RefillClouds":
                        ArrayList<Cloud> receivedClouds=new ArrayList<>();
                        Cloud c;
                        ArrayList<Student> studen;
                        for(Cloud clo: match.getCloud()){
                            clo.clearStudents();
                        }
                        //receivedClouds = (ArrayList<Cloud>) in.readObject();
                        for(int i=0; i<match.getPlayersNum(); i++) {
                            studen = (ArrayList<Student>) in.readObject();
                            System.out.println(studen.toString());
                            /*System.out.println("Ecco la nuvola n "+i+":");
                            System.out.println(c.toString());
                            System.out.println(c.getStudents().size());
                            receivedClouds.add(c);*/
                            match.getCloud()[i].setStudents(studen);
                        }
                        //Cloud[] clo=new Cloud[receivedClouds.size()];

                        //for(Cloud i: receivedClouds){
                            //System.out.println(i.toString());
                        //}
                        //match.setCloud(receivedClouds.toArray(new Cloud[0]));

                        view.printMatch(match);
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
                        view.wakeUp(received);
                        break;
                    //nella nuova versione non è previsto ACK o NACK
                    case "ChooseCloud":
                        List<Cloud> clouds;
                        clouds = (ArrayList<Cloud>) in.readObject();
                        view.setClouds(Arrays.asList(match.getCloud()));
                        view.wakeUp("ChooseCloud");
                        //server.sendChoiceCloud(cl);
                        break;
                    case "NotifyChosenCard":
                        AssistantCard card=(AssistantCard) in.readObject();
                        Player pl2=(Player) in.readObject();
                        System.out.println(pl2.getUserName()+" ha giocato "+card.toString());

                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if(match.getPlayer()[i].getUserName().equals(pl2.getUserName())){
                                match.getPlayer()[i].draw(card);
                            }
                        }
                        server.sendACK();
                        break;
                    case "NotifyMoveStudents (id)":
                        Student stu=(Student) in.readObject(); //lo studente stesso
                        int id=(int)in.readObject(); //id della Land
                        String user=(String) in.readObject();
                        for (Land e:match.getLands()) {
                            if(id==e.getID())
                                e.addStudent(stu);
                        }
                        for (int i=0;i<match.getPlayer().length;i++) {
                            if(match.getPlayer()[i].getUserName().equals(user)) {
                                match.getPlayer()[i].getBoard().removeStudent(stu);
                            }
                        }
                        action.checkAllProfessors();
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "NotifyMoveStudents (board)":
                        Student s=(Student) in.readObject(); //lo studente stesso
                        Board b=(Board) in.readObject();
                        String usern=(String) in.readObject();
                        for (int i=0;i<match.getPlayer().length;i++) {
                            if(match.getPlayer()[i].getUserName().equals(usern)) {
                                match.getPlayer()[i].getBoard().placeStudent(s);
                            }
                        }
                        action.checkAllProfessors();
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "NotifyMovementMN":
                        int movement=(int)in.readObject();
                        int idLand;
                        ArrayList<Land> lands=(ArrayList<Land>) in.readObject();
                        System.out.println(lands);
                        match.moveMotherNature(movement);
                        idLand=match.getMotherNature().getPosition().getID();
                        tow=match.getMotherNature().getPosition().size();
                        match.setLands(lands);
                        for (Land e:match.getLands()) {
                            if(e.getID()==idLand){
                                match.getMotherNature().setPosition(e);
                            }
                        }
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "NotifyProfessors":
                        Map<Type_Student, Player> prof=(Map<Type_Student, Player>) in.readObject();
                        match.setProfessors(prof);
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "NotifyChosenCloud":
                        Player p=(Player) in.readObject();
                        Cloud cl=(Cloud) in.readObject();
                        for (int j=0;j< match.getPlayersNum();j++) {
                            if(match.getPlayer()[j].getUserName().equals(p.getUserName())){
                                p.getBoard().importStudents(cl.getStudents());
                            }
                        }
                        for (Cloud e:match.getCloud()) {
                            if(cl.equals(e)){
                                e.clearStudents();
                                break;
                            }
                        }
                        view.printMatch(match);
                        server.sendACK();
                        break;
                    case "NotifyTowers (land)":
                        ArrayList<Tower> towers=(ArrayList<Tower>) in.readObject();
                        Land land=(Land) in.readObject();
                        String f=(String) in.readObject();
                        for (Land e: match.getLands()) {
                            for (int j=0;j< match.getPlayersNum();j++) {
                                if(e.getID()==land.getID() && towers.get(0).getColor().equals(match.getPlayer()[j].getColor())){
                                    ArrayList<Tower> tower=new ArrayList<>();
                                    for (int i = 0; i < tow; i++) {
                                        tower.add(match.getPlayer()[j].getBoard().removeTower());
                                    }
                                    for (int i = tow; i < e.size(); i++) {
                                        tower.add(new Tower(match.getPlayer()[j].getColor(),match.getPlayer()[j].getBoard()));
                                    }
                                    e.changeTower(tower);
                                    if(tow<e.size()){
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
                        ArrayList<Tower> towers1=(ArrayList<Tower>) in.readObject();
                        Board board=(Board) in.readObject();
                        String us=(String) in.readObject();
                        for (int i=0;i<match.getPlayer().length;i++) {
                            if(match.getPlayer()[i].getUserName().equals(us))
                                match.getPlayer()[i].setBoard(board);
                        }
                        server.sendACK();
                        break;
                    case "EndGame":
                        Player winner=(Player) in.readObject();
                        String ex=(String) in.readObject(); //spiegazione di perchè ha vinto
                        ArrayList<Land> landd=(ArrayList<Land>) in.readObject(); //situa finale lands
                        ArrayList<Board> boards=(ArrayList<Board>) in.readObject(); //situa di tutte le board
                        view.getWinner(winner);
                        match.setLands(landd);
                        for (int i = 0; i < match.getPlayer().length; i++) {
                            match.getPlayer()[i].setBoard(boards.get(i));
                        }
                        action.checkAllProfessors();
                        view.printMatch(match);
                        view.wakeUp("EndGame");
                        end=true;
                        server.sendACK();
                        break;
                    case "LastTower":
                        Player pl=(Player) in.readObject();
                        view.getWinner(pl);
                        view.wakeUp("EndGame");
                        end=true;
                        server.sendACK();
                        break;
                    case "NoMoreStudents":
                        view.lastRound();
                        server.sendACK();
                        break;
                    case "NextTurn":
                        Player play=(Player) in.readObject();
                        String phase=(String)in.readObject();
                        view.printTurn(play,phase);
                        server.sendACK();
                        System.out.println("Mandato ack");
                        break;
                    case "Ping":
                        server.sendPONG();
                        break;
                    case "NotifyPlayerConnected":
                        String u=(String) in.readObject();
                        boolean connected=(boolean) in.readObject();
                        if(connected){
                            view.playerConnected(u);
                        }else
                        {
                            view.playerDisconnected(u);
                        }
                        server.sendACK();
                        break;
                    case "NotifyAllPlayersDisconnected":
                        view.playerDisconnectedAll();
                        server.sendACK();
                        break;
                    case "FinishedAssistants":
                        Player who=(Player) in.readObject();
                        view.finishedAC(who);
                        server.sendACK();
                        break;
                    case "GenericError":
                        String error= (String) in.readObject();
                        System.out.println(error);
                        server.sendACK();
                        break;
                    case "Ch":
                        CharacterCard ch[]=(CharacterCard[])in.readObject();
                        view.setCharacters(ch);
                        view.wakeUp("Ch");
                        /*switch (n){
                            case 1:
                                Ch_1 c=(Ch_1) in.readObject();
                                break;
                            case 2:
                                Ch_2 ca=(Ch_2)in.readObject();
                                break;
                            case 4:
                                Ch_4 car=(Ch_4)in.readObject();
                                break;
                            case 5:
                                Ch_5 cara=(Ch_5)in.readObject();
                                break;
                            case 7:
                                Ch_7 carac=(Ch_7)in.readObject();
                                break;
                            case 10:
                                Ch_10 caract=(Ch_10)in.readObject();
                                break;
                            case 11:
                                Ch_11 caracte=(Ch_11)in.readObject();
                                break;
                            case 12:
                                Ch_12 caracter=(Ch_12)in.readObject();
                                break;
                        }*/
                        break;
                    default: server.sendNACK();
                }
                if(end)
                    break;
            }
        } catch (IOException e) {
            System.out.println("Non trovo il server");
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
