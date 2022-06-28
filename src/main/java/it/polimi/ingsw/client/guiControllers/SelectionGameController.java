package it.polimi.ingsw.client.guiControllers;

import it.polimi.ingsw.client.ClientGui;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import it.polimi.ingsw.client.Message4Server;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SelectionGameController {
    private static ArrayList<String> join=new ArrayList<>();
    private static ArrayList<String> resume=new ArrayList<>();
    private static Message4Server server;
    private static ClientGui cl;
    private static Stage stage;
    @FXML
    ChoiceBox selectionGames;

    public static void setServer(Message4Server server) {
        SelectionGameController.server = server;
    }


    public static void setJoin(ArrayList<String> join){
        SelectionGameController.join=join;
    }

    public static void setResume(ArrayList<String> resume){
        SelectionGameController.resume=resume;
    }

    public static void setStage(Stage stage) {
        SelectionGameController.stage = stage;
    }


    public void initialize(){
        System.out.println("Sto inizializzando la scena choosematch");
        if(join!=null) {
            selectionGames.getItems().addAll(join);
        }
        if(resume!=null) {
            selectionGames.getItems().addAll(resume);
        }
    }

    public static void setCl(ClientGui cl){
        SelectionGameController.cl=cl;
    }

    @FXML
    public void gameSelected(){
        System.out.println("Selezionato: una partita iniziata");
        server.sendChoosingGame((String) selectionGames.getValue());
        //cl.getView().setWhoAmI(1);
        synchronized (cl){
            cl.notifyAll();
        }
    }

    @FXML
    public void newGame(){
        System.out.println("Selezionato: newgame");
        server.sendChoosingGame("NewGame");
        //cl.getView().setWhoAmI(0);
        TypeMatchController.setCl(cl);
        TypeMatchController.setServer(server);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("typematch.fxml"));
        try {
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
    }
}
