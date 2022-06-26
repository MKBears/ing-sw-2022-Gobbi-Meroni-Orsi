module gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.management;
    requires org.jetbrains.annotations;

    opens it.polimi.ingsw.client to javafx.fxml;
    opens it.polimi.ingsw.client.guiControllers to javafx.fxml;
    exports it.polimi.ingsw.client;
    exports it.polimi.ingsw.client.guiControllers;
}