package it.polimi.ingsw.serverController;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{
    private final ExecutorService players;
    private final ArrayList<String> userNames;

    private final ArrayList<Controller> matches;

    public Server(){
        players = Executors.newCachedThreadPool();
        userNames = new ArrayList<>();
        matches = new ArrayList<>(1);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void run() {
        final int portUDP = 4898;
        final int portTCP = 2836;
        InetSocketAddress myIP;
        DatagramPacket packet;
        DatagramPacket packet4client;

        try (ServerSocket sSocket = new ServerSocket(); DatagramSocket sock=new DatagramSocket(portUDP)){
            ClientHandler newPlayer;
            myIP=new InetSocketAddress(InetAddress.getLocalHost(),portTCP); //indirizzo tcp
            sSocket.bind(myIP);
            System.out.println("Server ready");
            byte[] buf=new byte[1];
            buf[0]=1;
            packet=new DatagramPacket(buf, 0, 0);

            while (true) {
                try {
                    //System.out.println("Aspetto datagram packet");
                    sock.receive(packet); //ricevo richiesta di connessione dal client
                    packet4client = new DatagramPacket(buf, 0, buf.length, packet.getAddress(), packet.getPort());
                    sock.send(packet4client);//gli mando un datagrampacket all'indirizzo al pacchetto che ho ricevuto
                    //System.out.println("Info mandate");
                    Socket client = sSocket.accept(); //accetto connessione tcp dal client
                    newPlayer = new ClientHandler(client, this);
                    players.submit(newPlayer);
                    //newPlayer.start();
                    //System.out.println("Client accettato");
                }catch (IOException e) {
                    System.out.println("Server cannot connect with a client. Trying a new connection.");
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to start the socket. Shutting down...");
            throw new RuntimeException(e);
        }
    }

    public synchronized void addUserName(String userName) {
        if (!userNames.contains(userName)){
            userNames.add(userName);
        }
    }

    public ArrayList<String> getUserNames() {
        return (ArrayList<String>)userNames.clone();
    }

    public synchronized Controller createMatch(ClientHandler creator, int playersNum, boolean expertMatch) {
        if (!matches.isEmpty()) {
            System.out.println("Controllo se questo giocatore ha gia' creato una partita");
            for (Controller match : matches) {
                if (match.getCreator().equals(creator.getUserName())) {
                    matches.remove(match);
                    match.notifyDeletion("Creator started a new match");
                    break;
                }
            }
        }
        Controller match = new Controller(creator, playersNum, expertMatch);
        matches.add(match);
        System.out.println("Match creato");
        return match;
    }

    public boolean areThereJoinableMatches(String userName){
        for (Controller match : matches){
            if (match.isNotFull() || match.getPlayers().contains(userName)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getJoinableMatches() {
        ArrayList<String> creators = new ArrayList<>();
        for (Controller match : matches){
            if (match.isNotFull()) {
                creators.add(match.getCreator());
            }
        }
        return creators;
    }

    public synchronized Controller joinGame (String creator, ClientHandler player)throws Exception  {
        for (Controller match : matches){
            if (creator != null) {
                if (match.getCreator().equals(creator)) {
                    if (match.isPaused()) {
                        match.connectPlayer(player);

                        if (match.readyToStart()) {
                            match.resumeMatch();
                        }
                    } else {
                        match.addPlayer(player);

                        if (match.readyToStart()) {
                            match.start();
                        }
                    }
                    return match;
                }
            }
            else {
                if (match.getPlayers().contains(player.getUserName())) {
                    if (!match.isPaused()) {
                        match.connectPlayer(player);
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<String> getPausedMatches(String userName){
        ArrayList<String> creators = new ArrayList<>();
        for (Controller match : matches){
            if (match.isPaused() && match.getPlayers().contains(userName)) {
                creators.add(match.getCreator());
            }
        }
        return creators;
    }

    public boolean inactivePlayer (ClientHandler player) {
        for (Controller match : matches) {
            if (match.getPlayers().contains(player.getUserName())) {
                if (!match.isPaused()) {
                    if (match.getPlayer(player.getUserName()).isConnected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean canConnectPlayer (String player) {
        for (Controller match : matches) {
            if (match.getPlayers().contains(player)) {
                if (!match.isPaused()) {
                    return true;
                }
            }
        }
        return false;
    }

}
