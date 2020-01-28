package server.model;

import worklib.transfer.*;
import worklib.entities.*;

public interface Tracks {

    void validateAddTrack(Response command);

    void addTrack(String name, String artist, String genre);

    void validateRemoveTrack(Response command);

    void removeTrack(int index);

    void removeAllTracks();

    //void validateEditByArtistOrNameTrack(Response command);

    void validateEditTrack(Response command, boolean isArtistCorrect, boolean isGenreCorrect);

    void validateLockTrack(Response command);

    void validateUnlockTrack(Response command);

    void editName(int index, String newName);

    void editArtist(int index, String newArtist);

    //void validateEditByGenreTrack(Response command);

    void editGenre(int index, String newGenre);

    Track[] find(String name, String artist, String genre);

    void setGenreToNull(String genre);

    void setAllGenreToNull();

    Track getTrackByID(int id);

    Track[] getAllTracks();

    void editGenreName(String oldName, String newName);

    void editArtistName(String oldName, String newName);


    void addReadTracks(Track[] tracks, boolean duplicate);

    void lockTrack(String trackId, int lockId);

    void unlockTrack(String trackId);

    void unlockAll(int clientID);
}
