package it.polimi.ingsw.client.guiControllers;

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

public class LoginController {


    private String us;
    private static Gui gui;
    private static Message4Server server;
    private static ObjectInputStream in;

    @FXML
    public CheckBox newlogin;
    @FXML
    Button send_username;
    @FXML
    TextField username;
    @FXML
    Label result_username;


    public static void setServer(Message4Server server){
        LoginController.server=server;
    }

    public static void setGui(Gui gui) {
        LoginController.gui = gui;
    }

    public String getUs(){
        return us;
    }


    public void initialize() {
        result_username.setText("Scegleire l'username");
        result_username.setVisible(true);
        newlogin.setSelected(false);
    }


    @FXML
    public void username(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        us = username.getCharacters().toString();
        if (newlogin.isSelected()) {
            server.sendRegistration(us);
        } else {
            server.sendLogin(us);
        }
        synchronized (gui){
            gui.notifyAll();
        }
       /* String response= (String)in.readObject();
        if(response.equals("LoginFailed")){
            result_username.setText("Username errato.. Riprova");
        }else{*/
            /*ArrayList<String> join=new ArrayList<>();
            join=(ArrayList<String>) in.readObject();
            ArrayList<String> resume=new ArrayList<>();
            resume=(ArrayList<String>) in.readObject();
            join.addAll(resume);
            selectionGames.getItems().addAll(join);
            login_page.setVisible(false);
            first.setVisible(true);
    }*/

    }

}
