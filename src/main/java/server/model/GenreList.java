package server.model;

import worklib.transfer.*;
import worklib.parse.*;
import worklib.entities.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class GenreList implements Genres, Serializable {

    private LinkedList<Genre> genres = new LinkedList<>();

    public Response validateAddGenre(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if (getGenreByName(arguments[0]) != null) {
            Response.setEqualsNameError(true);
        }
        return Response;
    }

    @Override
    public void addGenre(String name){
        genres.addLast(new Genre(name));
        //Collections.sort(genres);
    }

    public Response validateRemoveGenre(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if (!arguments[0].equals("all") && getGenre(arguments[0]) == null){
            Response.setIndexError(true);
        }
        return Response;
    }

    @Override
    public void removeGenre(String index){
        genres.remove(getGenre(index));
    }

    @Override
    public void removeAllGenres(){
        genres.clear();
    }

    public Response validateEditGenre(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        Genre genre = getGenre(arguments[0]);
        if (genre == null) Response.setObjectNotFoundError(true);
        if (getGenreByName(arguments[1]) != null) Response.setEqualsNameError(true);
        return Response;
    }

    public Response validateLockGenre(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        Genre genreToLock = getGenreByID(arguments[0]);
        if (genreToLock == null) Response.setObjectNotFoundError(true);
        return Response;
    }

    @Override
    public void editName(String genre, String newName){
        getGenre(genre).setName(newName);
        //Collections.sort(genres);
    }

    public Genre getGenre(String genreID){
        Genre result = getGenreByName(genreID);
        return result == null ? getGenreByID(genreID) : result;
    }

    public Genre getGenreByName(String name) {
        for(Genre genre : genres) {
            if(genre.getName().equalsIgnoreCase(name))
                return genre;
        }
        return null;
    }

    public Genre getGenreByID(String genreID) {
        int id = Parser.parseID(genreID);
        for(Genre genre : genres) {
            if(genre.getId() == id)
                return genre;
        }
        return null;
    }

    @Override
    public Genre[] getAllGenres() {
        return genres.toArray(new Genre[0]);
    }

    @Override
    public void addReadGenres(Genre[] genres, Track[] tracks, boolean duplicate) {
        for(Genre g : genres) {
            Genre genre = getGenreByName(g.getName());
            if(genre == null) {
                g.setNewId();
                this.genres.add(g);
            }
            else if(duplicate){
                int dupInd = 1;
                for (int i = 0; i < this.genres.size(); i++) {
                    if(getGenreByName(genre.getName() + " (" + dupInd + ")") != null)
                        dupInd++;
                    else
                        break;
                }
                g.setNewId();
                String genreDup = g.getName() + " (" + dupInd + ")";
                for (Track track : tracks) {
                    if(track.getGenre().equals(g.getName()))
                        track.setGenre(genreDup);
                }
                g.setName(genreDup);
                this.genres.add(g);
            }
        }
        Collections.sort(this.genres);
    }
}