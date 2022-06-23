package it.polimi.ingsw.client;

import it.polimi.ingsw.client.guiControllers.LoginController;
import it.polimi.ingsw.client.guiControllers.MatchController;
import it.polimi.ingsw.client.guiControllers.PopUpController;
import it.polimi.ingsw.client.guiControllers.WizardsController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import it.polimi.ingsw.model.*;


import java.io.IOException;
import java.util.List;

public class Gui extends Application implements View {
    private Stage stage;
    private String state;
    private Boolean end;
    private Message4Server server;
    private Player me;
    private Match match;
    private Action action;
    private List<Wizards> willy;
    private List<Cloud> clouds;
    private List<AssistantCard> cards;
    private CharacterCard[] characters;
    private String username;
    private Wizards w;
    private FXMLLoader game;
    private boolean printmatch;

    public Gui() {
        end=false;
        state="Start";
        username=null;
        w=null;
        printmatch=false;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setStage(stage);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.sizeToScene();
        stage.setTitle("Eryantis");
    }

    public void setUsername(String username){
        this.username=username;
    }

    public static void main(String[] args) {
        launch(args);
        Gui view= new Gui();
        Client client= new Client(view);
        client.start();
        Stage s=new Stage();
        try {
            view.start(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getUsername() {
        LoginController.setServer(server);
        LoginController.setGui(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("login_page.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        }catch (IOException e){
            e.printStackTrace();
        }
        stage.show();
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ((LoginController)fxmlLoader.getController()).getUs();
    }

    @Override
    public void setServer(Message4Server server) {
        this.server=server;
    }

    @Override
    public Wizards getWizard(List<Wizards> wizards) {
        WizardsController.setServer(server);
        WizardsController.setGui(this);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("chooseWizard.fxml"));
        WizardsController.setWilly(wizards);
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
        synchronized (this){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return w;
    }

    @Override
    public Cloud getCloud(List<Cloud> clouds) { //serve solo alla cli
        return null;
    }
    public void popUp(String title, String message){
        Stage s=new Stage();
        PopUpController.setNotify(message);
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getClassLoader().getResource("popup_notify.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(title);
        stage.show();
    }

    @Override
    public AssistantCard getAssistantCard(List<AssistantCard> cards) { //serve solo alla cli
        return null;
    }

    @Override
    public int getNumStep(Player pl) { //serve alla cli
        return 0;
    }

    @Override
    public void getWinner(Player pl) { //da fare

    }

    @Override
    public int getDestination(Match match) { //serve alla cli
        return 0;
    }

    @Override
    public void printMatch(Match match) {
        if(!printmatch){
            game=new FXMLLoader(getClass().getClassLoader().getResource("prova.fxml"));
            ((MatchController)game.getController()).setAction(this.action);
            ((MatchController)game.getController()).setmatch(this.match);
            ((MatchController)game.getController()).setMe(this.me);
            ((MatchController)game.getController()).setServer(this.server);
            ((MatchController)game.getController()).setGui(this);
        }
        ((MatchController)game.getController()).wakeUp("Start");
    }

    @Override
    public void printTurn(Player pl, String phase) {
        popUp("Notifica turni", "E' il turno di "+pl.getUserName()+ "in fase di "+phase.toString());
        ((MatchController)game.getController()).wakeUp("Next Turn");
    }

    @Override
    public void lastRound() { //da fare

    }

    @Override
    public Student getStudent(Player pl) { //serve alla cli
        return null;
    }

    @Override
    public void getTitolo() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("titolo.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }

    @Override
    public void wakeUp(String state) {
        //((MatchController)game.getController()).wakeUp();
    }

    public void setMe(Player me) { //Non so a cosa serva
        this.me=me;
    }

    @Override
    public void setMatch(Match match) {
        this.match=match;
        action=new Action(match);
    }


    @Override
    public void setCards(List<AssistantCard> cards) {
        this.cards=cards;
    }

    @Override
    public void setWilly(List<Wizards> willy) {
        this.willy=willy;
    }

    @Override
    public void setClouds(List<Cloud> clouds) {
        this.clouds=clouds;
    }

    @Override
    public void chooseMatch(List<String> join, List<String> resume) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("selection_game.fxml"));
        Scene scene=null;
        try {
            scene=fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void setNack() { //non sono sicura che serva a qualcosa

    }

    @Override
    public String chooseLogin() {
        //lo fa già in getusername
        return null;
    }

    @Override
    public Land chooseLand(List<Land> lands) { //serve alla cli e ch
        return null;
    }

    @Override
    public Student chooseStudent(List<Student> student) {//serve alla cli e ch
        return null;
    }

    @Override
    public Type_Student chooseColorStudent() { //serve alla cli e ch
        return null;
    }

    @Override
    public void playerConnected(String username) {
        popUp("Nuovo giocatore connesso!", "Si è connesso "+ username);
    }

    @Override
    public void playerDisconnected(String username) {
        popUp("Giocatore disconnesso!", "Si è disconnesso "+ username);
    }

    @Override
    public void playerDisconnectedAll() {
        popUp("Si sono tutti disconnessi", "Tutti i giocatori si sono disconnessi...");
    }

    @Override
    public void finishedAC(Player p) { //da fare

    }

    @Override
    public CharacterCard chooseChCard(CharacterCard[] cards) { //da fare
        return null;
    }

    @Override
    public void setCharacters(CharacterCard[] characters) { //da fare

    }

    @Override
    public void printNotification(String message) {
        popUp("Notifica", message);
    }


    public void setW(Wizards w) {
        this.w = w;
    }
}
