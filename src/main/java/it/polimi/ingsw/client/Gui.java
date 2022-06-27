package it.polimi.ingsw.client;

import it.polimi.ingsw.client.guiControllers.*;
import javafx.application.Application;
import javafx.application.Platform;
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
    //private CharacterCard[] characters;
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
        //time=false;
        //newgame=false;
    }

    public void setCG(ClientGui cg){
        this.cg=cg;
    }
    public void setWhoAmI(int i){
        this.whoami=i;
    }
    public int getWhoAmI(){
        return whoami;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage);
        GuiThread gt=new GuiThread(this);
        gt.start();
        System.out.println("BUONGIORNOOOOO");
        System.out.println("Ho creato il client");
        ClientGui c=new ClientGui(this);
        c.start();
        setCG(c);
        System.out.println("Il client è partito");
    }

    public void setStage(Stage s) throws IOException {
        this.stage = s;
        stage.setTitle("Eryantis");
        FXMLLoader fxml=new FXMLLoader(getClass().getClassLoader().getResource("loading_page.fxml"));
        Scene scene=new Scene(fxml.load());
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public void setUsername(String username){
        this.username=username;
    }

    public static void main(String[] args) {
        System.out.println("BUONGIORNOOOOO");
        launch(args);
    }


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
        //System.out.println("Sono nella gui: "+((LoginController)fxmlLoader.getController()).getUs());
        //us=((LoginController)fxmlLoader.getController()).getUs();
    }

    public void setUs(String us){
        this.us=us;
        cg.setUsername(us);
    }

    public String getUs(){
        return us;
    }

    public void setServer(Message4Server server) {
        this.server=server;
    }


    public void getWizard(List<Wizards> wizards) {
        System.out.println("Sono in getWizard");
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
        System.out.println("Faccio show di getwizards");
        stage.show();
        /*synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }*/
    }

    public Wizards getW(){
        return w;
    }

    public void getCloud() {
        ((MatchController)game.getController()).wakeUp("ChooseCloud");
        synchronized (cg){
            cg.notifyAll();
        }
    }
    public void popUp(String title, String message){
        System.out.println("In popup di GUI ho: "+message);

        //PopUpController.setNotify(message);
        //System.out.println(fxmlLoader.getLocation().toString());
        //try {
            //popup.setScene(new Scene(pup.load()));
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("popup_notify.fxml"));
        try {
            popup.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((PopUpController)fxmlLoader.getController()).setNotify(message);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        popup.setTitle(title);
        popup.setAlwaysOnTop(true);
        popup.show();
    }

    public void getAssistantCard() {
        System.out.println("Sono in getAssistantCard");
        //printMatch(match); //non lo so...
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ((MatchController)game.getController()).setStateLabel("Scegli una carta assistente");
        ((MatchController)game.getController()).wakeUp("ChooseAssistant");
        stage.show();
        /*synchronized (cg){
            cg.notifyAll();
        }*/
    }

    public void setAssistant(AssistantCard ass){
        this.ass=ass;
    }

    public AssistantCard getAssistant(){
        return ass;
    }


    public int getNumStep(Player pl) { //serve alla cli
        return 0;
    }

    public void getWinner(Player pl) { //da fare

    }

    public int getDestination(Match match) { //serve alla cli
        return 0;
    }

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
                //game=new FXMLLoader(getClass().getClassLoader().getResource("real_matchh.fxml"));
                stage.setScene(new Scene(game.load()));
                ((MatchController)game.getController()).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //printmatch=true;
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


    public void printTurn(Player pl, String phase) {
        popUp("Notifica turni", "E' il turno di "+pl.getUserName()+ " in fase di "+phase.toString());
        //MatchController.setStateLabel(phase.toString());
        ((MatchController)game.getController()).setStateLabel("E' il turno di "+pl.getUserName()+ " in fase di "+phase.toString());
        ((MatchController)game.getController()).wakeUp("Next Turn");
        //synchronized (cg){
        //    cg.notifyAll();
        //}
    }


    public void lastRound() { //da fare

    }

    public Student getStudent(Player pl) { //serve alla cli
        return null;
    }


    public void getTitolo() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("initial_page.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }

    public void wakeUp(String state) {
        ((MatchController)game.getController()).wakeUp(state);
    }

    public void setMe(Player me) { //Non so a cosa serva
        this.me=me;
    }


    public void setMatch(Match match) {
        this.match=match;
        action=new Action(match);
    }



    public void setCards(List<AssistantCard> cards) {
        System.out.println("Sono all'inizio di setCards di Gui");
        this.cards=cards;
        //if(!time && newgame) {
        ((MatchController) game.getController()).setVisibleAssCards((List<AssistantCard>) cards);
        //}else time=true;
        System.out.println("Sono alla fine di setCards di Gui");
    }


    public void setWilly(List<Wizards> willy) {
        this.willy=willy;
    }


    public void setClouds(List<Cloud> clouds) {
        this.clouds=clouds;
    }

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

    public void setNack() { //non sono sicura che serva a qualcosa

    }

    public String chooseLogin() {
        //lo fa già in getusername
        return null;
    }

    public Land chooseLand(List<Land> lands) { //serve alla cli e ch
        return null;
    }


    public void moveStudent() {
        ((MatchController)game.getController()).setStateLabel("E' il tuo turno: scegli uno studente dall'ingresso della tua plancia");
        ((MatchController)game.getController()).wakeUp("MoveStudent");
        synchronized (cg){
            cg.notifyAll();
        }
    }

    public void clearStudentFromBoard(Type_Student s, int n_player){
        //((MatchController)game.getController()).clearStudentFromBoard(s, n_player);
    }

    public void moveMN() {
        ((MatchController)game.getController()).setStateLabel("E' il tuo turno: scegli un isola in cui spostare madre natura");
        ((MatchController)game.getController()).wakeUp("ChooseMN");
        synchronized (cg){
            cg.notifyAll();
        }
    }



    public void playerConnected(String username) {
        popUp("Nuovo giocatore connesso!", "Si è connesso "+ username);
    }


    public void playerDisconnected(String username) {
        popUp("Giocatore disconnesso!", "Si è disconnesso "+ username);
    }


    public void playerDisconnectedAll() {
        popUp("Si sono tutti disconnessi", "Tutti i giocatori si sono disconnessi...");
    }


    public void finishedAC(Player p) { //da fare

    }


    public CharacterCard chooseChCard(CharacterCard[] cards) { //da fare
        return null;
    }


    public void setCharacters(CharacterCard[] characters) { //da fare

    }


    public void printNotification(String message) {
        popUp("Notifica", message);
    }


    public void setW(Wizards w) {
        this.w = w;
    }
}
