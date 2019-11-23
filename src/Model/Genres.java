package Model;

import DataHolder.InputDataHolder;

public interface Genres {

    OutputDataHolder validateAddGenre(InputDataHolder command);

    void addGenre(String name);

    OutputDataHolder validateRemoveGenre(InputDataHolder command);

    void removeGenre(int index);

    void removeAllGenres();

    OutputDataHolder validateEditGenre(InputDataHolder command);

    void editName(int index, String newName);

    Genre getGenre(int id);

    Genre getGenre(String name);

    Genre[] getAllGenres();

}
