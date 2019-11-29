package Model;

import Controller.InputDataHolder;

public interface Tracks {

    OutputDataHolder validateAddTrack(InputDataHolder command);

    void addTrack(String name, String artist, String genre);

    OutputDataHolder validateRemoveTrack(InputDataHolder command);

    void removeTrack(int index);

    void removeAllTracks();

    OutputDataHolder validateEditByArtistOrNameTrack(InputDataHolder command);

    void editName(int index, String newName);

    void editArtist(int index, String newArtist);

    OutputDataHolder validateEditByGenreTrack(InputDataHolder command);

    void editGenre(int index, String newGenre);

    Track[] find(String name, String artist, String genre);

    void setGenreToNull(String genre);

    void setAllGenreToNull();

    Track getTrack(int id);

    Track[] getAllTracks();

    void editGenreName(String oldName, String newName);

    void addReadTracks(Track[] tracks, boolean duplicate);
}
