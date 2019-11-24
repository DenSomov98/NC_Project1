package Model;


import DataHolder.*;

import java.util.Collections;
import java.util.LinkedList;


public class TrackList implements Tracks {
    private LinkedList<Track> tracks = new LinkedList<Track>();


    public OutputDataHolder validateAddTrack(InputDataHolder command, Genre genre){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (command.getArguments().length < 3 || genre == null) {
            outputDataHolder.setTrackWithoutGenreWarning(true);
        }
        return outputDataHolder;
    }

    @Override
    public void addTrack(String name, String artist, Genre genre){
        Track newTrack = new Track(name, artist, genre);
        tracks.addLast(newTrack);
        Collections.sort(tracks);
    }

    public OutputDataHolder validateRemoveTrack(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (!command.getArguments()[0].equals("all") && (Integer.parseInt(command.getArguments()[0]) >= tracks.size() || Integer.parseInt(command.getArguments()[0]) < 0)){
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
    public void editName(int index, String newName){
        tracks.get(index).setName(newName);
        Collections.sort(tracks);
    }

    public OutputDataHolder validateEditByArtistOrNameTrack(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if(Integer.parseInt(command.getArguments()[0]) > tracks.size() || Integer.parseInt(command.getArguments()[0]) < 0){
            outputDataHolder.setIndexError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void editArtist(int index, String newArtist){
        tracks.get(index).setArtist(newArtist);
    }

    public OutputDataHolder validateEditByGenreTrack(InputDataHolder command, Genre genre){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if(genre == null){
            outputDataHolder.setTrackWithoutGenreWarning(true);
        }
        if(Integer.parseInt(command.getArguments()[0]) > tracks.size() || Integer.parseInt(command.getArguments()[0]) < 0){
            outputDataHolder.setIndexError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void editGenre(int index, Genre newGenre){
        tracks.get(index).setGenre(newGenre);
    }

    public void setGenreToNull(Genre genre){
        if (genre != null){
            for (Track track : tracks) {
                if (track.getGenre() == genre) track.setGenre(null);
            }
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
}