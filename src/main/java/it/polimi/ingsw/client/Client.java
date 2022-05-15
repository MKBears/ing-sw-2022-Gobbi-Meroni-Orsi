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
        message4Server server;
        Match match=null;
        Cli cli=new Cli();;
        Action action=null;
        String username=null;
        Player me=null;
        Boolean end=false;
        try {
            condition=false;
            addr= InetAddress.getLocalHost().getAddress();
            addr[3]=(byte)255;
            dSokk=new DatagramSocket();
            System.out.println("Client: Inizializzato");
            byte[] buf = new byte[1];
            starting= new DatagramPacket(buf, 0, buf.length, InetAddress.getByAddress(addr), 4898);
            do { //non do while... va messo un timer che ripete l'operazione dopo un po' e dopo 3 volte lancia eccezione
                dSokk.send(starting);
                System.out.println("Client: Ho mandato riciesta, ora vediamo di ricevere...");
                buf = new byte[1];
                buf[1]=-1;
                packet = new DatagramPacket(buf, buf.length);
                dSokk.receive(packet);
                if(packet.getData()==buf){
                    condition=true;
                }
            }while(!condition);
            System.out.println("Client: Ricevuto pacchetto da Marco");
            InetAddress ip= packet.getAddress();
            int port= 2836;
            //String connesso="Sono connesso TCP";
            System.out.println("Client: Ho creato la stringa da mandare: indirizzo di Marco "+ port +" "+ ip);
            /*try {
                this.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            socket= new Socket(ip.getHostAddress(),port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in= new ObjectInputStream(socket.getInputStream());
            server=new Message4Server(in,out);
            received="base1";
            while (true){
                if(received!="base1") {
                    received = (String) in.readObject();
                }
                switch (received){
                    case "base1": //login
                        username= cli.getUsername();
                        server.sendLogin(username); //Nella view facciamo due pulsanti: nuovo account o accedi al tuo account, in base a ciò decide il server se la login è succeeded o failed
                        response=(String)in.readObject(); //può essere LoginSucceeded o LoginFailed <------
                        //decisione da prendere: il server sa che se il login non ha successo bisogna mandare un altro username
                        //choosing game: decido se voglio fare new game, join game o resume game
                        String decision="JoinGame"; //decision
                        server.sendChoosingGame(decision); //la mando
                        response=(String) in.readObject(); //risposta: o NoGames o ListOfGames o ListOfStartedGames
                        //decisione da prendere
                        String selected="Gioco di Pippo";
                        //nel caso in cui stia creando una nuova partita: da mandare prima di GameSelected
                        server.sendNumPlayers(3); //esempio
                        response=(String)in.readObject();
                        if(response=="NACK"){
                            //decisione
                        }else if(response=="ACK"){
                            //si va a avanti
                        }
                        server.sendGameSelected(selected);
                        response=(String) in.readObject();
                        if(response=="Creation"){
                            match=(Match) in.readObject();
                            server.sendACK();
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
                        server.sendACK();
                        break;
                    case "RefillClouds": //posso ricevere da 2 a 4 ArrayList<Students>, uno per ogni nuvola
                        //decisione
                        server.sendACK();
                        break;
                    case "ChooseCard":
                        //decisione
                        AssistantCard a;
                        a=cli.getAssistantCard(me);
                        me.draw(a.getValue());
                        server.sendChosenCard(a);
                        //server.sendChosenCard();
                        break;
                    case "MoveStudents":
                        //decisione
                        Student st=null;
                        String move=null;
                        st=cli.getStudent(me);
                        move=cli.getDestination(match);
                        server.sendMovedStudent("Student1",st,move);
                        //server.sendMovedStudent(); con "Student1"
                        st=cli.getStudent(me);
                        move=cli.getDestination(match);
                        server.sendMovedStudent("Student2",st,move);
                        //server.sendMovedStudent(); con "Student2"
                        st=cli.getStudent(me);
                        move=cli.getDestination(match);
                        server.sendMovedStudent("Student3",st,move);
                        cli.printMatch(match);
                        //server.sendMovedStudent(); con "Student3", ma se non si muovono tutti e tre posso mettere null negli ultimi due campi (da fixare)
                        break;
                    case "MoveMN": //DA MODIFICARE IL PROTOCOLLO
                        //decisone
                        int step=cli.getNumStep(me);
                        //server.sendStepsMN();
                        action.cardAndMoveMN(me.getPlayedCard(),step);
                        server.sendStepsMN(step);
                        action.controlLand(me);
                        cli.printMatch(match);
                        //nella nuova versione non è previsto ACK o NACK
                        break;
                    case "ChooseCloud":
                        //decisione
                        Cloud clo=cli.getCloud(match);
                        //Cloud cl=new Cloud(...);
                        action.chooseCloud(me,clo);
                        server.sendChoiceCloud(clo);
                        cli.printMatch(match);
                        //server.sendChoiceCloud(cl);
                        break;
                    case "NotifyChosenCard":
                        //decisione
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyMoveStudents (id)":
                        String name=(String)in.readObject(); //"Student1" o "Student2" o "Student3"
                        Student stu=(Student) in.readObject(); //lo studente stesso
                        int id=(int)in.readObject(); //id della Land

                        //decisione
                        break;
                    case "NotifyMoveStudents (board)":
                        String n=(String)in.readObject(); //"Student1" o "Student2" o "Student3"
                        Student s=(Student) in.readObject(); //lo studente stesso
                        Board boa=(Board) in.readObject(); //la board in cui finisce
                        //decisione

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
                        //decisione
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyProfessors":
                        Map<Type_Student, Player> prof=(Map<Type_Student, Player>) in.readObject();
                        //decisione
                        match.setProfessors(prof);
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
                        //decisione
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
                        //decisione
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NotifyTowers (board)":
                        ArrayList<Tower> towers1=(ArrayList<Tower>) in.readObject();
                        Board board=(Board) in.readObject();
                        //decisione

                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "EndGame":
                        Player winner=(Player) in.readObject();
                        String ex=(String) in.readObject(); //spiegazione di perchè ha vinto
                        ArrayList<Land> landd=(ArrayList<Land>) in.readObject(); //situa finale lands
                        ArrayList<Board> boards=(ArrayList<Board>) in.readObject(); //situa di tutte le baoard
                        //decisione
                        cli.getWinner(winner);
                        end=true;
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "LastTower":
                        Player pl=(Player) in.readObject();
                        cli.getWinner(pl);
                        end=true;
                        //decisione
                        //server.sendACK(); o server.sendNACK();
                        break;
                    case "NoMoreStudents":
                        //decisione
                        //server.sendACK(); o server.sendNACK();
                        break; //NON HO FATTO CHCHOSEN
                    case "NextTurn":
                        Player play=(Player) in.readObject();
                        String phase=(String)in.readObject();
                        //decisione
                        cli.printTurn(play,phase);
                        server.sendACK(); //o server.sendNACK();
                        break;
                    case "Ping":
                        server.sendACK();
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
