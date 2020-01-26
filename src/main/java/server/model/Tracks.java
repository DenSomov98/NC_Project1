package server.model;

import worklib.transfer.*;
import worklib.entities.*;

public interface Tracks {

    Response validateAddTrack(Request command);

    void addTrack(String name, String artist, String genre);

    Response validateRemoveTrack(Request command);

    void removeTrack(int index);

    void removeAllTracks();

    //Response validateEditByArtistOrNameTrack(Request command);

    Response validateEditTrack(Request command);

    Response validateLockTrack(Request command);

    void editName(int index, String newName);

    void editArtist(int index, String newArtist);

    //Response validateEditByGenreTrack(Request command);

    void editGenre(int index, String newGenre);

    Track[] find(String name, String artist, String genre);

    void setGenreToNull(String genre);

    void setAllGenreToNull();

    Track getTrackByID(int id);

    Track getTrackByName(String name);

    Track[] getAllTracks();

    void editGenreName(String oldName, String newName);

    void addReadTracks(Track[] tracks, boolean duplicate);
}
