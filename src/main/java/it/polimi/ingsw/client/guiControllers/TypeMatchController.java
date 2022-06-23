package it.polimi.ingsw.client.guiControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import it.polimi.ingsw.client.Message4Server;

public class TypeMatchController {

    @FXML
    public Label error;
    @FXML
    ChoiceBox playernum;
    @FXML
    CheckBox expert;
    @FXML
    Button typematch;

    private static Message4Server server;

    public void initialize(){
        playernum.getItems().add("2");
        playernum.getItems().add("3");
        expert.setSelected(false);
        error.setVisible(false);
    }

    public static void setServer(Message4Server server){
        TypeMatchController.server=server;
    }

    @FXML
    public void choosetypematch(ActionEvent actionEvent) {
        if(playernum.getItems().toString().equals("2")){
            server.sendNumPlayers(2);
        }else if(playernum.getItems().toString().equals("3")){
            server.sendNumPlayers(3);
        }else{
            error.setVisible(true);
        }
        server.sendExpertMatch(expert.isSelected());
    }
}
