package client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import worklib.parse.Parser;

import java.io.IOException;

public class EntryFormController {

    private static Stage stage;
    private static Controller controller;


    @FXML
    private TextField addressField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectButton;

    public static void setStage(Stage stage) {
        EntryFormController.stage = stage;
    }

    public static void setController(Controller controller) {
        EntryFormController.controller = controller;
    }

    @FXML
    private void initialize(){
        addressField.setText("127.0.0.1");
        portField.setText("1667");
    }

    @FXML
    private void clickConnect() throws IOException {
        int port = Parser.parseID(portField.getText());
        if(port<0)
            showError("Некорректный ввод!");
        else if(!controller.tryToConnect(addressField.getText(), port))
            showError("Ошибка соединения с сервером.");
        else {
            Parent mainWindow = FXMLLoader.load(getClass().getResource("/fxml/mainform.fxml"));
            stage.setScene(new Scene(mainWindow, 600, 415));
            stage.setOnCloseRequest(event -> {controller.disconnect();});
            stage.show();
        }
    }

    private void showError(String info) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }
}
