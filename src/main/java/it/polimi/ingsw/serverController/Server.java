package it.polimi.ingsw.serverController;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{
    private final ExecutorService players;
    private ArrayList<String> userNames;
    private final ArrayList<Controller> matches;
    private final ArrayList<GameSaved> interrupt_matches;

    /**
     * The server of the game Eriantys
     */
    public Server(){
        players = Executors.newCachedThreadPool();
        userNames = new ArrayList<>();
        interrupt_matches=new ArrayList<>();
        File file=new File("username.txt");
        try {
            if(!file.createNewFile()) {
                file.setReadable(true);
                FileInputStream fIn = new FileInputStream(file);
                ObjectInputStream oIn = new ObjectInputStream(fIn);
                userNames=(ArrayList<String>)oIn.readObject();
                fIn.close();
                oIn.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        File matchFile;
        for (String u: userNames) {
           matchFile=new File("matches/"+u+".txt");
           if(matchFile.exists()){
               try{
                   FileInputStream fIn = new FileInputStream(matchFile);
                   ObjectInputStream oIn = new ObjectInputStream(fIn);
                   GameSaved game=(GameSaved) oIn.readObject();
                   fIn.close();
                   oIn.close();
                   interrupt_matches.add(game);
               } catch (IOException | ClassNotFoundException e) {
                   e.printStackTrace();
               }
           }
        }
        matches = new ArrayList<>(1);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void run() {
        final int portUDP = 4898;
        final int portTCP = 2836;
        InetSocketAddress myIP;
        DatagramPacket packet;
        DatagramPacket packet4client;

        try (ServerSocket sSocket = new ServerSocket(); DatagramSocket sock=new DatagramSocket(portUDP)){
            ClientHandler newPlayer;
            myIP=new InetSocketAddress(InetAddress.getLocalHost(),portTCP);
            sSocket.bind(myIP);
            System.out.println("Server running @"+myIP);
            byte[] buf=new byte[1];
            buf[0]=1;
            packet=new DatagramPacket(buf, 0, 0);

            while (true) {
                try {
                    sock.receive(packet);
                    packet4client = new DatagramPacket(buf, 0, buf.length, packet.getAddress(), packet.getPort());
                    sock.send(packet4client);
                    Socket client = sSocket.accept();
                    newPlayer = new ClientHandler(client, this);
                    players.submit(newPlayer);
                }catch (IOException e) {
                    System.out.println("Server cannot connect with a client. Trying a new connection.");
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to start the socket. Shutting down...");
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a username to the registered ones
     * @param userName the username to add
     */
    public synchronized void addUserName(String userName) {
        if (!userNames.contains(userName)){
            userNames.add(userName);
            File file=new File("username.txt");
            FileOutputStream f;
            ObjectOutputStream out;
            try {
                file.createNewFile();
                file.setWritable(true);
                f=new FileOutputStream(file);
                out=new ObjectOutputStream(f);
                out.writeObject(userNames);
                out.close();
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This methos return the ArrayList of the usernames utilized
     * @return the ArrayList of usernames
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getUserNames() {

        return (ArrayList<String>)userNames.clone();
    }

    /**
     * Creates an instance of the controller managing a match
     * @param creator the player who created the match
     * @param playersNum the number of players the match should contain
     * @param expertMatch true if it is an expert match
     * @return the instance of the controller managing the match
     */
    public synchronized Controller createMatch(ClientHandler creator, int playersNum, boolean expertMatch) {
        if (!matches.isEmpty()) {
            for (Controller match : matches) {
                if (match.getCreator().equals(creator.getUserName())) {
                    matches.remove(match);
                    match.notifyDeletion("Creator started a new match");
                    break;
                }
            }
        }
        Controller match = new Controller(creator, playersNum, expertMatch);
        matches.add(match);
        return match;
    }

    /**
     *
     * @return the list of the matches which are not full yet
     */
    public ArrayList<String> getJoinableMatches() {
        ArrayList<String> creators = new ArrayList<>();
        for (Controller match : matches){
            if (match.isNotFull()) {
                creators.add(match.getCreator());
            }
        }
        return creators;
    }

    /**
     * Makes a player join a match
     * @param creator the creator of the match
     * @param player the palyer to add to the match
     * @return the controller of the match
     * @throws Exception if the match is already full
     */
    public synchronized Controller joinGame (String creator, ClientHandler player)throws Exception  {
        for (Controller match : matches){
            if (creator != null) {
                if (match.getCreator().equals(creator)) {
                    if (match.isPaused() || match.getPlayers().contains(player.getUserName())) {
                        match.connectPlayer(player);
                    } else {
                        if(!match.isGame_from_memory()) {
                            match.addPlayer(player);
                        }else{
                            if(match!=null) {
                                match.restartMatch(player);
                            }
                        }
                    }
                    return match;
                }
            }
            else {
                if (match.getPlayers().contains(player.getUserName())) {
                    if (!match.isPaused()) {
                        match.connectPlayer(player);
                        return match;
                    }
                }
            }
        }
        for (GameSaved g:interrupt_matches) {
            if(g.usernames().get(0).equals(creator)){
                Controller game=new Controller(player,g);
                matches.add(game);
                return  game;
            }
        }
        return null;
    }

    /**
     *
     * @param userName the username of the player
     * @return the paused matches containing the player's username
     */
    public ArrayList<String> getPausedMatches(String userName){
        ArrayList<String> creators = new ArrayList<>();

        for (GameSaved g:interrupt_matches) {
            if(g.usernames().contains(userName)){
                creators.add(g.usernames().get(0));
            }
        }
        return creators;
    }

    /**
     * Check if the player is already connected
     * @param player
     * @return true if the player is not connected
     */
    public boolean inactivePlayer (ClientHandler player) {
        for (Controller match : matches) {
            if (match.getPlayers().contains(player.getUserName())) {
                if (!match.isPaused()) {
                    if (match.getPlayer(player.getUserName()).isConnected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     *
     * @param player
     * @return true if the player can be reconnected to an ongoing match
     */
    public boolean canConnectPlayer (String player) {
        for (Controller match : matches) {
            if (match.getPlayers().contains(player)) {
                if (!match.isPaused()) {
                    return true;
                }
            }
        }
        return false;
    }

}
