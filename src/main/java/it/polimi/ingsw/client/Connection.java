package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Connection { //DA MODIFICARE TUTTA

    public void run(){
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;
        DatagramSocket dSokk;
        byte[] addr;
        DatagramPacket starting;
        DatagramPacket packet;

        try {
            addr= InetAddress.getLocalHost().getAddress();
            addr[3]=(byte)255;
            dSokk=new DatagramSocket();
            System.out.println("Client: Inizializzato");
            byte[] buf = new byte[30];
            starting= new DatagramPacket(buf, 0, buf.length, InetAddress.getByAddress(addr), 4898);
            do { //non do while... va messo un timer che ripete l'operazione dopo un po' e dopo 3 volte lancia eccezione
                dSokk.send(starting);
                System.out.println("Client: Ho mandato il mandabile, ora vediamo di ricevere...");
                buf = new byte[1];
                packet = new DatagramPacket(buf, buf.length);
            }while(!dSokk.receive(packet));
            System.out.println("Client: Ricevuto pacchetto da Marco");
            InetAddress ip= packet.getAddress();
            int port= packet.getPort();
            String connesso="Sono connesso TCP";
            System.out.println("Client: Ho creato la stringa da mandare: indirizzo di Marco "+ port +" "+ ip);
            /*try {
                this.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }*/
            socket= new Socket(ip.getHostAddress(),port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(connesso);
            System.out.println("Client: Stringa mandata");
            socket.close();
        } catch (IOException e) {
            System.out.println("Non trovo il server");
            System.err.println(e.getMessage());
        }
    }
}
