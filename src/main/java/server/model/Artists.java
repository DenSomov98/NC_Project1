package server.model;

import worklib.entities.Artist;
import worklib.entities.Genre;
import worklib.entities.Track;
import worklib.transfer.Request;
import worklib.transfer.Response;

public interface Artists {

    void validateAddArtist(Response command);

    void addArtist(String name);

    void validateRemoveArtist(Response command);

    void removeArtist(String index);

    void removeAllArtist();

    void validateEditArtist(Response command);

    void validateLockArtist(Response command);

    void validateUnlockArtist(Response command);

    void editName(String artist, String newName);

    Artist getArtist(String artist);

    Artist getArtistByName(String name);

    Artist getArtistByID(String id);

    Artist[] getAllArtists();

    void addReadArtist(Artist[] artists, Track[] tracks, boolean duplicate);

    void lockArtist(String artistID, int lockID);

    void unlockArtist(String artistID);

    void unlockAll(int clientID);
}
