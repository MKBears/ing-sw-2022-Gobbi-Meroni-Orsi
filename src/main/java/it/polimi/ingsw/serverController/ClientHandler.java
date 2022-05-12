package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Colors;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizards;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;

/**
 * Manages all the interactions between Controller (server) and the remote player (client)
 */
public class ClientHandler extends Thread{
    private final int port = 4096;
    private ServerSocket sSocket;
    private Socket client;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private InetSocketAddress ip_mio;
    private DatagramPacket packet;
    private DatagramPacket packet4client;
    private DatagramSocket sock;

    private String message;
    private String userName;
    private int wizardNumber;
    private Player avatar;
    private boolean condition;

    /**
     *
     * @param s the socket associated with this player
     */
    public ClientHandler (Socket s, Server server){ /////METTI A POSTO POI
        condition=true;
        try {
            sSocket = new ServerSocket();
            ip_mio = new InetSocketAddress(Inet4Address.getLocalHost(), port);
            sSocket.bind(ip_mio);
            System.out.println("Server ready");
            sock = new DatagramSocket(port);
            byte[] buf = new byte[1];
            packet = new DatagramPacket(buf, 0, 0);
            sock.receive(packet);
            client = new Socket(packet.getAddress(), packet.getPort());
            packet4client = new DatagramPacket(buf, 0, buf.length, client.getInetAddress(), client.getPort());
            sock.send(packet4client);
            client = sSocket.accept();
            in = new ObjectInputStream(client.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {

    }

    /**
     * Asks the remote controller how many players will play the match
     * @return the number of players
     */
    public int choosePlayersNum (){
        out.println("Players");
        return in.nextInt();
        //Lato client metteremo tre bottoni: uno per 2 giocatori, uno per 3 e l'altro per 4
        //  quindi non c'é bisogno di controllare quello che inserisce l'utente
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Asks the remote controller to choose a Wizard between the available ones
     * @param wizards are the wizards chosen by the players before
     * @return
     */
    public int setWizard(boolean[] wizards){
        out.println("Wizard");
        for (int i = 0; i < wizards.length; i++) {
            if (wizards[i]) {
                out.println(i);
            }
        }
        wizardNumber = in.nextInt();
        return wizardNumber;
    }

    /**
     *
     * @return true if the player wants to play an expert match
     */
    public boolean expertMatch(){ //non contemplare
        out.println("Pro");
        return in.nextBoolean();
    }

    /**
     * Creates an instance of the Class Player in the game model
     * @param color the color of the towers this player controls
     * @param playersNum to decide how many towers instantiate in the board
     * @param expert true if it's an expert match
     */
    public void createAvatar(Colors color, int playersNum, boolean expert){
        Wizards wizard;
        int towersNum;

        switch (wizardNumber){
            case 1: wizard = Wizards.WIZARD1;
            break;
            case 2: wizard = Wizards.WIZARD2;
            break;
            case 3: wizard = Wizards.WIZARD3;
            break;
            default: wizard = Wizards.WIZARD4;
            break;
        }

        if(playersNum==2 || playersNum==4){
            towersNum = 8;
        }
        else{
            towersNum = 6;
        }
        avatar = new Player(userName, color, towersNum, wizard, expert);
    }

    /**
     *
     * @return the representation of this player in the model
     */
    public Player getAvatar(){
        return avatar;
    }

    /**
     * Asks the remote controller to play an Assistant Card, verifies it hasn't already been played by another player
     * (if the player has only played cards in his deck, he can play any of them) and sets it as the plyed card in the model
     * @param played are the values of the cards which have already been played
     * @return the value of the played card
     */
    public int playAssistant(int[] played){
        //Played sono le carte giocate dagli altri giocatori
        // played[i]==0 significa che il player i non ha ancora giocato una carta
        boolean hasPlayableCard = false;
        ArrayList<AssistantCard> deck = avatar.getDeck();
        int i = 0;
        int c;
        int card;

        //Si controlla se il player ha almeno una carta che non è ancora stata giocata nella mano corrente
        while(!hasPlayableCard && played[i]!=0 && i<played.length){
            c = 0;
            while(c<deck.size() && !hasPlayableCard){
                if (deck.get(c).getValue() != played[i]) {
                    hasPlayableCard = true;
                }
                c++;
            }
            i++;
        }
        out.println("Assistant");

        if (hasPlayableCard){
            for (i=0; played[i]!=0; i++) {
                out.println(played[i]);
            }
        }
        out.println(0);
        //Quando il controller lato client riceve (eventualmente qualche int e) 0 dopo "Assistant",
        // sa che puo' inviare alla view il comando di fare scegliere al player la carta assistente da giocare
        card = in.nextInt();
        avatar.draw(card);
        return card;
    }

    /**
     * Closes inward and outward stream and the socket
     * @throws Exception fails to close the socket
     */
    public void closeConnection() throws Exception{
        in.close();
        out.close();
        socket.close();
    }

    /**
     *
     * @param endOfCurrentRound indicates if the match finishes at the end of the current round (true) or immediately (false)
     */
    public void notifyEndMatch(boolean endOfCurrentRound){
        out.print("End ");
        if (endOfCurrentRound){
            out.println("round");
        }
        else{
            out.println("immediately");
        }
    }
}
