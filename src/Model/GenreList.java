package Model;

import DataHolder.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class GenreList implements Genres, Serializable {

    private LinkedList<Genre> genres = new LinkedList<Genre>();

    public GenreList () {}

    public OutputDataHolder validateAddGenre(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (getGenre(arguments[0]) != null) {
            outputDataHolder.setGenreEqualsNameError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void addGenre(String name){
        genres.addLast(new Genre(name));
        Collections.sort(genres);
    }



    public OutputDataHolder validateRemoveGenre(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (!arguments[0].equals("all") && getGenre(arguments[0]) == null){
            outputDataHolder.setIndexError(true);
        }
        return outputDataHolder;
    }

    @Override
    public void removeGenre(int index){
        genres.remove(index);
    }

    @Override
    public void removeAllGenres(){
        genres.clear();
    }

    public OutputDataHolder validateEditGenre(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        Genre genre = getGenre(arguments[0]);
        if (genre == null) outputDataHolder.setObjectNotFoundedError(true);
        if (findGenre(arguments[1]) != null) outputDataHolder.setGenreEqualsNameError(true);
        return outputDataHolder;
    }

    @Override
    public void editName(String genre, String newName){
        getGenre(genre).setName(newName);
        Collections.sort(genres);
    }

    public Genre getGenre(String genre){
        for (Genre igenre : genres) {
            if (igenre.getName().equals(genre))return igenre;
        }
        int id = Model.parseID(genre);
        if (id >= genres.size()|| id < 0) return null;
        return genres.get(id);
    }

    public Genre findGenre(String name) {
        for(Genre genre : genres) {
            if(genre.getName().equals(name))
                return genre;
        }
        return null;
    }

    @Override
    public Genre[] getAllGenres() {
        return genres.toArray(new Genre[0]);
    }

    @Override
    public void addReadGenres(Genre[] genres) {
        for(Genre genre : genres) {
            if(findGenre(genre.getName()) == null)
                this.genres.add(genre);
        }
        Collections.sort(this.genres);
    }
}