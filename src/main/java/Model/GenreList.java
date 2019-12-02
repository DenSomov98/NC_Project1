package Model;

import Controller.InputDataHolder;
import Parse.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class GenreList implements Genres, Serializable {

    private LinkedList<Genre> genres = new LinkedList<>();

    public OutputDataHolder validateAddGenre(InputDataHolder command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        OutputDataHolder outputDataHolder = new OutputDataHolder(keys, arguments);
        if (findGenre(arguments[0]) != null) {
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
    public void removeGenre(String index){
        genres.remove(getGenre(index));
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
        if (genre == null) outputDataHolder.setObjectNotFoundError(true);
        if (findGenre(arguments[1]) != null) outputDataHolder.setGenreEqualsNameError(true);
        return outputDataHolder;
    }

    @Override
    public void editName(String genre, String newName){
        getGenre(genre).setName(newName);
        Collections.sort(genres);
    }

    public Genre getGenre(String genreID){
        for (Genre genre : genres) {
            if (genre.getName().equalsIgnoreCase(genreID))return genre;
        }
        int id = Parser.parseID(genreID);
        if (id >= genres.size()|| id < 0) return null;
        return genres.get(id);
    }

    public Genre findGenre(String name) {
        for(Genre genre : genres) {
            if(genre.getName().equalsIgnoreCase(name))
                return genre;
        }
        return null;
    }

    @Override
    public Genre[] getAllGenres() {
        return genres.toArray(new Genre[0]);
    }

    @Override
    public void addReadGenres(Genre[] genres, boolean duplicate) {
        for(Genre g : genres) {
            Genre genre = findGenre(g.getName());
            if(genre == null)
                this.genres.add(g);
            else if(duplicate){
                int dubInd = 1;
                for (int i = 0; i < this.genres.size(); i++) {
                    if(findGenre(genre.getName() + " (" + dubInd + ")") != null)
                        dubInd++;
                    else
                        break;
                }
                g.setName(g.getName() + " (" + dubInd + ")");
                this.genres.add(g);
            }
        }
        Collections.sort(this.genres);
    }
}