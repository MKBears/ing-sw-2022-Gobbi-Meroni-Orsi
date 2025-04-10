package it.polimi.ingsw.client.guiControllers;

import it.polimi.ingsw.client.ClientGui;
import it.polimi.ingsw.client.Message4Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

public class TypeMatchController {

    private static ClientGui cl;
    private static Message4Server server;
    @FXML
    public Label error;
    @FXML
    ChoiceBox playernum;
    @FXML
    CheckBox expert;
    @FXML
    Button typematch;

    /**
     * set the client gui
     * @param cl
     */
    public static void setCl(ClientGui cl){
        TypeMatchController.cl=cl;
    }

    /**
     * initialize the page
     */
    public void initialize(){
        playernum.getItems().add("2");
        playernum.getItems().add("3");
        expert.setSelected(false);
        error.setVisible(false);
    }

    /**
     * set the server to send the messages
     * @param server the server to send the messages to
     */
    public static void setServer(Message4Server server){
        TypeMatchController.server=server;
    }

    @FXML
    /**
     * action after choose the number of players and if do an expert match
     */
    public void choosetypematch(ActionEvent actionEvent) {
        if(playernum.getValue().toString()=="2"){
            server.sendNumPlayers(2);
        }else if(playernum.getValue().toString()=="3"){
            server.sendNumPlayers(3);
        }else{
            error.setVisible(true);
        }
        server.sendExpertMatch(expert.isSelected());
        synchronized (cl){
            cl.notifyAll();
        }
    }
}
