package it.polimi.ingsw.serverController;

import it.polimi.ingsw.model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Manages all the interactions between Controller (server) and the remote player (client)
 */
public class ClientHandler extends Thread{
    private final Socket socket;
    private final Server server;
    ObjectInputStream in;
    ObjectOutputStream out;
    private Controller controller;
    private String userName;
    private Colors color;
    private int wizard;
    private Player avatar;
    private int state;
    private Match match;
    private boolean connected;
    private boolean ongoingMatch;

    /**
     *
     * @param s the socket associated with this player
     */
    public ClientHandler (Socket s, Server server){
        socket = s;
        this.server = server;

        try{
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(4500); //gli mando inirizzo di porta TCP, speriamo funzioni
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        connected = true;
        ongoingMatch = true;
    }

    public void run(){

    }

    private void changeState(){
        boolean control;

        switch(state){
            case 0:
                String game;
                try {
                    game = (String) in.readObject();

                    if (game.equals("NewGame")) {
                        userName = (String) in.readObject();
                        controller = server.createMatch(this, choosePlayersNum());
                    }
                    else {
                        if (game.equals("JoinGame")){
                            if (server.areThereJoinableMatches(userName)){
                                out.writeObject(server.getJoinableMatches(userName));
                                try {
                                    controller = server.joinGame((String) in.readObject(), this);
                                } catch (Exception e) {
                                    //Dice al client che quel match é gia' pieno
                                }
                                do {
                                    userName = (String) in.readObject();

                                    //if (controller.getUserNames().contains(userName)){
                                        //control = false;
                                        //loginFailed();
                                    //}
                                    else {
                                        control = true;
                                    }
                                }while (!control);
                            }
                        }
                        else {
                            //out.writeObject(server.getResumeableMatches());
                            //controller = server.resumeGame((String) in.readObject(), this);
                        }
                    }
                }catch (ClassNotFoundException | IOException e){
                    //out.writeChars("Nack");
                }

                state = 1;
                break;
            case 1:
                //Fase PIANIFICAZIONE: si notificano gli studenti spostati nelle nuvole
                state = 2;
                break;
            case 2:
                //Fase PIANIFICAZIONE: gioca una carta assistente
                state = 3;
                break;
            case 3:
                //Fase AZIONE: si muovono i 3/4 studenti dall'ingresso
                //Verifica se puo controllare qualche professore
                state = 4;
            case 4:
                //Fase AZIONE: muove MN
                //Verifica se l'isola diventa controllata o viene conquistata
                //Unisce le isole
                if (ongoingMatch){
                    state = 5;
                }
                else{
                    state = 6;
                    break;
                }
            case 5:
                //Fase AZIONE: sceglie una nuova nuvola e importa gli studenti
                state = 1;
                break;
            case 6:
                //Fine partita: si invia il vincitore
                break;
            case 7:
                //Fase AZIONE: gioca una carta personaggio
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setColor(Colors color) {
        this.color = color;
    }

    public Colors getColor() {
        return color;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    /**
     * Creates an instance of the Class Player in the game model
     * @param color the color of the towers this player controls
     * @param playersNum to decide how many towers instantiate in the board
     * @param expert true if it's an expert match
     */
    public void createAvatar(Colors color, int playersNum, boolean expert){
        int towersNum;

        if (playersNum==2 || playersNum==4){
            towersNum = 8;
        }
        else {
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
        System.out.println("Assistant");

        if (hasPlayableCard){
            for (i=0; played[i]!=0; i++) {
                System.out.println(played[i]);
            }
        }
        System.out.println(0);
        //Quando il controller lato client riceve (eventualmente qualche int e) 0 dopo "Assistant",
        // sa che puo' inviare alla view il comando di fare scegliere al player la carta assistente da giocare
        card = in.nextInt();
        avatar.draw(card);
        return card;
    }

    public boolean isConnected() {
        return connected;
    }

    public int seeState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void matchHasBeenDeleted (String creator){
        //Nei messaggi ne va aggiunto uno per notificare che il match è stato eliminato
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

}
