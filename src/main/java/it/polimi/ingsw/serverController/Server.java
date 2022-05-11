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

        try {
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
                    message = (String) in.readObject();
                    switch (message) {
                        case "ChoosingGame":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "ACK":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "NACK":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "Login":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "NewGame":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "JoinGame":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "ResumeGame":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "GameSelected":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "NumPlayers":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "Choice":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "ChosenCard":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "Student1":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "Student2":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "Student3":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "StepsMN":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "ChoiceCloud":
                            System.out.println("Received!");
                            //decision
                            break;
                        case "ChChosen":
                            System.out.println("Received!");
                            //decision
                            break;
                    }
                } catch (IOException e) {
                    System.out.println("Server cannot connect with a client. Trying a new connection.");
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
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
