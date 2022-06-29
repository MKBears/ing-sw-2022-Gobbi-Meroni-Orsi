package it.polimi.ingsw.client.guiControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class PopUpController {

    @FXML
    Text notify;

    public void initialize(){

    }

    public void setNotify(String s){
        System.out.println("In popupcontroller ho: "+s);
        notify.setText(s);
    }
}
