package it.polimi.ingsw.client.guiControllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class PopUpController {

    @FXML
    Text notify;

    public void initialize(){

    }

    /**
     * set the message of the popup
     * @param s string to be added in the popup
     */
    public void setNotify(String s){
        System.out.println("In popupcontroller ho: "+s);
        notify.setText(s);
    }
}
