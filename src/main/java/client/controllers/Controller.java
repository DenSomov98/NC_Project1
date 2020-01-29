package client.controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import javafx.scene.control.Tab;
import worklib.parse.*;
import worklib.transfer.Request;

public class Controller {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public boolean tryToConnect(String host, int port) {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 3000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            new ServerListener(in).start();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void getAllData() {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.GET);
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add("all");
        Request request = new Request(true, keys, arguments);
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestToAddTrack(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.ADD);
        keys.add(Key.TRACK);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToAddGenre(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.ADD);
        keys.add(Key.GENRE);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToAddArtist(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.ADD);
        keys.add(Key.ARTIST);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToRemoveTrack(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.REMOVE);
        keys.add(Key.TRACK);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToRemoveGenre(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.REMOVE);
        keys.add(Key.GENRE);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToRemoveArtist(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.REMOVE);
        keys.add(Key.ARTIST);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToLockTrack(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.LOCK);
        keys.add(Key.TRACK);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToLockGenre(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.LOCK);
        keys.add(Key.GENRE);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToLockArtist(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.LOCK);
        keys.add(Key.ARTIST);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToUnlockTrack(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.UNLOCK);
        keys.add(Key.TRACK);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToUnLockGenre(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.UNLOCK);
        keys.add(Key.GENRE);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToUnLockArtist(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.UNLOCK);
        keys.add(Key.ARTIST);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToEditTrack(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.EDIT);
        keys.add(Key.TRACK);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToEditGenre(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.EDIT);
        keys.add(Key.GENRE);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToEditArtist(ArrayList<String> arguments) throws IOException{
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.EDIT);
        keys.add(Key.ARTIST);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

    public void requestToFindTrack(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.FIND);
        keys.add(Key.TRACK);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }

   /* public void requestToFindGenre(ArrayList<String> arguments) throws IOException {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.FIND);
        keys.add(Key.GENRE);
        Request request = new Request(true, keys, arguments);
        out.writeObject(request);
    }*/

    public void requestToSave(String filename){
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.SAVE);
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(filename);
        Request request = new Request(true, keys, arguments);
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void disconnect() {
        System.out.println("bb");
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.DISCONNECT);
        Request request = new Request(true, keys);
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestToLoadDuplicate(String filename) {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.LOAD);
        keys.add(Key.DUPLICATE);
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(filename);
        Request request = new Request(true, keys, arguments);
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestToLoad(String filename) {
        ArrayList<Key> keys = new ArrayList<>();
        keys.add(Key.LOAD);
        keys.add(Key.OVERWRITE);
        ArrayList<String> arguments = new ArrayList<>();
        arguments.add(filename);
        Request request = new Request(true, keys, arguments);
        try {
            out.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
