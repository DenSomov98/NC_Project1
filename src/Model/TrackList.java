package Model;


import Controller.InputDataHolder;
import Parse.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;


public class TrackList implements Tracks, Serializable {
    private LinkedList<Track> tracks = new LinkedList<Track>();

    public TrackList () {}

    public OutputDataHolder validateAddTrack(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (arguments.length == 3 && arguments[2] == null) {
            outputDataHolder.setTrackWithoutGenreWarning(true);
        }
        return outputDataHolder;
    }

    @Override
    public void addTrack(String name, String artist, String genre){
        Track newTrack = new Track(name, artist, genre);
        tracks.addLast(newTrack);
        Collections.sort(tracks);
    }

    public OutputDataHolder validateRemoveTrack(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (!arguments[0].equals("all") && (Parser.parseID(arguments[0]) >= tracks.size() || Parser.parseID(arguments[0]) < 0)){
            outputDataHolder.setIndexError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void removeTrack(int index){
        tracks.remove(index);
    }

    @Override
    public void removeAllTracks(){
        tracks.clear();
    }

    @Override
    public void editGenreName(String oldName, String newName){
        for(Track track : tracks) {
            if(track.getGenre().equalsIgnoreCase(oldName))
                track.setGenre(newName);
        }
        Collections.sort(tracks);
    }

    public OutputDataHolder validateEditByArtistOrNameTrack(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if(Integer.parseInt(command.getArguments()[0]) >= tracks.size() || Integer.parseInt(command.getArguments()[0]) < 0){
            outputDataHolder.setIndexError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void editName(int index, String newName){
        tracks.get(index).setName(newName);
        Collections.sort(tracks);
    }
    @Override
    public void editArtist(int index, String newArtist){
        tracks.get(index).setArtist(newArtist);
        Collections.sort(tracks);
    }

    public OutputDataHolder validateEditByGenreTrack(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if(arguments[1] == null){
            outputDataHolder.setTrackWithoutGenreWarning(true);
        }
        if(Parser.parseID(arguments[0]) >= tracks.size() || Parser.parseID(arguments[0]) < 0){
            outputDataHolder.setIndexError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void editGenre(int index, String newGenre){
        tracks.get(index).setGenre(newGenre);
    }

    @Override
    public Track[] find(String name, String artist, String genre) {
        LinkedList<Track> result = new LinkedList<>();
        for(Track track : tracks) {
            if(track.searchCompare(name, artist, genre))
                result.add(track);
        }
        return result.toArray(new Track[0]);
    }

    public void setGenreToNull(String genre){
        for (Track track : tracks) {
            if (track.getGenre().equalsIgnoreCase(genre)) track.setGenre(null);
        }
    }

    public void setAllGenreToNull(){
        for (Track track : tracks) {
             track.setGenre(null);
        }
    }

    @Override
    public Track getTrack(int id) {
        if (id >= tracks.size() || id < 0) return null;
        return tracks.get(id);
    }

    @Override
    public Track[] getAllTracks() { return tracks.toArray(new Track[0]); }

    private boolean alreadyExist(Track checked) {
        for(Track track : tracks) {
            if(track.compareTo(checked) == 0)
                return true;
        }
        return false;
    }

    @Override
    public void addReadTracks(Track[] tracks) {
        for(Track track : tracks) {
            if(!alreadyExist(track))
                this.tracks.add(track);
        }
        Collections.sort(this.tracks);
    }
}
