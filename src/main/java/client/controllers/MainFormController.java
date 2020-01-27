package client.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Blend;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import server.view.View;
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
    private Button clearButton; //кнопка очистить

    public MainFormController() {
        //  stage.setOnCloseRequest(event -> {controller.disconnect();});
    }

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
        idColumnTrack.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnTrack.setCellValueFactory(new PropertyValueFactory<>("name"));
        artistColumnTrack.setCellValueFactory(new PropertyValueFactory<>("artist"));
        genreColumnTrack.setCellValueFactory(new PropertyValueFactory<>("genre"));
        idColumnGenre.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumnGenre.setCellValueFactory(new PropertyValueFactory<>("name"));
        serverListener.setTableViewTrack(tableTrack);
        serverListener.setTableViewGenre(tableGenres);
        serverListener.setTabPane(tabpane);
        serverListener.setExchanger(exchanger);
        controller.getAllData();
        operationName.setText("");

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
            if(newValue.getText().equals("Жанры")){
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
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        arguments.add(artistField.getText());
        arguments.add(genreField.getText());
        controller.requestToAddTrack(arguments);
    }

    public void requestToAddGenre() throws IOException {
        System.out.println("нажата иконка \"Ок  Добавить Жанр\"");
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        controller.requestToAddGenre(arguments);
    }

    @FXML
    //нажата иконка "Добавить"
    private void clickAdd() throws IOException {
        unHighlightImages();
        InnerShadow shadowEffect = new InnerShadow();
        shadowEffect.setColor(Color.GREEN);
        shadowEffect.setChoke(1);
        addImage.setEffect(shadowEffect);
        if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Треки")) {
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
        }
        if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Жанры")) {
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
        //парметры удаляемого объекта
        /*arguments.add(trackToDelete.getName());
        arguments.add(trackToDelete.getArtist());
        arguments.add(trackToDelete.getGenre());*/
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
        //парметры удаляемого объекта
        arguments.add(genreToDelete.getName());
        controller.requestToRemoveGenre(arguments);
    }

    @FXML
    //нажата иконка "Удалить"
    private void clickRemove() throws IOException {
        unHighlightImages();
        InnerShadow shadowEffect = new InnerShadow();
        shadowEffect.setColor(Color.RED);
        shadowEffect.setChoke(1);
        removeImage.setEffect(shadowEffect);
        System.out.println("нажата иконка \"Удалить\"");
        if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Треки")) {
            requestToRemoveTrack();
        }
        if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Жанры")) {
            requestToRemoveGenre();
        }
    }

    public void requestToLockTrack() throws IOException {
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(String.valueOf(tableTrack.getSelectionModel().getSelectedItem().getId()));
        controller.requestToLockTrack(arguments);
    }

    public void requestToLockGenre() throws IOException {
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(String.valueOf(tableGenres.getSelectionModel().getSelectedItem().getId()));
        controller.requestToLockGenre(arguments);
    }

    public void requestToEditTrack() throws IOException {
        System.out.println("нажата иконка \"Ок  Редактировать Трек\"");
        ArrayList<String> arguments = new ArrayList<>();
        if(tableTrack.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Не выбран трек для редактирования!");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        arguments.add(String.valueOf(tableTrack.getSelectionModel().getSelectedItem().getId()));
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
            alert.setContentText("Не выбран жанр для редактирования!");
            alert.showAndWait();
            removeImage.setEffect(new Blend());
            return;
        }
        arguments.add(String.valueOf(tableGenres.getSelectionModel().getSelectedItem().getId()));
        arguments.add(nameField.getText());
        controller.requestToEditGenre(arguments);
    }

    @FXML
    //нажата иконка "Редактировать"
    private void clickEdit() throws IOException {
        InnerShadow shadowEffect = new InnerShadow();
        shadowEffect.setColor(Color.BLUE);
        shadowEffect.setChoke(1);
        editImage.setEffect(shadowEffect);
        System.out.println("нажата иконка \"Редактировать\"");
        if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Треки")) {
            operationName.setText("Операция: Редактирование трека");
            requestToLockTrack();
            try {
                Response response = exchanger.exchange(null, 1500, TimeUnit.MILLISECONDS);
                System.out.println(response);
                if(response.isAlreadyLockedError()) {
                    new View(null).printError(response);
                    System.out.println("Занято");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Выбранный трек в данный момент запрещен для редактирования. Попробуйте выполнить операцию позже.");
                    alert.showAndWait();
                    return;
                }
                nameField.setVisible(true);
                artistField.setVisible(true);
                genreField.setVisible(true);
                okayButton.setVisible(true);
                okayButton.setOnAction(args-> {
                    try {
                        requestToEditTrack();
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

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException ignored) {
            }
        }
        else if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Жанры")) {
            operationName.setText("Операция: Редактирование жанра");
            requestToLockGenre();
            try {
                Response response = exchanger.exchange(null, 1500, TimeUnit.MILLISECONDS);
                System.out.println(response);
                if(response.hasErrors()) {
                    new View(null).printError(response);
                    System.out.println("Занято");
                    return;
                }
                nameField.setVisible(true);
                okayButton.setVisible(true);
                artistField.setVisible(false);
                genreField.setVisible(false);
                okayButton.setOnAction(args-> {
                    try {
                        requestToEditGenre();
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

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException ignored) {
            }
        }
        /*unHighlightImages();
        artistField.setVisible(false);
        genreField.setVisible(false);
        okayButton.setVisible(false);
        operationName.setText("");*/
    }

    public void requestToFindTrack() throws IOException {
        System.out.println("нажата иконка \"Ок Найти Трек\"");
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        arguments.add(artistField.getText());
        arguments.add(genreField.getText());
        controller.requestToFindTrack(arguments);
    }

    /*public void requestToFindGenre() throws IOException {
        System.out.println("нажата иконка \"Ок Найти Жанр\"");
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(nameField.getText());
        controller.requestToFindGenre(arguments);
    }*/

    @FXML
    //нажата иконка "Поиск"
    private void clickSearch() {
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        /*if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Жанры")) {
            nameField.setVisible(true);
            okayButton.setVisible(true);
            okayButton.setOnAction(args-> {
                try {
                    requestToFindGenre();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }*/
    }

    public void ClickDisconnect(ActionEvent actionEvent) {
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
        okayButton.setOnAction(args -> {
            controller.requestToSave(nameField.getText());
            nameField.clear();
            nameField.setVisible(false);
            okayButton.setVisible(false);
        });
    }

    @FXML
    private void clickLoad() {
        unHighlightImages();
        artistField.setVisible(false);
        genreField.setVisible(false);
        nameField.setVisible(true);
        okayButton.setVisible(true);
        okayButton.setOnAction(args -> {
            controller.requestToLoad(nameField.getText());
            nameField.clear();
            nameField.setVisible(false);
            okayButton.setVisible(false);
        });
    }

    public void clickLoadDuplicate() {
        unHighlightImages();
        artistField.setVisible(false);
        genreField.setVisible(false);
        nameField.setVisible(true);
        okayButton.setVisible(true);
        okayButton.setOnAction(args -> {
            controller.requestToLoadDuplicate(nameField.getText());
            nameField.clear();
            nameField.setVisible(false);
            okayButton.setVisible(false);
        });
    }


    /*@FXML
    private void clickOkay() {
        System.out.println("нажата иконка \"Ок\"");
    }*/
}

