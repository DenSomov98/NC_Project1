package server.controllers;

import server.model.Model;
import server.model.TrackList;
import worklib.entities.Track;
import worklib.entities.Wrapper;
import worklib.parse.*;
import worklib.transfer.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class ClientService extends Thread {

    private Socket socket;
    private LinkedList<ClientDataHolder> clients;
    private Model model;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ClientDataHolder client;


    public ClientService(Socket s, LinkedList<ClientDataHolder> clients, Model model) throws IOException {
        this.socket = s;
        this.clients = clients;
        this.model = model;
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.client = new ClientDataHolder(out);
        clients.add(client);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = (Request) in.readObject();
                if (request.getKeys()[0] == Key.DISCONNECT)
                    break;
                else
                    processing(request);
            }
            clients.remove(client);
            model.unlockByID(client.getId());
            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            model.unlockByID(client.getId());
            ex.printStackTrace();
        }
    }

    private void processing(Request request) throws IOException {
        Response response = model.validate(request, client.getId());
        Key[] keys = response.getKeys();
        switch (keys[0]) {
            case GET:
                client.setInSearch(false);
                client.send(response);
                client.send(model.getAllData());
                break;
            case SAVE:
            case LOCK:
                if(!response.hasErrors())
                    model.execute(response);
                client.send(response);
                break;
            case FIND:
                client.setSearchCriteria(response.getArguments());
                client.setInSearch(true);
                client.send(response);
                client.send(model.findTracks(response));
                break;
            case ADD:
            case EDIT:
                if(keys[1] == Key.TRACK && client.isInSearch() &&
                        !TrackList.isMatches(response.getArguments(), client.getSearchCriteria()))
                    response.setObjMatchesNoLongerWarning(true);
                if (response.hasErrors()) client.send(response);
                model.execute(response);
                synchronized (clients) {
                    sendResponseToAll(response);
                    sendDataUpdateToAll();
                }
                break;
            default:
                model.execute(response);
                synchronized (clients) {
                    sendResponseToAll(response);
                    sendDataUpdateToAll();
                }
        }
    }

    private void sendResponseToAll(Response response) throws IOException {
        for (ClientDataHolder client : clients)
            client.send(response);
    }

    private void sendDataUpdateToAll() throws IOException {
        Wrapper data = model.getAllData();
        for (ClientDataHolder client : clients)
            if(client.isInSearch())
                client.send(getMatches(client, data));
            else
                client.send(data);
    }

    private Wrapper getMatches(ClientDataHolder client, Wrapper data) {
        Wrapper result = new Wrapper();
        result.setGenres(data.getG());
        // TODO: 28.01.2020 result.setArtists(data.getA());
        Track[] tracks = data.getT();
        String[] searchCriteria = client.getSearchCriteria();
        result.setTracks(TrackList.find(tracks, searchCriteria));
        return result;
    }

}
