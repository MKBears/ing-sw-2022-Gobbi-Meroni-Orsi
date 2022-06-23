package it.polimi.ingsw.client.guiControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopUpController {

    @FXML
    static
    Label notify;

    public static void setNotify(String s){
        notify.setText(s);
    }
}
