package it.polimi.ingsw.serverController;

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

    public Server(){
        players = Executors.newCachedThreadPool();
        Socket socket;
        userNames = new ArrayList<>();

        try {
            sSocket = new ServerSocket();
            sSocket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), port));
            System.out.println("Server ready");

            while (true){
                try {
                    socket = sSocket.accept();
                    players.submit(new ClientHandler(socket, this));
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
}
