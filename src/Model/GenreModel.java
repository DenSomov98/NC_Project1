package Model;

import DataHolder.Key;
import DataHolder.InputDataHolder;
import DataHolder.OutputDataHolder;
import java.util.Collections;
import java.util.LinkedList;

public class GenreModel {

    private LinkedList<Genre> genres;

    public OutputDataHolder validate(InputDataHolder holder){
        OutputDataHolder outputDataHolder = new OutputDataHolder(holder.getID(), holder.getArguments());
        if(holder.hasID() && (holder.getID() >= genres.size() || holder.getID() < 0))//ошибка индексации
            outputDataHolder.setIndexError(true);
        if(holder.hasArguments()){
            Key[] keys = holder.getKeys();
            switch (keys[0]){
                case ADD:
                    if (getGenre(holder.getArguments()[0]) != null) {
                        outputDataHolder.setGenreEqualsNameError(true);
                    }
                    break;
                case EDIT:
                    if(getGenre(holder.getArguments()[1]) != null){
                        outputDataHolder.setGenreEqualsNameError(true);
                    }
                    break;
            }
        }
        return outputDataHolder;
    }

    public void addGenre(String name) throws IllegalArgumentException{
        for (Genre genre : genres) {
            if (genre.getName().equals(name)) throw new IllegalArgumentException("Жанр с таким именем уже существует");
        }
        Genre newGenre = new Genre(name);
        int index = Collections.binarySearch(genres, newGenre);
        genres.add(index, newGenre);
    }

    public void addGenre(Genre genre){
        genres.addLast(genre);
    }

    public void removeGenre(int index){
        genres.remove(index);
    }

    public void editName(int index, String newName)throws IllegalArgumentException{
        for (Genre genre : genres) {
            if (genre.getName().equals(newName)) throw new IllegalArgumentException("Жанр с таким именем уже существует");
        }
        Genre newGenre = genres.get(index);
        removeGenre(index);
        newGenre.setName(newName);
        int newIndex = Collections.binarySearch(genres, newGenre);
        genres.add(newIndex, newGenre);
        //сортирвка
    }

    public LinkedList<Genre> getViewList(){
        return genres;
    }

    public Genre getGenre(String name){
        for (Genre genre : genres) {
            if (genre.getName().equals(name))return genre;
        }
        return null;
    }
}
