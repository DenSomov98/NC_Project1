package server.model;

import worklib.transfer.*;
import worklib.parse.*;
import worklib.entities.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class GenreList implements Genres, Serializable {

    private LinkedList<Genre> genres = new LinkedList<>();

    public void validateAddGenre(Response command){
        if (getGenreByName(command.getArguments()[0]) != null)
            command.setEqualsNameError(true);
    }

    @Override
    public void addGenre(String name){
        genres.addLast(new Genre(name));
        //Collections.sort(genres);
    }

    public void validateRemoveGenre(Response command){
        String[] arguments = command.getArguments();
        if (!arguments[0].equals("all") && getGenre(arguments[0]) == null)
            command.setIndexError(true);
    }

    @Override
    public void removeGenre(String index){
        genres.remove(getGenre(index));
    }

    @Override
    public void removeAllGenres(){
        genres.clear();
    }

    public void validateEditGenre(Response command){
        String[] arguments = command.getArguments();
        Genre genre = getGenre(arguments[0]);
        if (genre == null) command.setObjectNotFoundError(true);
        else if (genre.getLockID() != command.getClientID()) command.setAccessError(true);
        else if (getGenreByName(arguments[1]) != null) command.setEqualsNameError(true);
    }

    public void validateLockGenre(Response command){
        String[] arguments = command.getArguments();
        Genre genreToLock = getGenreByID(arguments[0]);
        if (genreToLock == null) command.setObjectNotFoundError(true);
        else if (genreToLock.getLockID() > -1) command.setAlreadyLockedError(true);
    }

    public void validateUnlockGenre(Response command){
        String[] arguments = command.getArguments();
        Genre genreToLock = getGenreByID(arguments[0]);
        if (genreToLock == null) command.setObjectNotFoundError(true);
        else if (genreToLock.getLockID() != command.getClientID()) command.setAccessError(true);
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
        Genre[] result = new Genre[genres.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = genres.get(i).clone();
        }
        return result;
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

    @Override
    public void unlockGenre(String genreID) {
        getGenre(genreID).unLock();
    }

    @Override
    public void unlockAll(int clientID) {
        for(Genre genre : genres) {
            if(genre.getLockID() == clientID)
                genre.unLock();
        }
    }

    @Override
    public void lockGenre(String genreID, int clientID) {
        getGenre(genreID).setLockID(clientID);
    }
}