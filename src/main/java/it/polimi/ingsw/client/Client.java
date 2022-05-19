package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.serverController.Action;

public class Client{

    public void run(){
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;
        DatagramSocket dSokk;
        byte[] addr;
        DatagramPacket starting;
        DatagramPacket packet;
        boolean condition;
        String received;
        String send;
        String response;
        Message4Server server;
        Match match=null;
        Cli cli=new Cli();
        Action action=null;
        String username=null;
        Player me=null;
        Boolean end=false;
        int counter=0;
        MessageFromServer mfs;

        try {
            condition=false;
            addr= InetAddress.getLocalHost().getAddress();
            addr[3]=(byte)255;
            dSokk=new DatagramSocket();
            System.out.println("Client: Inizializzato");
            byte[] buf = new byte[1];
            starting= new DatagramPacket(buf, 0, buf.length, InetAddress.getByAddress(addr), 4898);
            do {
                dSokk.setSoTimeout(5000); //Ho messo il timeout per la ricezione dei messaggi
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
            out.writeObject("Prova prova 1 2 3");
            server=new Message4Server(in,out);
            mfs=new MessageFromServer(in,this);

            received="base1";
            while (true){
                if(received!="base1") {
                    received= mfs.message;
                }
                switch (received){
                    case "base1": //login
                        username= cli.getUsername();
                        server.sendLogin(username); //Nella view facciamo due pulsanti: nuovo account o accedi al tuo account, in base a ciò decide il server se la login è succeeded o failed
                        response=(String)in.readObject();
                        while(response.equals("LoginFailed")){
                            username= cli.getUsername();
                            server.sendLogin(username);
                            response=(String) in.readObject();
                        }
                        response=(String)in.readObject();
                        if(response.equals("ListOfGames")){
                            ArrayList<String> join=new ArrayList<>();
                            join=(ArrayList<String>) in.readObject();
                            ArrayList<String> resume=new ArrayList<>();
                            resume=(ArrayList<String>) in.readObject();
                            String selected="Gioco di Pippo";
                            //mandare choosingGame con la choice
                            server.sendGameSelected(selected);
                        }
                        else if(response.equals("NoGames")) {
                            //che famo? penso che farà new game di default
                        }
                        response=(String) in.readObject();
                        if(response=="Creation"){
                            match=(Match) in.readObject();
                            server.sendACK();
                            //nel caso in cui stia creando una nuova partita: da mandare prima di GameSelected
                            int num=cli.getNumPlayer();
                            server.sendNumPlayers(num);
                            response=(String)in.readObject();
                            if(response=="NACK"){
                                //decisione
                            }else if(response=="ACK"){
                                //si va a avanti
                            }
                        }
                        else{
                            server.sendNACK();
                        }
                        //decision
                        received="ok";
                        break; //adesso mi metto in ascolto
                    case "ACK":
                        //decisione
                        break;
                    case  "NACK":
                        //decisione
                        break;
                    case "Wizard":
                        //decisione
                        Wizards willy;
                        willy=cli.getWizard();
                        server.sendChoice(willy);
                        break;
                    case "Creation":
                        match=(Match) in.readObject();
                        action=new Action(match);
                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if(match.getPlayer()[i].getUserName().equals(username))
                                me=match.getPlayer()[i];
                        }
                        cli.printMatch(match);
                        cli.setMe(me);
                        cli.setMatch(match);
                        view=new Thread(cli);
                        view.start();
                        server.sendACK();
                        break;
                    case "RefillClouds": //posso ricevere da 2 a 4 ArrayList<Students>, uno per ogni nuvola
                        for(int i=0;i<match.getCloud().length;i++) {
                            match.getCloud()[i] = (Cloud) in.readObject();
                        }
                        cli.printMatch(match);
                        server.sendACK();
                        break;
                    case "ChooseCard":
                        List<AssistantCard> cards;
                        cards = (ArrayList<AssistantCard>) in.readObject();
                        cli.setCards(cards);
                        cli.wakeUp(received);
                        break;
                    case "MoveStudents":
                        cli.wakeUp(received);
                        break;
                    case "MoveMN": //DA MODIFICARE IL PROTOCOLLO
                        cli.wakeUp(received);
                        //nella nuova versione non è previsto ACK o NACK
                        break;
                    case "ChooseCloud":
                        List<Cloud> clouds;
                        clouds = (ArrayList<Cloud>) in.readObject();
                        cli.setClouds(clouds);
                        cli.wakeUp(received);
                        //server.sendChoiceCloud(cl);
                        break;
                    case "NotifyChosenCard":
                        AssistantCard card=(AssistantCard) in.readObject();
                        Player pl2=(Player) in.readObject();
                        for (int i = 0; i < match.getPlayer().length; i++) {
                            if(match.getPlayer()[i].getUserName().equals(pl2.getUserName())){
                                match.getPlayer()[i].draw(card.getValue());
                            }
                        }
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyMoveStudents (id)":
                        Student stu=(Student) in.readObject(); //lo studente stesso
                        int id=(int)in.readObject(); //id della Land
                        String user=(String) in.readObject();
                        for (Land e:match.getLands()) {
                            if(id==e.getID())
                                e.addStudent(stu);
                        }
                        cli.printMatch(match);
                        server.sendACK();
                        break;
                    case "NotifyMoveStudents (board)":
                        Student s=(Student) in.readObject(); //lo studente stesso
                        Board b=(Board) in.readObject();
                        String usern=(String) in.readObject();
                        for (int i=0;i<match.getPlayer().length;i++) {
                            if(match.getPlayer()[i].getUserName().equals(user)) {
                                action.moveFromIngressToBoard(match.getPlayer()[i],s);
                            }
                        }
                        action.checkAllProfessors();
                        cli.printMatch(match);
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyMovementMN":
                        int movement=(int)in.readObject();
                        int idLand;
                        ArrayList<Land> lands=(ArrayList<Land>) in.readObject();
                        match.moveMotherNature(movement);
                        idLand=match.getMotherNature().getPosition().getID();
                        match.setLands(lands);
                        for (Land e:match.getLands()) {
                            if(e.getID()==idLand){
                                match.getMotherNature().setPosition(e);
                            }
                        }
                        cli.printMatch(match);
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyProfessors":
                        Map<Type_Student, Player> prof=(Map<Type_Student, Player>) in.readObject();
                        match.setProfessors(prof);
                        cli.printMatch(match);
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyChosenCloud":
                        Player p=(Player) in.readObject();
                        Cloud cl=(Cloud) in.readObject();
                        for (int j=0;j<match.getCloud().length;j++) {
                            if(cl==match.getCloud()[j]){
                                match.getCloud()[j].choose();
                                p.getBoard().importStudents(cl.getStudents());
                            }
                        }
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyTowers (land)":
                        ArrayList<Tower> towers=(ArrayList<Tower>) in.readObject();
                        Land land=(Land) in.readObject();
                        for (Land e: match.getLands()) {
                            if(e.getID()==land.getID())
                                e.changeTower(towers.get(0));
                        }
                        cli.printMatch(match);
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyTowers (board)":
                        ArrayList<Tower> towers1=(ArrayList<Tower>) in.readObject();
                        Board board=(Board) in.readObject();
                        String us=(String) in.readObject();
                        for (int i=0;i<match.getPlayer().length;i++) {
                            if(match.getPlayer()[i].getUserName().equals(us))
                                match.getPlayer()[i].getBoard().setTowers(towers1);
                        }
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "EndGame":
                        Player winner=(Player) in.readObject();
                        String ex=(String) in.readObject(); //spiegazione di perchè ha vinto
                        ArrayList<Land> landd=(ArrayList<Land>) in.readObject(); //situa finale lands
                        ArrayList<Board> boards=(ArrayList<Board>) in.readObject(); //situa di tutte le baoard
                        cli.getWinner(winner);
                        cli.wakeUp("EndGame");
                        end=true;
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "LastTower":
                        Player pl=(Player) in.readObject();
                        cli.getWinner(pl);
                        cli.wakeUp("EndGame");
                        end=true;
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NoMoreStudents":
                        cli.lastRound();
                        server.sendACK();
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NextTurn":
                        Player play=(Player) in.readObject();
                        String phase=(String)in.readObject();
                        cli.printTurn(play,phase);
                        server.sendACK(); //o server.sendNACK();
                        break;
                    case "Ping":
                        server.sendPONG();
                        break;
                }
                if(end==true)
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
