package Model;

import DataHolder.*;
import java.util.Collections;
import java.util.LinkedList;

public class GenreList implements Genres{

    private LinkedList<Genre> genres;

    public OutputDataHolder validateAddGenre(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (getGenre(command.getArguments()[0]) != null) {
            outputDataHolder.setGenreEqualsNameError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void addGenre(String name) throws IllegalArgumentException{
        genres.addLast(getGenre(name));
        Collections.sort(genres);
    }

    @Override
    public void addGenre(Genre genre){
        genres.addLast(genre);
    }

    public OutputDataHolder validateRemoveGenre(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (!command.getArguments()[0].equals("all") || (Integer.parseInt(command.getArguments()[0]) > genres.size() && Integer.parseInt(command.getArguments()[0]) < 0)){
            outputDataHolder.setIndexError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void removeGenre(int index){
        genres.remove(index);
    }

    public OutputDataHolder validateEditGenre(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (getGenre(command.getArguments()[1]) != null) {
            outputDataHolder.setGenreEqualsNameError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void editName(int index, String newName){
        genres.get(index).setName(newName);
        Collections.sort(genres);
    }

    public Genre getGenre(String name){
        for (Genre genre : genres) {
            if (genre.getName().equals(name))return genre;
        }
        return null;
    }

    @Override
    public Genre getGenre(int id) {
        return genres.get(id);
    }

    @Override
    public Genre[] getAllGenres() {
        return genres.toArray(new Genre[0]);
    }
}