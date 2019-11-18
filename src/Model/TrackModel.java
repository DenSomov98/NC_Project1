package Model;


import DataHolder.Key;
import DataHolder.OutputDataHolder;
import DataHolder.InputDataHolder;

import java.util.Collections;
import java.util.LinkedList;


public class TrackModel {
    private LinkedList<Track> tracks;
    private GenreModel genreModel;

    public OutputDataHolder validate (InputDataHolder holder) {
        OutputDataHolder outputDataHolder = new OutputDataHolder(holder.getID(), holder.getArguments());
        if(holder.hasID() && (holder.getID() >= tracks.size() || holder.getID() < 0))//ошибка индексации
            outputDataHolder.setIndexError(true);

        if(holder.hasArguments()){
            Key[] keys = holder.getKeys();
            switch (keys[0]){
                case ADD:
                    if (holder.getArguments().length < 3 || genreModel.getGenre(holder.getArguments()[2]) == null) {
                        outputDataHolder.setTrackWithoutGenreError(true);
                    }
                    break;
                case EDIT:
                    if(keys[2] == Key.GENRE && genreModel.getGenre(holder.getArguments()[1]) == null){
                        outputDataHolder.setTrackWithoutGenreError(true);
                    }
                    break;
            }
        }
        return outputDataHolder;
        // все?
    }

    public void addTrack(String name, String artist, String genreName){
        Genre genre = genreModel.getGenre(genreName);
        if(genre != null) {
            Track newTrack = new Track(name, artist, genre);
            int index = Collections.binarySearch(tracks, newTrack);
            tracks.add(index, newTrack);
        }
    }

    public void removeTrack(int index){
        tracks.remove(index);
    }

    public void editName(int index, String newName){
        Track track = tracks.get(index);
        removeTrack(index);
        track.setName(newName);
        int newIndex = Collections.binarySearch(tracks, track);
        tracks.add(newIndex, track);
    }

    public void editArtist(int index, String newArtist){
        tracks.get(index).setArtist(newArtist);
    }

    public void editGenre(int index, String newGenreName){
        Genre genre = genreModel.getGenre(newGenreName);
        if(genre != null) {
            tracks.get(index).setGenre(genre);
        }
    }

    public void setGenreToNull(String genreName){
        Genre oldGenre = genreModel.getGenre(genreName);
        if(oldGenre != null) {
            for (Track track : tracks) {
                if (track.getGenre() == oldGenre) track.setGenre(null);
            }
        }
    }

    public LinkedList<Track> getViewList(){
        return tracks;
    }
}
