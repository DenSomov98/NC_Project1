package server.model;

import worklib.entities.Artist;
import worklib.entities.Genre;
import worklib.entities.Track;
import worklib.parse.Key;
import worklib.parse.Parser;
import worklib.transfer.Request;
import worklib.transfer.Response;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;

public class ArtistList implements Artists, Serializable {

    private LinkedList<Artist> artists = new LinkedList<>();

    public void validateAddArtist(Response command){
        if (getArtistByName(command.getArguments()[0]) != null)
            command.setEqualsNameError(true);
    }

    @Override
    public void addArtist(String name){
        artists.addLast(new Artist(name));
    }

    public void validateRemoveArtist(Response command){
        String[] arguments = command.getArguments();
        if (!arguments[0].equals("all") && getArtist(arguments[0]) == null)
            command.setIndexError(true);
    }

    @Override
    public void removeArtist(String index){
        artists.remove(getArtist(index));
    }

    @Override
    public void removeAllArtist(){
        artists.clear();
    }

    public void validateEditArtist(Response command){
        String[] arguments = command.getArguments();
        Artist artist = getArtist(arguments[0]);
        if (artist == null) command.setObjectNotFoundError(true);
        else if (artist.getLockID() != command.getClientID()) command.setAccessError(true);
        else if (getArtistByName(arguments[1]) != null) command.setEqualsNameError(true);
    }

    public void validateLockArtist(Response command){
        String[] arguments = command.getArguments();
        Artist artistToLock = getArtistByID(arguments[0]);
        if (artistToLock == null) command.setObjectNotFoundError(true);
        else if (artistToLock.getLockID() > -1) command.setAlreadyLockedError(true);
    }

    public void validateUnlockArtist(Response command){
        String[] arguments = command.getArguments();
        Artist artistToLock = getArtistByID(arguments[0]);
        if (artistToLock == null) command.setObjectNotFoundError(true);
        else if (artistToLock.getLockID() != command.getClientID()) command.setAccessError(true);
    }

    @Override
    public void editName(String artist, String newName){
        getArtist(artist).setName(newName);
    }

    public Artist getArtist(String artistID){
        Artist result = getArtistByName(artistID);
        return result == null ? getArtistByID(artistID) : result;
    }

    public Artist getArtistByName(String name) {
        for(Artist artist : artists) {
            if(artist.getName().equalsIgnoreCase(name))
                return artist;
        }
        return null;
    }

    public Artist getArtistByID(String artistID) {
        int id = Parser.parseID(artistID);
        for(Artist artist : artists) {
            if(artist.getId() == id)
                return artist;
        }
        return null;
    }

    @Override
    public Artist[] getAllArtists() {
        return artists.toArray(new Artist[0]);
    }

    @Override
    public void addReadArtist(Artist[] artists, Track[] tracks, boolean duplicate) {
        for(Artist a : artists) {
            Artist artist = getArtistByName(a.getName());
            if(artist == null) {
                a.setNewId();
                this.artists.add(a);
            }
            else if(duplicate){
                int dupInd = 1;
                for (int i = 0; i < this.artists.size(); i++) {
                    if(getArtistByName(artist.getName() + " (" + dupInd + ")") != null)
                        dupInd++;
                    else
                        break;
                }
                a.setNewId();
                String artistDup = a.getName() + " (" + dupInd + ")";
                for (Track track : tracks) {
                    if(track.getArtist().equals(a.getName()))
                        track.setArtist(artistDup);
                }
                a.setName(artistDup);
                this.artists.add(a);
            }
        }
        Collections.sort(this.artists);
    }

    @Override
    public void unlockArtist(String artistID) {
        getArtist(artistID).unLock();
    }

    @Override
    public void unlockAll(int clientID) {
        for(Artist artist : artists) {
            if(artist.getLockID() == clientID)
                artist.unLock();
        }
    }

    @Override
    public void lockArtist(String artistID, int clientID) {
        getArtist(artistID).setLockID(clientID);
    }
}