package Model;

import java.util.LinkedList;

public class GenreModel {
    private LinkedList<Genre> genres;
    public void addGenre(String name)throws Exception{
        for (Genre genre:genres) {
            if (genre.getName().equals(name)) throw new Exception("");
        }
        genres.addLast(new Genre(name));
    }
    public void addGenre(Genre genre){
        genres.addLast(genre);
    }
    public void removeGenre(int index){
        genres.remove(index);
    }
    public void editName(int index, String name)throws Exception{
        for (Genre genre:genres) {
            if (genre.getName().equals(name)) throw new Exception("");
        }
        genres.set(index, new Genre(name));
    }
    public String getViewList(){
        StringBuilder s=new StringBuilder();
        int index=1;
        for (Genre genre:genres) {
            s.append(genre.getName()).append(" ").append(index);
            index++;
        }
        return s.toString();
    }
    public Genre getGenre(String name)throws Exception{
        for (Genre genre:genres) {
            if (genre.getName().equals(name))return genre;
        }
        throw new Exception("");
    }
}
