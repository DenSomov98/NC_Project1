package server.model;

import worklib.transfer.*;
import worklib.entities.*;
import worklib.parse.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TrackList implements Tracks, Serializable {
    private LinkedList<Track> tracks = new LinkedList<>();

    public TrackList () {}

    public Response validateAddTrack(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if (arguments.length == 3 && arguments[2] == null) {
            Response.setTrackWithoutGenreWarning(true);
        }
        return Response;
    }

    @Override
    public void addTrack(String name, String artist, String genre){
        Track newTrack = new Track(name, artist, genre);
        tracks.addLast(newTrack);
        //Collections.sort(tracks);
    }

    public Response validateRemoveTrack(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if (!arguments[0].equals("all") && (getTrack(Parser.parseID(arguments[0])) == null)){
            Response.setIndexError(true);
        }
        return Response;
    }

    @Override
    public void removeTrack(int index){
        tracks.remove(getTrack(index));
    }

    @Override
    public void removeAllTracks(){
        tracks.clear();
    }

    @Override
    public void editGenreName(String oldName, String newName){
        for(Track track : tracks) {
            if(track.getGenre().equalsIgnoreCase(oldName))
                track.setGenre(newName);
        }
        //Collections.sort(tracks);
    }

    public Response validateEditByArtistOrNameTrack(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if(Parser.parseID(arguments[0]) >= tracks.size() || Parser.parseID(arguments[0]) < 0){
            Response.setIndexError(true);
        }
        return Response;
    }

    @Override
    public void editName(int index, String newName){
        tracks.get(index).setName(newName);
        //Collections.sort(tracks);
    }
    @Override
    public void editArtist(int index, String newArtist){
        tracks.get(index).setArtist(newArtist);
        //Collections.sort(tracks);
    }

    public Response validateEditByGenreTrack(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if(arguments[1] == null){
            Response.setTrackWithoutGenreWarning(true);
        }
        if(Parser.parseID(arguments[0]) >= tracks.size() || Parser.parseID(arguments[0]) < 0){
            Response.setIndexError(true);
        }
        return Response;
    }

    @Override
    public void editGenre(int index, String newGenre){
        tracks.get(index).setGenre(newGenre);
    }

    @Override
    public Track[] find(String name, String artist, String genre) {
        LinkedList<Track> result = new LinkedList<>();
        StringBuilder regexName = new StringBuilder(name);
        StringBuilder regexArtist = new StringBuilder(artist);
        StringBuilder regexGenre = new StringBuilder(genre);
        Parser.toRegEx(regexName, "*");
        Parser.toRegEx(regexName, "?");
        Parser.toRegEx(regexArtist, "*");
        Parser.toRegEx(regexArtist, "?");
        Parser.toRegEx(regexGenre, "*");
        Parser.toRegEx(regexGenre, "?");

        Pattern patternName = Pattern.compile(regexName.toString(), Pattern.CASE_INSENSITIVE);
        Pattern patternArtist = Pattern.compile(regexArtist.toString(), Pattern.CASE_INSENSITIVE);
        Pattern patternGenre = Pattern.compile(regexGenre.toString(),Pattern.CASE_INSENSITIVE);

        for (Track track : tracks) {
            Matcher matcherName = patternName.matcher(track.getName());
            Matcher matcherArtist = patternArtist.matcher(track.getArtist());
            Matcher matcherGenre = patternGenre.matcher(track.getGenre());
            if (matcherName.find() && matcherArtist.find() && matcherGenre.find()){
                result.add(track);
            }
        }
        return result.toArray(new Track[0]);
    }

    public void setGenreToNull(String genre){
        for (Track track : tracks) {
            if (track.getGenre().equalsIgnoreCase(genre)) track.setGenre("");
        }
    }

    public void setAllGenreToNull(){
        for (Track track : tracks) {
             track.setGenre(null);
        }
    }

    @Override
    public Track getTrack(int id) {
        for(Track track : tracks) {
            if(track.getId() == id)
                return track;
        }
        return null;
    }

    @Override
    public Track[] getAllTracks() { return tracks.toArray(new Track[0]); }

    private boolean alreadyExist(Track checked) {
        for(Track track : tracks) {
            if(track.equals(checked))
                return true;
        }
        return false;
    }

    @Override
    public void addReadTracks(Track[] tracks, boolean duplicate) {
        for(Track track : tracks) {
            if(!alreadyExist(track)) {
                track.setNewId();
                this.tracks.add(track);
            }
            else if(duplicate){
                String oldName = track.getName();
                for (int i = 1; i <= this.tracks.size(); i++) {
                    track.setName(oldName + " (" + i + ")");
                    if(!alreadyExist(track))
                        break;
                }
                track.setNewId();
                this.tracks.add(track);
            }
        }
        Collections.sort(this.tracks);
    }

}
