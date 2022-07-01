package it.polimi.ingsw.client.guiControllers;

import it.polimi.ingsw.client.ClientGui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import it.polimi.ingsw.client.Gui;
import it.polimi.ingsw.client.Message4Server;

import java.io.IOException;
import java.io.ObjectInputStream;

public class LoginController{


    private String us;
    private static Gui gui;
    private static Message4Server server;
    private static ClientGui cl;

    @FXML
    public CheckBox newlogin;
    @FXML
    Button send_username;
    @FXML
    TextField username;
    @FXML
    Label result_username;

    /**
     * set the client gui
     * @param cl
     */
    public static void setCl(ClientGui cl){
        LoginController.cl=cl;
    }

    /**
     * set the server to send message of the login
     * @param server
     */
    public static void setServer(Message4Server server){
        LoginController.server=server;
    }

    /**
     * set the gui that call this method
     * @param gui
     */
    public static void setGui(Gui gui) {
        LoginController.gui = gui;
    }

    /**
     * initialize the scene
     */
    public void initialize() {
        result_username.setText("Scegleire l'username");
        result_username.setVisible(true);
        newlogin.setSelected(false);
        us=null;
    }

    @FXML
    /**
     * action after the click of the button
     */
    public void username(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        us = username.getCharacters().toString();
        if (newlogin.isSelected()) {
            server.sendRegistration(us.toLowerCase());
        } else {
            server.sendLogin(us);
        }
        gui.setUs(us);
        synchronized (cl){
            cl.notifyAll();
        }
    }

}
