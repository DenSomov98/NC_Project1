package client.controllers;

//import com.sun.xml.internal.ws.model.WrapperParameter;
import javafx.application.Platform;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import worklib.entities.Genre;
import worklib.entities.Track;
import worklib.entities.Wrapper;
import worklib.parse.Key;
import worklib.transfer.Response;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ServerListener extends Thread {
    private ObjectInputStream in;
    private Wrapper wrapper;
    private TableView<Track> tableViewTrack;
    private TableView<Genre> tableViewGenre;
    private TabPane tabPane;

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

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
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
                if (response.getKeys()[0] == Key.ADD || response.getKeys()[0] == Key.REMOVE ||
                        response.getKeys()[0] == Key.LOAD) {
                    wrapper = (Wrapper)in.readObject();
                    showTracks();
                    showGenres();
                    //Platform.runLater(() -> showTracks(wrapper));
                }
                if (response.getKeys()[0] == Key.LOCK) {
                    Response responseEdit = (Response)in.readObject();
                }
                if (response.getKeys()[0] == Key.FIND) {
                    Track[] tracks = (Track[])in.readObject();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

     public void showTracks(){
        tableViewTrack.getItems().clear();
        Track[] tracks = wrapper.getT();
        for(Track track: tracks) {
            tableViewTrack.getItems().add(track);
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
