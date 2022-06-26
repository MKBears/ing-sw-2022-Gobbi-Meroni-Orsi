package it.polimi.ingsw.client.guiControllers;

import it.polimi.ingsw.client.ClientGui;
import it.polimi.ingsw.model.Type_Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import it.polimi.ingsw.client.Message4Server;

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

    public static void setCl(ClientGui cl){
        TypeMatchController.cl=cl;
    }

    public void initialize(){
        playernum.getItems().add("2");
        playernum.getItems().add("3");
        //playernum.setValue("2");
        expert.setSelected(false);
        error.setVisible(false);
    }

    public static void setServer(Message4Server server){
        TypeMatchController.server=server;
    }

    @FXML
    public void choosetypematch(ActionEvent actionEvent) {
        //System.out.println(playernum.getItems());
        if(playernum.getValue().toString()=="2"){
            server.sendNumPlayers(2);
            System.out.println("Ho mandato numero giocatori: 2");
        }else if(playernum.getValue().toString()=="3"){
            server.sendNumPlayers(3);
            System.out.println("Ho mandato numero giocatori: 2");
        }else{
            error.setVisible(true);
            System.out.println("Errore in send num players");
        }
        server.sendExpertMatch(expert.isSelected());
        System.out.println("Ho mandato expert match: "+ expert.isSelected());
        System.out.println("Ho mandato tutto per newgame");
        synchronized (cl){
            cl.notifyAll();
        }
    }
}
