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

    /**
     * The server of the game Eriantys
     */
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
            System.out.println("Server running @"+myIP);
            byte[] buf=new byte[1];
            buf[0]=1;
            packet=new DatagramPacket(buf, 0, 0);

            while (true) {
                try {
                    sock.receive(packet); //ricevo richiesta di connessione dal client
                    packet4client = new DatagramPacket(buf, 0, buf.length, packet.getAddress(), packet.getPort());
                    sock.send(packet4client);//gli mando un datagrampacket all'indirizzo al pacchetto che ho ricevuto
                    Socket client = sSocket.accept(); //accetto connessione tcp dal client
                    newPlayer = new ClientHandler(client, this);
                    players.submit(newPlayer);
                }catch (IOException e) {
                    System.out.println("Server cannot connect with a client. Trying a new connection.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to start the socket. Shutting down...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a username to the registered ones
     * @param userName the username to add
     */
    public synchronized void addUserName(String userName) {
        if (!userNames.contains(userName)){
            userNames.add(userName);
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getUserNames() {

        return (ArrayList<String>)userNames.clone();
    }

    /**
     * Creates an instance of the controller managing a match
     * @param creator the player who created the match
     * @param playersNum the number of players the match should contain
     * @param expertMatch true if it is an expert match
     * @return the instance of the controller managing the match
     */
    public synchronized Controller createMatch(ClientHandler creator, int playersNum, boolean expertMatch) {
        if (!matches.isEmpty()) {
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
        return match;
    }

    /**
     *
     * @return the list of the matches which are not full yet
     */
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
                    if (match.isPaused() || match.getPlayers().contains(player.getUserName())) {
                        match.connectPlayer(player);

                        if (match.readyToStart()) {
                            match.resumeMatch();
                        }
                    } else {
                        match.addPlayer(player);
                    }
                    return match;
                }
            }
            else {
                if (match.getPlayers().contains(player.getUserName())) {
                    if (!match.isPaused()) {
                        match.connectPlayer(player);
                        return match;
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
