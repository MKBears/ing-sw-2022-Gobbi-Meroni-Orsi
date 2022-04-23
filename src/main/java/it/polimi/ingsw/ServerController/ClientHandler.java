package it.polimi.ingsw.ServerController;

import it.polimi.ingsw.model.AssistantCard;
import it.polimi.ingsw.model.Colors;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Wizards;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler extends Thread{
    private final int playerId;
    private final Socket socket;
    Scanner in;
    PrintWriter out;
    private String userName;
    private int wizardNumber;
    private Player avatar;

    public ClientHandler (int id, Socket s){
        playerId = id;
        socket = s;
    }

    //Non credo sia necessario il multithreading perché si gioca un player alla volta
    @Override
    public void run() {
        try{
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());

            while (true){
                setUserName();
                System.out.println("Player"+(playerId+1)+": "+userName);
                wait();
                //I giocatori mandano roba
                break;
            }
            in.close();
            out.close();
            socket.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPlayerId() {
        return playerId;
    }

    public int choosePlayersNum (){
        out.println("Giocatori");
        return in.nextInt();
        //Lato client metteremo tre bottoni: uno per 2 giocatori, uno per 3 e l'altro per 4
        //  quindi non c'é bisogno di controllare quello che inserisce l'utente
    }

    private void setUserName(){
        out.println("Username");
        userName = in.nextLine();
    }

    public String getUserName() {
        return userName;
    }

    public int setWizard(boolean[] wizards){
        synchronized(wizards) {
            out.println("Wizard");
            for (int i = 0; i < wizards.length; i++) {
                if (wizards[i]) {
                    out.println(i);
                }
            }
            wizardNumber = in.nextInt();
            return wizardNumber;
        }
    }

    public boolean expertMatch(){
        out.println("Pro");
        return in.nextBoolean();
    }

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

    public Player getAvatar(){
        return avatar;
    }

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
        //Quando il controller lato client riceve 0 dopo "Assistant" ed eventualmente qualche int,
        // sa che puo' inviare alla view il comando di fare scegliere al player la carta assistente da giocare
        card = in.nextInt();
        avatar.draw(card);
        return card;
    }

}
