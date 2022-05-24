package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;

public class MessageFromServer extends Thread{
    private ObjectInputStream in;
    public String message;
    private Client client;
    private String ping;

    public MessageFromServer(ObjectInputStream in, Client client){
        this.in=in;
        this.client=client;
    }

    public void run(){
        while(true){
            try {
                message=(String)in.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            switch (message) {
                case "ACK":

            }
        }
    }

}
