package it.polimi.ingsw.client.guiControllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import it.polimi.ingsw.client.Message4Server;

import java.util.ArrayList;

public class SelectionGameController {
    private static ArrayList<String> join=new ArrayList<>();
    private static ArrayList<String> resume=new ArrayList<>();
    @FXML
    ChoiceBox selectionGames;
    private static Message4Server server;

    public static void setServer(Message4Server server) {
        SelectionGameController.server = server;
    }


    public static void setJoin(ArrayList<String> join){
        SelectionGameController.join=join;
    }

    public static void setResume(ArrayList<String> resume){
        SelectionGameController.resume=resume;
    }


    public void initialize(){
        if(join!=null) {
            selectionGames.getItems().addAll(join);
        }
        if(resume!=null) {
            selectionGames.getItems().addAll(resume);
        }
    }

    @FXML
    public void gameSelected(){
        server.sendChoosingGame((String) selectionGames.getValue());
    }

    @FXML
    public void newGame(){
        server.sendChoosingGame("NewGame");
    }
}
