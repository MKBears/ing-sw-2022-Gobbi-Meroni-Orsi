package it.polimi.ingsw.client.guiControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopUpController {

    @FXML
    Label notify;

    public void setNotify(String s){
        System.out.println("In popupcontroller ho: "+s);
        notify.setText(s);
    }
}
