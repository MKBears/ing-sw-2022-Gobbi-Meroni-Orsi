package it.polimi.ingsw.serverController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

/**
 * This class is the thread that sends the "Ping" message to every client to check che connection is still working
 */
public class Ping extends Thread implements Observer{

    private Message4Client client;
    private boolean connection;
    private String name;
    private ObjectInputStream in;
    private  ObjectOutputStream out;

    /**
     * Message4Client initialization
     * @param in
     * @param out
     */
    public Ping(ObjectInputStream in, ObjectOutputStream out){
        client=new Message4Client(out, in);
        connection=true;
        this.in=in;
        this.out=out;
    }

    /**
     * The "Ping" thread. It stops when the server doesn't receive the ACK message from the client and turns the condition "connection" to false
     */
    public void run() throws RuntimeException{
        boolean condition=true;

        while (condition) {
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
            name="Ping";
            out.writeObject(name);
            name=(String)in.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Getter of the boolean connection
     * @return the state of connection
     */
    public boolean getclientconnection(){
        return connection;
    }

    public void turnconnecionfalse(){
        if(connection==true){
            connection=false;
        }
    }


    @Override
    public void update(Observable o, Object connection) {

    }
}
