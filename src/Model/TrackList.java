package Model;


import DataHolder.*;

import java.util.Collections;
import java.util.LinkedList;


public class TrackList implements Tracks {
    private LinkedList<Track> tracks;
    //private GenreList genreModel; //

    //Перенести в Model
    public OutputDataHolder validate (InputDataHolder holder) {
        int id = Integer.parseInt(holder.getArguments()[0]);
        String[] arguments = holder.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(id, holder.getArguments());
        if(id >= tracks.size() || id < 0)//ошибка индексации
            outputDataHolder.setIndexError(true);

        if(holder.hasArguments()){
            Key[] keys = holder.getKeys();
            switch (keys[0]){
                case ADD:
                    if (holder.getArguments().length < 3 /*|| genreModel.getGenre(holder.getArguments()[2]) == null*/) {
                        outputDataHolder.setTrackWithoutGenreError(true);
                    }
                    break;
                case EDIT:
                    if(keys[2] == Key.GENRE /*&& genreModel.getGenre(holder.getArguments()[1]) == null*/){
                        outputDataHolder.setTrackWithoutGenreError(true);
                    }
                    break;
            }
        }
        return outputDataHolder;
        // все?
    }

    //модель отправляет сюда объект типа Genre вместо строки
    @Override
    public void addTrack(String name, String artist, String genreName){
        Genre genre = null;// = genreModel.getGenre(genreName);
        if(genre != null) {
            Track newTrack = new Track(name, artist, genre);
            int index = Collections.binarySearch(tracks, newTrack);
            tracks.add(index, newTrack);
        }
    }

    @Override
    public void removeTrack(int index){
        tracks.remove(index);
    }

    @Override
    public void editName(int index, String newName){
        Track track = tracks.get(index);
        removeTrack(index);
        track.setName(newName);
        int newIndex = Collections.binarySearch(tracks, track);
        tracks.add(newIndex, track);
    }

    @Override
    public void editArtist(int index, String newArtist){
        tracks.get(index).setArtist(newArtist);
    }

    @Override
    public void editGenre(int index, String newGenreName){
        Genre genre = null; // genreModel.getGenre(newGenreName);
        if(genre != null) {
            tracks.get(index).setGenre(genre);
        }
    }

    public void setGenreToNull(String genreName){
        Genre oldGenre = null;// genreModel.getGenre(genreName);
        if(oldGenre != null) {
            for (Track track : tracks) {
                if (track.getGenre() == oldGenre) track.setGenre(null);
            }
        }
    }

    @Override
    public Track getTrack(int id) { return tracks.get(id); }

    @Override
    public Track[] getAllTracks() { return tracks.toArray(new Track[0]); }
}
