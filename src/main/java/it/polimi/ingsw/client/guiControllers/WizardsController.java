package it.polimi.ingsw.client.guiControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import it.polimi.ingsw.client.Gui;
import it.polimi.ingsw.client.Message4Server;
import it.polimi.ingsw.model.Wizards;
import java.util.List;

public class WizardsController {

    private static Message4Server server;
    private static Gui gui;
    @FXML
    static Button wizard1;
    @FXML
    static Button wizard2;
    @FXML
    static Button wizard3;
    @FXML
    static Button wizard4;

    public static void setServer(Message4Server server) {
        WizardsController.server = server;
    }


    public static void setWilly(List<Wizards> w){
        for(Wizards wi: w){
            switch (wi){
                case WIZARD1 -> wizard1.setVisible(true);
                case WIZARD2 -> wizard2.setVisible(true);
                case WIZARD3 -> wizard3.setVisible(true);
                case WIZARD4 -> wizard4.setVisible(true);
            }
        }
    }

    public static void setGui(Gui gui) {
        WizardsController.gui = gui;
    }


    public void initialize(){
        wizard1.setVisible(false);
        wizard2.setVisible(false);
        wizard3.setVisible(false);
        wizard4.setVisible(false);
    }
    @FXML
    public void chooseWizard(ActionEvent actionEvent){
        switch (((Button)actionEvent.getSource()).getId()){
            case("wizard1"):
                server.sendChoice(Wizards.WIZARD1);
                gui.setW(Wizards.WIZARD1);
                break;
            case ("wizard2"):
                server.sendChoice(Wizards.WIZARD2);
                gui.setW(Wizards.WIZARD2);
                break;
            case ("wizard3"):
                server.sendChoice(Wizards.WIZARD3);
                gui.setW(Wizards.WIZARD3);
                break;
            case ("wizard4"):
                server.sendChoice(Wizards.WIZARD4);
                gui.setW(Wizards.WIZARD4);
                break;
        }
        synchronized (gui){
            gui.notifyAll();
        }
    }

}
