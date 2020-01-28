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
        if (!arguments[0].equals("all") && (getTrackByID(Parser.parseID(arguments[0])) == null)){
            Response.setIndexError(true);
        }
        return Response;
    }

    @Override
    public void removeTrack(int index){
        tracks.remove(getTrackByID(index));
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

    public Response validateEditTrack(Request command, boolean isGenreCorrect){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if(!isGenreCorrect)
            arguments[3] = "";
        Track test = new Track(arguments[1], arguments[2], arguments[3]);
        if (alreadyExist(test)) Response.setEqualsNameError(true);
        else if(arguments[3].equals("")) Response.setTrackWithoutGenreWarning(true);
        return Response;
    }

    /*public Response validateEditByArtistOrNameTrack(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        if(Parser.parseID(arguments[0]) >= tracks.size() || Parser.parseID(arguments[0]) < 0){
            Response.setIndexError(true);
        }
        return Response;
    }*/

    public Response validateLockTrack(Request command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        Response Response = new Response(keys, arguments);
        Track trackToLock = getTrackByID(Parser.parseID(arguments[0]));
        if (trackToLock == null) Response.setObjectNotFoundError(true);
        return Response;
    }

    @Override
    public void lockTrack(Response command) {
        String[] arguments = command.getArguments();
        Track trackToLock = getTrackByID(Parser.parseID(arguments[0]));
        trackToLock.setLocked(true);
    }

    @Override
    public void unLockTrack(Response command) {
        String[] arguments = command.getArguments();
        Track trackToLock = getTrackByID(Parser.parseID(arguments[0]));
        trackToLock.setLocked(false);
    }

    @Override
    public void editName(int index, String newName){
        Track track = getTrackByID(index);
        track.setName(newName);
        //tracks.get(index).setName(newName);
        //Collections.sort(tracks);
    }
    @Override
    public void editArtist(int index, String newArtist){
        Track track = getTrackByID(index);
        track.setArtist(newArtist);
        //tracks.get(index).setArtist(newArtist);
        //Collections.sort(tracks);
    }

    /*public Response validateEditByGenreTrack(Request command){
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
    }*/

    @Override
    public void editGenre(int index, String newGenre){
        Track track = getTrackByID(index);
        track.setGenre(newGenre);
        //tracks.get(index).setGenre(newGenre);
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

    public static Track[] find(Track[] tracks, String[] searchCriteria) {
        String name = searchCriteria[0];
        String artist = searchCriteria[1];
        String genre = searchCriteria[2];
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

    public static boolean isMatches(String[] data, String[] searchCriteria) {
        String name = searchCriteria[0];
        String artist = searchCriteria[1];
        String genre = searchCriteria[2];
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

        Matcher matcherName = patternName.matcher(data[0]);
        Matcher matcherArtist = patternArtist.matcher(data[1]);
        Matcher matcherGenre = patternGenre.matcher(data[2]);
        return  (matcherName.find() && matcherArtist.find() && matcherGenre.find());
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
    public Track getTrackByID(int id) {
        for(Track track : tracks) {
            if(track.getId() == id)
                return track;
        }
        return null;
    }

    public Track getTrackByName(String name) {
        for(Track track : tracks) {
            if(track.getName().equalsIgnoreCase(name))
                return track;
        }
        return null;
    }

    @Override
    public Track[] getAllTracks() {
        Track[] result = new Track[tracks.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tracks.get(i).clone();
        }
        return result;
    }

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
