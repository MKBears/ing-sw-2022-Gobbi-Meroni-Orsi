package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.Colors;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port = 4096;
    private ServerSocket sSocket;
    private final ExecutorService players;
    private final ArrayList<String> userNames;
    private final ArrayList<Controller> matches;

    public Server(){
        players = Executors.newCachedThreadPool();
        userNames = new ArrayList<>();
        matches = new ArrayList<>(1);
    }

    public void start(){
        try {
            Socket socket;
            sSocket = new ServerSocket();
            sSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), port));
            System.out.println("Server ready");

            while (true){
                try {
                    socket = sSocket.accept();
                    players.submit(new ClientHandler(socket, this, Colors.BLACK));
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

    public void addUserName(String userName){
        userNames.add(userName);
    }

    public void removeUserName(String userName){
        userNames.remove(userName);
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public synchronized Controller createMatch(ClientHandler creator, int playersNum){
        Controller match = new Controller();
        matches.add(match);
        return match;
    }

    public boolean areThereJoinableMatches(){
        if (!matches.isEmpty()){
            return true;
        }
        else {
            return false;
        }
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

    public synchronized Controller joinGame (String creator, ClientHandler player){
        for (Controller match : matches){
            if (match.getPlayers().contains(creator)){
                match.addPlayer(player);
                return match;
            }
        }
        return null;
    }

    public ArrayList<String> getResumeableMatches(){
        ArrayList<String> creators = new ArrayList<>();
        for (Controller match : matches){
            if (match.isPaused()) {
                creators.add(match.getCreator());
            }
        }
        return creators;
    }

    public synchronized Controller resumeGame(String creator, ClientHandler player){
        for (Controller match : matches){
            if (match.getPlayers().contains(creator)){
                match.connectPlayer(player);
                return match;
            }
        }
        return null;
    }
}
