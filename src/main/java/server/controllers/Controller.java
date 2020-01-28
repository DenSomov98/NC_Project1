package server.controllers;

import server.model.ArtistList;
import server.model.GenreList;
import server.model.Model;
import server.model.TrackList;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Controller {

    private LinkedList<ClientDataHolder> clients = new LinkedList<>();
    private Model model = new Model(new TrackList(), new ArtistList(), new GenreList());

    public void startSever() {
        try (ServerSocket ss = new ServerSocket(1667)) {
            while (true) {
                Socket s = ss.accept();
                new ClientService(s, clients, model).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
