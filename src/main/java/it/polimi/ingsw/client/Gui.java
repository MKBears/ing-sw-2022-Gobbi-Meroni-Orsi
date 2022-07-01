package it.polimi.ingsw.client;

import it.polimi.ingsw.client.guiControllers.*;
import it.polimi.ingsw.serverController.GameRecap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import it.polimi.ingsw.model.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Gui extends Application {
    private Stage stage;
    private Stage popup;
    private String state;
    private Boolean end;
    private Message4Server server;
    private Player me;
    private Match match;
    private Action action;
    private List<Wizards> willy;
    private List<Cloud> clouds;
    private List<AssistantCard> cards;
    private String username;
    private Wizards w;
    private FXMLLoader game;
    private boolean printmatch;
    private String us;
    private AssistantCard ass;
    private ClientGui cg;
    private Scene g;
    private boolean time;
    private FXMLLoader pup;
    private boolean newgame;
    private  int whoami;
    private CharacterCard[] ch;
    private boolean isch1usable;
    private boolean isch2usable;
    private boolean isch3usable;

    /**
     * constructor of the gui
     */
    public Gui() {
        end=false;
        state="Start";
        username=null;
        w=null;
        printmatch=false;
        us=null;
        ass=null;
        popup=new Stage();
        whoami=0;
        game=new FXMLLoader(getClass().getClassLoader().getResource("real_matchh.fxml"));
        pup=new FXMLLoader(getClass().getClassLoader().getResource("popup_notify.fxml"));
        try {
            g=new Scene(pup.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * set the client gui of the client
     * @param cg the client gui
     */
    public void setCG(ClientGui cg){
        this.cg=cg;
    }


    @Override
    /**
     * start of the application
     */
    public void start(Stage stage) throws Exception {
        setStage(stage);
        ClientGui c=new ClientGui(this);
        c.start();
        setCG(c);
    }

    /**
     * set the loading page at the beginning of the match
     * @param s stage of the application
     * @throws IOException throws by the loading of the file
     */
    public void setStage(Stage s) throws IOException {
        this.stage = s;
        stage.setTitle("Eryantis");
        FXMLLoader fxml=new FXMLLoader(getClass().getClassLoader().getResource("loading_page.fxml"));
        Scene scene=new Scene(fxml.load());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * set the username chosen by the player
     * @param username of the player
     */
    public void setUsername(String username){
        this.username=username;
    }

    /**
     * main to start the gui
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * set on the screen the request for the username
     */
    public void getUsername() {
        LoginController.setServer(server);
        LoginController.setGui(this);
        LoginController.setCl(cg);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("login_page.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        }catch (IOException e){
            e.printStackTrace();
        }
        stage.show();
    }

    /**
     * set the username chosen by the player
     * @param us the username
     */
    public void setUs(String us){
        this.us=us;
        cg.setUsername(us);
    }

    /**
     *
     * @return the username of the player
     */
    public String getUs(){
        return us;
    }

    /**
     * set the server to send the messages
     * @param server the class with th messages
     */
    public void setServer(Message4Server server) {
        this.server=server;
    }

    /**
     * request of the wizard to the player
     * @param wizards that the player can choose
     */
    public void getWizard(List<Wizards> wizards) {
        WizardsController.setServer(server);
        WizardsController.setGui(this);
        WizardsController.setCl(cg);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("chooseWizard.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
            ((WizardsController)fxmlLoader.getController()).setWilly(wizards);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }

    /**
     *
     * @return the wizard chosen from the player
     */
    public Wizards getW(){
        return w;
    }

    /**
     * allow the player choosing the clouds changing the state
     */
    public void getCloud() {
        ((MatchController)game.getController()).wakeUp("ChooseCloud");
        synchronized (cg){
            cg.notifyAll();
        }
    }

    /**
     * show thw popup with the message to show
     * @param title of the popup
     * @param message to show
     */
    public void popUp(String title, String message){
        boolean b=false;
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("popup_notify.fxml"));
        try {
            popup.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((PopUpController)fxmlLoader.getController()).setNotify(message);
        if(message.contains("vincitore")){
            if(b){
                message="Si sono formati tre gruppi di isole\n"+message;
            }
            popup.setFullScreen(true);
        }else {
            popup.sizeToScene();
        }
        if(message.contains("tre gruppi") && b==false){
            b=true;
        }
        popup.setTitle(title);
        popup.setAlwaysOnTop(true);
        popup.show();
    }

    /**
     * request for the assistant card to the player changing the state of the thread
     */
    public void getAssistantCard() {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ((MatchController)game.getController()).setStateLabel("Scegli una carta assistente");
        ((MatchController)game.getController()).wakeUp("ChooseAssistant");
        stage.show();
    }

    /**
     *
     * @param ass set the assistant card chosen
     */
    public void setAssistant(AssistantCard ass){
        this.ass=ass;
    }

    /**
     *
     * @return the assistant cards
     */
    public AssistantCard getAssistant(){
        return ass;
    }



    /**
     *
     * @param match the match that the player is playing
     */
    public void printMatch(Match match) {
        this.match=match;
        if(!printmatch){
            try {
                MatchController.setmatch(this.match);
                MatchController.setGui(this);
                MatchController.setAction(this.action);
                MatchController.setServer(this.server);
                MatchController.setMe(this.me);
                MatchController.setClientGui(cg);
                stage.setScene(new Scene(game.load()));
                ((MatchController)game.getController()).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            MatchController.setmatch(this.match);
        }
        ((MatchController)game.getController()).wakeUp("Start");
        if(!printmatch) {
            printmatch=true;
            stage.show();
        }
    }

    /**
     * print the end of the match
     * @param winner the player who winn the match
     * @param exp why he win the game
     * @param gr recap of the end
     */
    public void printEndGame(Player winner, String exp, GameRecap gr){
        String finish;
        String grgui;
        grgui=gr.toString();
        if (grgui.contains("\u001B[31m")){
            grgui.replace("\u001B[31m", "drago");
        }
        if (grgui.contains("\u001B[33;1m")){
            grgui.replace("\u001B[33;1m", "gnomo");
        }
        if (grgui.contains("\u001B[35m")){
            grgui.replace("\u001B[35m", "fata");
        }
        if (grgui.contains("\u001B[34m")){
            grgui.replace("\u001B[34m", "unicorno");
        }
        if (grgui.contains("\u001B[32m")){
            grgui.replace("\u001B[32m", "rana");
        }
        finish="Il giocatore "+winner+" ha vinto perchè "+exp+"\n"+grgui;
        popUp("Abbiamo un vincitore!", finish);
        ((MatchController)game.getController()).wakeUp("EndGame");
    }

    /**
     * print who has to do the turn
     * @param pl player who has to do the turn
     * @param phase the phase of the match
     */
    public void printTurn(Player pl, String phase) {
        popUp("Notifica turni", "E' il turno di "+pl.getUserName()+ " in fase di "+phase.toString());
        ((MatchController)game.getController()).setStateLabel("E' il turno di "+pl.getUserName()+ " in fase di "+phase.toString());
        ((MatchController)game.getController()).wakeUp("Next Turn");
    }



    /**
     * set the title on the screen
     */
    public void getTitolo() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("initial_page.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }

    /**
     * wake up the thread of match controller to change state and set the view
     * @param state state of the match
     */
    public void wakeUp(String state) {
        ((MatchController)game.getController()).wakeUp(state);
    }

    /**
     * set who is playing the match
     * @param me the player
     */
    public void setMe(Player me) {
        this.me=me;
    }

    /**
     * set the match that the player is playing
     * @param match the match
     */
    public void setMatch(Match match) {
        this.match=match;
        action=new Action(match);
    }


    /**
     * set visible the cards that you can choose
     * @param cards list of the cards
     */
    public void setCards(List<AssistantCard> cards) {
        this.cards=cards;
        ((MatchController) game.getController()).setVisibleAssCards((List<AssistantCard>) cards);
    }

    /**
     * set the wizard that the player can choose
     * @param willy list of the wizard
     */
    public void setWilly(List<Wizards> willy) {
        this.willy=willy;
    }

    /**
     * set the chose of the match (new game or join game)
     * @param join list of the match you can join to
     * @param resume list of the match you can resume
     */
    public void chooseMatch(List<String> join, List<String> resume) {
        SelectionGameController.setJoin((ArrayList<String>) join);
        SelectionGameController.setResume((ArrayList<String>) resume);
        SelectionGameController.setCl(cg);
        SelectionGameController.setServer(server);
        SelectionGameController.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("selection_game.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }

    /**
     * show the loading on the message
     */
    public void showLoading(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("loading_page.fxml"));
        //Scene scene=new Scene(stage);
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }


    /**
     * change the state to allow the player moving the students
     */
    public void moveStudent() {
        ((MatchController)game.getController()).setStateLabel("E' il tuo turno: scegli uno studente dall'ingresso della tua plancia");
        ((MatchController) game.getController()).refreshEntry();
        ((MatchController)game.getController()).wakeUp("MoveStudent");
        synchronized (cg){
            cg.notifyAll();
        }
    }

    /**
     * change the state and allow moving mother nature
     */
    public void moveMN() {
        ((MatchController)game.getController()).setStateLabel("E' il tuo turno: scegli un isola in cui spostare madre natura");
        ((MatchController)game.getController()).wakeUp("ChooseMN");
        synchronized (cg){
            cg.notifyAll();
        }
    }


    /**
     * show the popup to show who join the game
     * @param username of the player
     */
    public void playerConnected(String username) {
        popUp("Nuovo giocatore connesso!", "Si è connesso "+ username);
    }

    /**
     * show the popup to show who left the game
     * @param username of the player disconnected
     */
    public void playerDisconnected(String username) {
        popUp("Giocatore disconnesso!", "Si è disconnesso "+ username);
    }

    /**
     * show the popup the disconnection
     */
    public void playerDisconnectedAll() {
        popUp("Si sono tutti disconnessi", "Tutti i giocatori si sono disconnessi...");
    }

    /**
     * set the character card activated if the player has enough money
     * @param cards cards of the match
     */
    public void getCharacter(CharacterCard[] cards) {
        this.ch=cards;
        isch1usable=false;
        isch2usable=false;
        isch3usable=false;
        if(((Board_Experts)me.getBoard()).getCoinsNumber()>=ch[0].getPrice()){
            isch1usable=true;
        }
        if(((Board_Experts)me.getBoard()).getCoinsNumber()>=ch[1].getPrice()){
            isch2usable=true;
        }
        if(((Board_Experts)me.getBoard()).getCoinsNumber()>=ch[2].getPrice()){
            isch3usable=true;
        }
        ((MatchController)game.getController()).wakeUp("Ch");
    }

    /**
     * return if a player can use the card in positon y of the array
     * @param y number of the card
     * @return if the player can use the card
     */
    public boolean getUsability(int y){
        switch (y) {
            case 1:
                return isch1usable;
            case 2:
                return isch2usable;
            case 3:
                return isch3usable;
        }
        return false;
    }

    /**
     *
     * @return the character card of the match
     */
    public CharacterCard[] getCh(){
        return ch;
    }

    /**
     * set the character of the match
     * @param ch array of the character
     */
    public void setCharacters(CharacterCard[] ch){
        this.ch=ch;
        ((MatchController)game.getController()).setCharacters(ch);
    }

    /**
     * set the notification
     * @param message to set on the popup
     */
    public void printNotification(String message) {
        popUp("Notifica", message);
    }

    /**
     * set the wizard
     * @param w wizard
     */
    public void setW(Wizards w) {
        this.w = w;
    }

    public void showCard(int i, AssistantCard card){
        ((MatchController)game.getController()).setCardChosenbytheother(i,card);
    }
}
