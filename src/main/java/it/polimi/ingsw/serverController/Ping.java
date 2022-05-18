package it.polimi.ingsw.serverController;

import it.polimi.ingsw.client.Cli;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is the thread that sends the "Ping" message to every client to check che connection is still working
 */
public class Ping extends Thread{

    private Message4Client client;
    private boolean connection;
    private String name;
    private ObjectInputStream in;
    private  ObjectOutputStream out;

    ClientHandler ch;
    /**
     * Message4Client initialization
     * @param in
     * @param out
     */
    public Ping(ObjectInputStream in, ObjectOutputStream out, ClientHandler ch){
        client=new Message4Client(out, in);
        connection=true;
        this.in=in;
        this.out=out;
        this.ch=ch;
    }

    /**
     * The "Ping" thread. It stops when the server doesn't receive the ACK message from the client and turns the condition "connection" to false
     */
    public void run(){
        boolean condition=true;

        while (condition) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
            name="Ping";
            synchronized (this) {
                out.writeObject(name);
                name = (String) in.readObject();
            }
            } catch (ClassNotFoundException | IOException e) {
                ch.setDisconnected();
            }

        }

    }

}
