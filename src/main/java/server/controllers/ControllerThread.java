package server.controllers;

import server.model.Model;
import worklib.entities.Wrapper;
import worklib.parse.*;
import worklib.transfer.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

public class ControllerThread extends Thread{

    private Socket socket;
    private LinkedList<ObjectOutputStream> clients;
    private Model model;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ControllerThread(Socket s, LinkedList<ObjectOutputStream> clients, Model model) {
        this.socket = s;
        this.clients = clients;
        this.model = model;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            clients.add(out);
            while (true) {
                Request request = (Request) in.readObject();
                if(request.getKeys()[0] == Key.DISCONNECT) {
                    break;
                }
                else {
                    System.out.println(request.getKeys()[0]);
                    processing(request);
                }
            }
            clients.remove(out);
            //System.out.println("eliminated");
            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void processing(Request request) throws IOException {
        Response response = model.validate(request);
        System.out.println(response.getKeys()[0]);
        if (request.getKeys()[0] == Key.GET) {
            out.writeObject(response);
            out.writeObject(model.getAllData());
        }
        if (request.getKeys()[0] == Key.SAVE) {
            out.writeObject(response);
        }
        if (request.getKeys()[0] == Key.FIND) {
            out.writeObject(response);
            out.writeObject(model.findTracks(response));
        }
        if (request.getKeys()[0] == Key.LOCK) {
            out.writeObject(response);
        }
        else {
            model.execute(response);
            //System.out.println(response.getKeys()[0]);
            synchronized (clients) {
                for(ObjectOutputStream out : clients)
                    out.writeObject(response);
            }
            sendToAll();
        }
    }

    private void sendToAll() throws IOException {
        synchronized (clients) {
            Wrapper data = model.getAllData();
            for(ObjectOutputStream out : clients)
                out.writeObject(data);
        }
    }

}
