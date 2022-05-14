package it.polimi.ingsw.serverController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port = 4096;
    private ServerSocket sSocket;
    private Socket client;
    private InetSocketAddress myIP;
    private DatagramPacket packet;
    private DatagramPacket packet4client;
    private DatagramSocket sock;
    private final ExecutorService players;
    private final ArrayList<String> userNames;

    private final ArrayList<Controller> matches;

    private String message;

    public Server(){
        players = Executors.newCachedThreadPool();
        userNames = new ArrayList<>();
        matches = new ArrayList<>(1);
    }

    public void start(){
        try {
            Socket socket;
            sSocket = new ServerSocket();
            myIP=new InetSocketAddress(InetAddress.getLocalHost(),port);
            sSocket.bind(myIP);
            System.out.println("Server ready");
            sock=new DatagramSocket(port);
            byte[] buf=new byte[1];
            packet=new DatagramPacket(buf, 0, 0);

            while (true) {
                try {
                    sock.receive(packet);
                    packet4client = new DatagramPacket(buf, 0, buf.length, packet.getAddress(), packet.getPort());
                    sock.send(packet4client);
                    client = sSocket.accept();
                    players.submit(new ClientHandler(client, this));
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

    public synchronized void addUserName(String userName) throws Exception{
        if (userNames.contains(userName)){
            throw new Exception("Username already existing");
        }
        userNames.add(userName);
    }

    public synchronized void removeUserName(String userName){
        userNames.remove(userName);
    }

    public ArrayList<String> getUserNames() {
        return (ArrayList<String>)userNames.clone();
    }

    public synchronized Controller createMatch(ClientHandler creator, int playersNum, boolean expertMatch) throws Exception{
        for (Controller match : matches){
            if (match.getCreator().equals(creator.getUserName())){
                matches.remove(match);
                match.notifyDeletion();
                break;
            }
        }
        Controller match = new Controller(creator, playersNum, expertMatch);
        matches.add(match);
        return match;
    }

    public boolean areThereJoinableMatches(String userName){
        for (Controller match : matches){
            if (match.isNotFull() && match.getPlayers().contains(userName)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getJoinableMatches(String userName) {
        ArrayList<String> creators = new ArrayList<>();
        for (Controller match : matches){
            if (match.isNotFull() && match.getPlayers().contains(userName)) {
                creators.add(match.getCreator());
            }
        }
        return creators;
    }

    public synchronized Controller joinGame (String creator, ClientHandler player) throws Exception {
        for (Controller match : matches){
            if (match.getCreator().equals(creator)){
                match.addPlayer(player);
                return match;
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

    public synchronized Controller rejoinToGame(String creator, ClientHandler player){
        for (Controller match : matches){
            if (match.getCreator().equals(creator)){
                match.connectPlayer(player);
                return match;
            }
        }
        return null;
    }
}
