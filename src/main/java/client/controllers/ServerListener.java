package client.controllers;

//import com.sun.xml.internal.ws.model.WrapperParameter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import worklib.entities.Artist;
import worklib.entities.Genre;
import worklib.entities.Track;
import worklib.entities.Wrapper;
import worklib.parse.Key;
import worklib.transfer.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ServerListener extends Thread {
    private static Stage stage;
    private ObjectInputStream in;
    private Wrapper wrapper;
    private TableView<Track> tableViewTrack;
    private TableView<Artist> tableViewArtist;

    private TableView<Genre> tableViewGenre;

    public static void setStage(Stage stage) {
        ServerListener.stage = stage;
    }

    private TabPane tabPane;
    private Exchanger<Response> exchanger;

    public ServerListener(ObjectInputStream in) {
        this.in = in;
        MainFormController.setServerListener(this);
    }

    public TableView<Track> getTableViewTrack() {
        return tableViewTrack;
    }

    public void setTableViewTrack(TableView<Track> tableViewTrack) {
        this.tableViewTrack = tableViewTrack;
    }

    public void setTableViewGenre(TableView<Genre> tableViewGenre) {
        this.tableViewGenre = tableViewGenre;
    }

    public void setTableViewArtist(TableView<Artist> tableViewArtist) {
        this.tableViewArtist = tableViewArtist;
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void setExchanger(Exchanger<Response> exchanger) {
        this.exchanger = exchanger;
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Response response = (Response)in.readObject();
                System.out.println(response.isObjMatchesNoLongerWarning());
                if (response.getKeys()[0] == Key.ADD || response.getKeys()[0] == Key.GET || response.getKeys()[0] == Key.REMOVE || response.getKeys()[0] == Key.LOAD ||
                        response.getKeys()[0] == Key.EDIT) {
                    if (response.isObjMatchesNoLongerWarning()) {
                        Platform.runLater(() -> {
                            showError("Объект добавлен/изменен, но не отображается из-за параметров фильтрации");
                        });
                    }
                    if (response.hasErrors()) {
                        Platform.runLater(() -> {
                            showError("Такой объект уже существует");
                        });
                    }
                    wrapper = (Wrapper)in.readObject();
                    showTracks();
                    showArtists();
                    showGenres();
                    //Platform.runLater(() -> showTracks(wrapper));
                }
                if (response.getKeys()[0] == Key.LOCK) {
                    try {exchanger.exchange(response, 1500, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (TimeoutException ignored) {
                    }
                }
                if (response.getKeys()[0] == Key.FIND) {
                    Track[] tracks = (Track[])in.readObject();
                    //для проверки пока что в этой же вкладке
                    showFindTracks(tracks);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    showError("Потеря соединения с сервером. Попробуйте подключиться снова");
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = null;
                        root = (Parent) loader.load(getClass().getResourceAsStream("/fxml/entry.fxml"));
                        stage.setTitle("Music library");
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                break;
            }
        }
    }

    public void showError(String info) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(info);
        alert.showAndWait();
    }

    public void showFindTracks(Track[] tracks){
        tableViewTrack.getItems().clear();
        for(Track track: tracks) {
            tableViewTrack.getItems().add(track);
        }
    }

     public void showTracks(){
        tableViewTrack.getItems().clear();
        Track[] tracks = wrapper.getT();
        for(Track track: tracks) {
            tableViewTrack.getItems().add(track);
        }
    }

    public void showArtists(){
        tableViewArtist.getItems().clear();
        Artist[] artists = wrapper.getA();
        for(Artist artist: artists){
            tableViewArtist.getItems().add(artist);
        }
    }

    public void showGenres(){
        tableViewGenre.getItems().clear();
        Genre[] genres = wrapper.getG();
        for(Genre genre: genres){
            tableViewGenre.getItems().add(genre);
        }
    }
}
