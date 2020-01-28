package server.model;

import worklib.transfer.*;
import worklib.entities.*;

public interface Genres {

    void validateAddGenre(Response command);

    void addGenre(String name);

    void validateRemoveGenre(Response command);

    void removeGenre(String index);

    void removeAllGenres();

    void validateEditGenre(Response command);

    void validateLockGenre(Response command);

    void validateUnlockGenre(Response command);

    void editName(String genre, String newName);

    Genre getGenre(String genre);

    Genre getGenreByName(String name);

    Genre getGenreByID(String id);

    Genre[] getAllGenres();

    void addReadGenres(Genre[] genres, Track[] tracks, boolean duplicate);

    void lockGenre(String genreID, int lockID);

    void unlockGenre(String genreID);

    void unlockAll(int clientID);
}
