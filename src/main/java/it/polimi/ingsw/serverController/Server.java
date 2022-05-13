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
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private InetSocketAddress ip_mio;
    private DatagramPacket packet;
    private DatagramPacket packet4client;
    private DatagramSocket sock;
    private final ExecutorService players;
    private final ArrayList<String> userNames;

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
            ip_mio=new InetSocketAddress(Inet4Address.getLocalHost(),port);
            sSocket.bind(ip_mio);
            System.out.println("Server ready");
            sock=new DatagramSocket(port);
            byte[] buf=new byte[1];
            packet=new DatagramPacket(buf, 0, 0);
            sock.receive(packet);
            client= new Socket(packet.getAddress(),packet.getPort());
            packet4client=new DatagramPacket(buf,0,buf.length,client.getInetAddress(), client.getPort());
            sock.send(packet4client);
            client = sSocket.accept();
            in= new ObjectInputStream(client.getInputStream());
            out= new ObjectOutputStream(client.getOutputStream());

            while (true) {
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
