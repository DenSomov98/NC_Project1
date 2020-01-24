package server.model;

import worklib.transfer.*;
import worklib.entities.*;

public interface Genres {

    Response validateAddGenre(Request command);

    void addGenre(String name);

    Response validateRemoveGenre(Request command);

    void removeGenre(String index);

    void removeAllGenres();

    Response validateEditGenre(Request command);

    void editName(String genre, String newName);

    Genre getGenre(String genre);

    Genre getGenreByName(String name);

    Genre getGenreByID(String id);

    Genre[] getAllGenres();

    void addReadGenres(Genre[] genres, boolean duplicate);

}
