package it.polimi.ingsw.client.guiControllers;

import it.polimi.ingsw.client.ClientGui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import it.polimi.ingsw.client.Gui;
import it.polimi.ingsw.client.Message4Server;
import it.polimi.ingsw.model.Wizards;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WizardsController {

    private static Message4Server server;
    private static Gui gui;
    private static ClientGui cl;
    @FXML
    public Button w_1;
    @FXML
    public Button w_2;
    @FXML
    public Button w_3;
    @FXML
    public Button w_4;

    /**
     * set the server to send the messages
     * @param server server of the match
     */
    public static void setServer(Message4Server server) {
        WizardsController.server = server;
    }

    /**
     * thread that call this scene
     * @param cl
     */
    public static void setCl(ClientGui cl){
        WizardsController.cl=cl;
    }

    /**
     * set he wizard that you can choose
     * @param w
     */
    public void setWilly(List<Wizards> w){
        w_1.setVisible(false);
        w_2.setVisible(false);
        w_3.setVisible(false);
        w_4.setVisible(false);
        for(Wizards wi: w){
            switch (wi){
                case WIZARD1 -> w_1.setVisible(true);
                case WIZARD2 -> w_2.setVisible(true);
                case WIZARD3 -> w_3.setVisible(true);
                case WIZARD4 -> w_4.setVisible(true);
            }
        }
    }

    public static void setGui(Gui gui) {
        WizardsController.gui = gui;
    }


    public void initialize(){
    }
    @FXML
    /**
     * action of the wizard that yuo choose and send it to the server
     */
    public void chooseWizard(ActionEvent actionEvent){
        switch (((Button)actionEvent.getSource()).getId()){
            case("w_1"):
                System.out.println("Mando mago 1");
                server.sendChoice(Wizards.WIZARD1);
                gui.setW(Wizards.WIZARD1);
                break;
            case ("w_2"):
                server.sendChoice(Wizards.WIZARD2);
                gui.setW(Wizards.WIZARD2);
                break;
            case ("w_3"):
                server.sendChoice(Wizards.WIZARD3);
                gui.setW(Wizards.WIZARD3);
                break;
            case ("w_4"):
                server.sendChoice(Wizards.WIZARD4);
                gui.setW(Wizards.WIZARD4);
                break;
        }
        synchronized (cl){
            cl.notifyAll();
        }
    }

}
