package Model;

public interface Genres {

    public void addGenre(String name);

    public void addGenre(Genre genre);

    public void removeGenre(int index);

    public void editName(int index, String newName);

    public Genre getGenre(int id);

    public Genre[] getAllGenres();

    //public Genre getGenre(String name);  //Перейдет в функционал поиска
}
