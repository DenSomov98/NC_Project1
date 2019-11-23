package Model;

import DataHolder.InputDataHolder;

public interface Genres {

    public OutputDataHolder validateAddGenre(InputDataHolder command);

    public void addGenre(String name);

    public OutputDataHolder validateRemoveGenre(InputDataHolder command);

    public void removeGenre(int index);

    public OutputDataHolder validateEditGenre(InputDataHolder command);

    public void editName(int index, String newName);

    public Genre getGenre(int id);

    public Genre getGenre(String name);

    public Genre[] getAllGenres();

    //public Genre getGenre(String name);  //Перейдет в функционал поиска
}
