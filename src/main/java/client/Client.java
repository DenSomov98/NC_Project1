package client;

import client.controllers.Controller;
import client.controllers.EntryFormController;
import client.controllers.MainFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/entry.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Music library");
        stage.setScene(new Scene(root));
        stage.show();
        EntryFormController.setStage(stage);
        Controller controller = new Controller();
        EntryFormController.setController(controller);
        MainFormController.setController(controller);

    }
}
