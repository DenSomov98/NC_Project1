package client.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Blend;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import worklib.entities.Artist;
import worklib.entities.Genre;
import worklib.entities.Track;
import worklib.entities.Wrapper;
import worklib.parse.Key;
import worklib.transfer.Request;
import worklib.transfer.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainFormController {

    private static Stage stage;
    private static Controller controller;
    private static ServerListener serverListener;
    private static Exchanger<Response> exchanger = new Exchanger<>();

    @FXML
    private MenuBar menuBar;
    @FXML
    private TabPane tabpane; //панель, разделяющая таблицы
    @FXML
    private TableView<Track> tableTrack; //таблица треков
    @FXML
    private TableColumn<Track, Integer> idColumnTrack;
    @FXML
    private TableColumn<Track, String> nameColumnTrack;
    @FXML
    private TableColumn<Track, String> artistColumnTrack;
    @FXML
    private TableColumn<Track, String> genreColumnTrack;

    @FXML
    private TableView<Genre> tableGenres; // таблица жанров
    @FXML
    private TableColumn<Genre, Integer> idColumnGenre;
    @FXML
    private TableColumn<Genre, String> nameColumnGenre;

    @FXML
    private TableView<Artist> tableArtist; // таблица жанров
    @FXML
    private TableColumn<Artist, Integer> idColumnArtist;
    @FXML
    private TableColumn<Artist, String> nameColumnArtist;

    @FXML
    private TextField nameField; //поле названия
    @FXML
    private TextField artistField;//поле исполнителя
    @FXML
    private TextField genreField; //поле жанра
    @FXML
    private Button okayButton;

    @FXML
    private ImageView addImage; //значок добавления
    @FXML
    private ImageView editImage;//значок редактирования
    @FXML
    private ImageView removeImage;//значок удаления
    @FXML
    private ImageView searchImage;//значок поиска
    @FXML
    private Label operationName;//название операции
    @FXML
    private Button unDoSearchButton; //кнопка возврата к первоначальным данным

    public MainFormController() {}

    public TabPane getTabPane() {
        return tabpane;
    }

    public TableView getTableGenres() {
        return tableGenres;
    }

    public TableView getTableTrack() {
        return tableTrack;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getArtistField() {
        return artistField;
    }

    public TextField getGenreField() {
        return genreField;
    }

    public static void setStage(Stage stage) { MainFormController.stage = stage; }

    public static void setController(Controller controller) {
        MainFormController.controller = controller;
    }

    public static ServerListener getServerListener() {
        return serverListener;
    }

    public static void setServerListener(ServerListener serverListener) {
        MainFormController.serverListener = serverListener;
    }

    @FXML
    private void initialize() {
        nameField.setVisible(false);
        artistField.setVisible(false);
        genreField.setVisible(false);
        okayButton.setVisible(false);
        unDoSearchButton.setVisible(false);
        idColumnTrack.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnTrack.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistColumnTrack.setCellValueFactory(new PropertyValueFactory<>("artist"));
        genreColumnTrack.setCellValueFactory(new PropertyValueFactory<>("genre"));
        idColumnGenre.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnGenre.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumnArtist.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnArtist.setCellValueFactory(new PropertyValueFactory<>("name"));
        serverListener.setTableViewTrack(tableTrack);
        serverListener.setTableViewGenre(tableGenres);
        serverListener.setTableViewArtist(tableArtist);
        serverListener.setTabPane(tabpane);
        serverListener.setExchanger(exchanger);
        serverListener.setStage(stage);
        controller.getAllData();
        operationName.setText("");

        stage.setOnCloseRequest(event -> {
            serverListener.setDisconnecting(true);
            controller.disconnect();});

        //смена полей ввода при переключении вкладок
        tabpane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            unHighlightImages();
            nameField.clear();
            artistField.clear();
            genreField.clear();
            nameField.setVisible(false);
            artistField.setVisible(false);
            genreField.setVisible(false);
            okayButton.setVisible(false);
            operationName.setText("");
            if(newValue.getText().equals("Жанры") || newValue.getText().equals("Исполнители")){
                searchImage.setVisible(false);
                searchImage.setDisable(true);
            }
            else {
                searchImage.setVisible(true);
                searchImage.setDisable(false);
            }
                /*if (newValue.getText().equals("Треки")) {
                    tabpane.getSelectionModel().select(0);
                    nameField.setVisible(true);
                    artistField.setVisible(true);
                    genreField.setVisible(true);
                } else if (newValue.getText().equals("Жанры")) {
                    tabpane.getSelectionModel().select(1);
                    nameField.setVisible(true);
                    artistField.setVisible(false);
                    genreField.setVisible(false);
                }*/
        });
    }

    public void requestToAddTrack() throws IOException {
        System.out.println("нажата иконка \"Ок Добавить Трек\"");
        if(nameField.getText().length() == 0){
            callAlertEmptyFields();
            return;
        }
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        arguments.add(artistField.getText());
        arguments.add(genreField.getText());
        controller.requestToAddTrack(arguments);
    }

    public void requestToAddGenre() throws IOException {
        System.out.println("нажата иконка \"Ок  Добавить Жанр\"");
        if(nameField.getText().length() == 0){
            callAlertEmptyFields();
            return;
        }
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        controller.requestToAddGenre(arguments);
    }

    public void requestToAddArtist() throws IOException {
        System.out.println("нажата иконка \"Ок  Добавить Исполнителя\"");
        if(nameField.getText().length() == 0){
            callAlertEmptyFields();
            return;
        }
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        controller.requestToAddArtist(arguments);
    }

    @FXML
    //нажата иконка "Добавить"
    private void clickAdd() {
        nameField.clear();
        artistField.clear();
        genreField.clear();
        unHighlightImages();
        InnerShadow shadowEffect = new InnerShadow();
        shadowEffect.setColor(Color.GREEN);
        shadowEffect.setChoke(1);
        addImage.setEffect(shadowEffect);
        switch (tabpane.getSelectionModel().getSelectedItem().getText()){
            case "Треки":
                operationName.setText("Операция: Добавление трека");
                nameField.setVisible(true);
                artistField.setVisible(true);
                genreField.setVisible(true);
                okayButton.setVisible(true);
                okayButton.setOnAction(args -> {
                    try {
                        requestToAddTrack();
                        nameField.clear();
                        artistField.clear();
                        genreField.clear();
                        unHighlightImages();
                        nameField.setVisible(false);
                        artistField.setVisible(false);
                        genreField.setVisible(false);
                        okayButton.setVisible(false);
                        operationName.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "Жанры":
                operationName.setText("Операция: Добавление жанра");
                nameField.setVisible(true);
                okayButton.setVisible(true);
                okayButton.setOnAction(args -> {
                    try {
                        requestToAddGenre();
                        nameField.clear();
                        unHighlightImages();
                        nameField.setVisible(false);
                        okayButton.setVisible(false);
                        operationName.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "Исполнители":
                operationName.setText("Операция: Добавление исполнителя");
                nameField.setVisible(true);
                okayButton.setVisible(true);
                okayButton.setOnAction(args -> {
                    try {
                        requestToAddArtist();
                        nameField.clear();
                        unHighlightImages();
                        nameField.setVisible(false);
                        okayButton.setVisible(false);
                        operationName.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
        System.out.println("нажата иконка \"Добавить\"");
    }

    private void unHighlightImages() {
        addImage.setEffect(new Blend());
        removeImage.setEffect(new Blend());
        editImage.setEffect(new Blend());
        searchImage.setEffect(new Blend());
    }

    public void requestToRemoveTrack() throws IOException {
        Track trackToDelete = tableTrack.getSelectionModel().getSelectedItem();
        if(trackToDelete == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не выбран трек для удаления!");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(String.valueOf(trackToDelete.getId()));
        controller.requestToRemoveTrack(arguments);
    }

    public void requestToRemoveGenre() throws IOException {
        Genre genreToDelete = tableGenres.getSelectionModel().getSelectedItem();
        if(genreToDelete == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не выбран жанр для удаления!");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(genreToDelete.getName());
        controller.requestToRemoveGenre(arguments);
    }

    public void requestToRemoveArtist() throws IOException {
        Artist artistToDelete = tableArtist.getSelectionModel().getSelectedItem();
        if(artistToDelete == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не выбран исполнитель для удаления!");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(artistToDelete.getName());
        controller.requestToRemoveArtist(arguments);
    }

    @FXML
    private void clickRemove() throws IOException {
        unHighlightImages();
        InnerShadow shadowEffect = new InnerShadow();
        shadowEffect.setColor(Color.RED);
        shadowEffect.setChoke(1);
        removeImage.setEffect(shadowEffect);
        System.out.println("нажата иконка \"Удалить\"");
        switch (tabpane.getSelectionModel().getSelectedItem().getText()){
            case "Треки":
                requestToRemoveTrack();
                break;
            case "Жанры":
                requestToRemoveGenre();
                break;
            case "Исполнители":
                requestToRemoveArtist();
                break;
        }
    }

    public void requestToLockTrack() throws IOException {
        ArrayList<String> arguments = new ArrayList<>();
        Track trackToLock = tableTrack.getSelectionModel().getSelectedItem();
        if(trackToLock == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не выбран трек для редактирования!");
            alert.showAndWait();
            editImage.setEffect(new Blend());
            setDisableAllControlElements(false);
            operationName.setText("");
            return;
        }
        arguments.add(String.valueOf(tableTrack.getSelectionModel().getSelectedItem().getId()));
        controller.requestToLockTrack(arguments);
    }

    public void requestToLockGenre() throws IOException {
        ArrayList<String> arguments = new ArrayList<>();
        Genre genreToEdit = tableGenres.getSelectionModel().getSelectedItem();
        if(genreToEdit == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не выбран жанр для редактирования!");
            alert.showAndWait();
            editImage.setEffect(new Blend());
            setDisableAllControlElements(false);
            operationName.setText("");
            return;
        }
        arguments.add(String.valueOf(tableGenres.getSelectionModel().getSelectedItem().getId()));
        controller.requestToLockGenre(arguments);
    }

    public void requestToLockArtist() throws IOException {
        ArrayList<String> arguments = new ArrayList<>();
        Artist artistToEdit = tableArtist.getSelectionModel().getSelectedItem();
        if(artistToEdit == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не выбран исполнитель для редактирования!");
            alert.showAndWait();
            editImage.setEffect(new Blend());
            setDisableAllControlElements(false);
            operationName.setText("");
            return;
        }
        arguments.add(String.valueOf(tableArtist.getSelectionModel().getSelectedItem().getId()));
        controller.requestToLockArtist(arguments);
    }

    public void requestToEditTrack() throws IOException {
        System.out.println("нажата иконка \"Ок  Редактировать Трек\"");
        ArrayList<String> arguments = new ArrayList<>();
        if(tableTrack.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Похоже, выбранный трек уже удалён.");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        arguments.add(String.valueOf(tableTrack.getSelectionModel().getSelectedItem().getId()));
        if(nameField.getText().length() == 0){
            callAlertEmptyFields();
            controller.requestToUnlockTrack(arguments);
            return;
        }
        arguments.add(nameField.getText());
        arguments.add(artistField.getText());
        arguments.add(genreField.getText());
        controller.requestToEditTrack(arguments);
    }

    public void requestToEditGenre() throws IOException {
        System.out.println("нажата иконка \"Ок  Редактировать Жанр\"");
        ArrayList<String> arguments = new ArrayList<>();
        if(tableGenres.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Похоже, выбранный жанр уже удалён.");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        arguments.add(String.valueOf(tableGenres.getSelectionModel().getSelectedItem().getId()));
        if(nameField.getText().length() == 0){
            callAlertEmptyFields();
            controller.requestToUnLockGenre(arguments);
            return;
        }
        arguments.add(nameField.getText());
        controller.requestToEditGenre(arguments);
    }

    public void requestToEditArtist() throws IOException {
        System.out.println("нажата иконка \"Ок  Редактировать Исполнителя\"");
        ArrayList<String> arguments = new ArrayList<>();
        if(tableArtist.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Похоже, выбранный исполнитель уже удалён.");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        arguments.add(String.valueOf(tableArtist.getSelectionModel().getSelectedItem().getId()));
        if(nameField.getText().length() == 0){
            callAlertEmptyFields();
            controller.requestToUnLockArtist(arguments);
            return;
        }
        arguments.add(nameField.getText());
        controller.requestToEditArtist(arguments);
    }

    @FXML
    //нажата иконка "Редактировать"
    private void clickEdit() throws IOException {
        setDisableAllControlElements(true);
        InnerShadow shadowEffect = new InnerShadow();
        shadowEffect.setColor(Color.BLUE);
        shadowEffect.setChoke(1);
        editImage.setEffect(shadowEffect);
        System.out.println("нажата иконка \"Редактировать\"");
        switch (tabpane.getSelectionModel().getSelectedItem().getText()){
            case "Треки":
                operationName.setText("Операция: Редактирование трека");
                requestToLockTrack();
                if(tableTrack.getSelectionModel().getSelectedItem() == null)
                    return;
                try {
                    Response response = exchanger.exchange(null, 1500, TimeUnit.MILLISECONDS);
                    if(response.isAlreadyLockedError()) {
                        System.out.println("Занято");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Выбранный трек в данный момент запрещен для редактирования. Попробуйте выполнить операцию позже.");
                        alert.showAndWait();
                        setDisableAllControlElements(false);
                        unHighlightImages();
                        okayButton.setVisible(false);
                        operationName.setText("");
                        return;
                    }
                    nameField.setVisible(true);
                    artistField.setVisible(true);
                    genreField.setVisible(true);
                    okayButton.setVisible(true);
                    nameField.setText(tableTrack.getSelectionModel().getSelectedItem().getName());
                    artistField.setText(tableTrack.getSelectionModel().getSelectedItem().getArtist());
                    genreField.setText(tableTrack.getSelectionModel().getSelectedItem().getGenre());
                    okayButton.setOnAction(args-> {
                        try {
                            setDisableAllControlElements(false);
                            unHighlightImages();
                            okayButton.setVisible(false);
                            operationName.setText("");
                            if(nameField.getText().equals(tableTrack.getSelectionModel().getSelectedItem().getName()) && artistField.getText().equals(tableTrack.getSelectionModel().getSelectedItem().getArtist()) && genreField.getText().equals(tableTrack.getSelectionModel().getSelectedItem().getGenre())){
                                ArrayList<String> arguments = new ArrayList<>();
                                arguments.add(String.valueOf(tableTrack.getSelectionModel().getSelectedItem().getId()));
                                controller.requestToUnlockTrack(arguments);
                                nameField.setVisible(false);
                                artistField.setVisible(false);
                                genreField.setVisible(false);
                                return;
                            }
                            nameField.setVisible(false);
                            artistField.setVisible(false);
                            genreField.setVisible(false);
                            requestToEditTrack();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (TimeoutException ignored) {
                }
                catch (NullPointerException ignored) {
                }
                break;
            case "Жанры":
                operationName.setText("Операция: Редактирование жанра");
                requestToLockGenre();
                if(tableGenres.getSelectionModel().getSelectedItem() == null)
                    return;
                try {
                    Response response = exchanger.exchange(null, 1500, TimeUnit.MILLISECONDS);
                    System.out.println(response);
                    if(response.isAlreadyLockedError()) {
                        System.out.println("Занято");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Выбранный жанр в данный момент запрещен для редактирования. Попробуйте выполнить операцию позже.");
                        alert.showAndWait();
                        setDisableAllControlElements(false);
                        unHighlightImages();
                        okayButton.setVisible(false);
                        operationName.setText("");
                        return;
                    }
                    nameField.setVisible(true);
                    okayButton.setVisible(true);
                    artistField.setVisible(false);
                    genreField.setVisible(false);
                    nameField.setText(tableGenres.getSelectionModel().getSelectedItem().getName());
                    okayButton.setOnAction(args-> {
                        try {
                            setDisableAllControlElements(false);
                            unHighlightImages();
                            okayButton.setVisible(false);
                            operationName.setText("");
                            if(nameField.getText().equals(tableGenres.getSelectionModel().getSelectedItem().getName())){
                                ArrayList<String> arguments = new ArrayList<>();
                                arguments.add(String.valueOf(tableGenres.getSelectionModel().getSelectedItem().getId()));
                                controller.requestToUnLockGenre(arguments);
                                nameField.setVisible(false);
                                return;
                            }
                            nameField.setVisible(false);
                            requestToEditGenre();
                        } catch (IOException | NullPointerException ignored) {
                        }
                    });

                } catch (InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                    return;
                }
                break;
            case "Исполнители":
                operationName.setText("Операция: Редактирование исполнителя");
                requestToLockArtist();
                if(tableArtist.getSelectionModel().getSelectedItem() == null)
                    return;
                try {
                    Response response = exchanger.exchange(null, 1500, TimeUnit.MILLISECONDS);
                    System.out.println(response);
                    if(response.isAlreadyLockedError()) {
                        System.out.println("Занято");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Выбранный исполнитель в данный момент запрещен для редактирования. Попробуйте выполнить операцию позже.");
                        alert.showAndWait();
                        setDisableAllControlElements(false);
                        unHighlightImages();
                        okayButton.setVisible(false);
                        operationName.setText("");
                        return;
                    }
                    nameField.setVisible(true);
                    okayButton.setVisible(true);
                    artistField.setVisible(false);
                    genreField.setVisible(false);
                    nameField.setText(tableArtist.getSelectionModel().getSelectedItem().getName());
                    okayButton.setOnAction(args-> {
                        try {
                            setDisableAllControlElements(false);
                            unHighlightImages();
                            okayButton.setVisible(false);
                            operationName.setText("");
                            if(nameField.getText().equals(tableArtist.getSelectionModel().getSelectedItem().getName())){
                                ArrayList<String> arguments = new ArrayList<>();
                                arguments.add(String.valueOf(tableArtist.getSelectionModel().getSelectedItem().getId()));
                                controller.requestToUnLockArtist(arguments);
                                nameField.setVisible(false);
                                return;
                            }
                            nameField.setVisible(false);
                            requestToEditArtist();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (InterruptedException | TimeoutException | NullPointerException ignored) { }
        }
    }

    private void setDisableAllControlElements(boolean flag){
        addImage.setDisable(flag);
        removeImage.setDisable(flag);
        searchImage.setDisable(flag);
        switch (tabpane.getSelectionModel().getSelectedItem().getText()){
            case "Треки": {
                tabpane.getTabs().get(1).setDisable(flag);
                tabpane.getTabs().get(2).setDisable(flag);
                break;
            }
            case "Жанры":{
                tabpane.getTabs().get(0).setDisable(flag);
                tabpane.getTabs().get(1).setDisable(flag);
                break;
            }
            case "Исполнители": {
                tabpane.getTabs().get(0).setDisable(flag);
                tabpane.getTabs().get(2).setDisable(flag);
                break;
            }
        }
        menuBar.setDisable(flag);
    }

    public void requestToFindTrack() throws IOException {
        System.out.println("нажата иконка \"Ок Найти Трек\"");
        if(nameField.getText().length() == 0 || artistField.getText().length() == 0 || genreField.getText().length() == 0){
            callAlertEmptyFields();
            return;
        }
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        arguments.add(artistField.getText());
        arguments.add(genreField.getText());
        controller.requestToFindTrack(arguments);
    }

    @FXML
    //нажата иконка "Поиск"
    private void clickSearch() {
        nameField.clear();
        artistField.clear();
        genreField.clear();
        System.out.println("нажата иконка \"Поиск\"");
        InnerShadow shadowEffect = new InnerShadow();
        shadowEffect.setColor(Color.BLUE);
        shadowEffect.setChoke(1);
        searchImage.setEffect(shadowEffect);
        if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Треки")) {
            operationName.setText("Операция: Поиск трека");
            nameField.setVisible(true);
            artistField.setVisible(true);
            genreField.setVisible(true);
            okayButton.setVisible(true);
            okayButton.setOnAction(args -> {
                try {
                    requestToFindTrack();
                    unHighlightImages();
                    nameField.setVisible(false);
                    artistField.setVisible(false);
                    genreField.setVisible(false);
                    okayButton.setVisible(false);
                    operationName.setText("");
                    unDoSearchButton.setVisible(true);
                    unDoSearchButton.setOnAction(arg ->{
                        controller.getAllData();
                        unDoSearchButton.setVisible(false);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void ClickDisconnect(ActionEvent actionEvent) throws IOException {
        String fxmlFile = "/fxml/entry.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        controller.disconnect();
        serverListener.setDisconnecting(true);
        stage.setOnCloseRequest(event -> {System.exit(0);});
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void ClickExit(ActionEvent actionEvent) {
        controller.disconnect();
        System.exit(0);
    }

    @FXML
    //нажат пункт меню "Файл - Сохранить"
    private void clickSave() {
        unHighlightImages();
        artistField.setVisible(false);
        genreField.setVisible(false);
        System.out.println("нажат пункт меню Файл - Сохранить");
        nameField.setVisible(true);
        okayButton.setVisible(true);
        operationName.setText("Операция: Сохранение в файл на сервере.");
        okayButton.setOnAction(args -> {
            if(nameField.getText().length() == 0){
                callAlertEmptyFields();
                return;
            }
            controller.requestToSave(nameField.getText());
            nameField.clear();
            nameField.setVisible(false);
            okayButton.setVisible(false);
            operationName.setText("");
        });
    }

    @FXML
    private void clickLoad() {
        unHighlightImages();
        artistField.setVisible(false);
        genreField.setVisible(false);
        nameField.setVisible(true);
        okayButton.setVisible(true);
        operationName.setText("Операция: Загрузка данных из файла.");
        okayButton.setOnAction(args -> {
            if(nameField.getText().length() == 0){
                callAlertEmptyFields();
                return;
            }
            controller.requestToLoad(nameField.getText());
            nameField.clear();
            nameField.setVisible(false);
            okayButton.setVisible(false);
            operationName.setText("");
        });
    }

    public void clickLoadDuplicate() {
        unHighlightImages();
        artistField.setVisible(false);
        genreField.setVisible(false);
        nameField.setVisible(true);
        okayButton.setVisible(true);
        operationName.setText("Операция: Загрузка данных из файла (с дубликатами).");
        okayButton.setOnAction(args -> {
            if(nameField.getText() == ""){
                callAlertEmptyFields();
                return;
            }
            controller.requestToLoadDuplicate(nameField.getText());
            nameField.clear();
            nameField.setVisible(false);
            okayButton.setVisible(false);
            operationName.setText("");
        });
    }

    public void callAlertEmptyFields(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Одно или несколько полей ввода пустые. Повторите ввод.");
        alert.showAndWait();
    }
}

