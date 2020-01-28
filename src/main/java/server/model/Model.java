package server.model;

import worklib.transfer.*;
import worklib.parse.*;
import worklib.entities.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.*;

public class Model {

    private Tracks tracks;
    private Genres genres;

    public Model(TrackList tracks, Genres genres) {
        this.tracks = tracks;
        this.genres = genres;
    }

    public synchronized Response validate(Request command) {
        Key[] keys = command.getKeys();
        switch (keys[0]) {
            case ADD:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateAddGenre(command);
                    case TRACK:
                        return tracks.validateAddTrack(command);
                    default:
                        Response Response = new Response(command.getKeys(), command.getArguments());
                        Response.setUnknownError(true);
                        return Response;
                }
            case LOCK:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateLockGenre(command);
                    case TRACK:
                        return tracks.validateLockTrack(command);
                    default:
                        Response Response = new Response(command.getKeys(), command.getArguments());
                        Response.setUnknownError(true);
                        return Response;
                }
            case EDIT:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateEditGenre(command);
                    case TRACK:;
                        boolean isGenreCorrect = true;
                        Genre genre = genres.getGenre(command.getArguments()[3]);
                        if(genre == null)
                            isGenreCorrect = false;
                        return tracks.validateEditTrack(command, isGenreCorrect);
                    default:
                        Response Response = new Response(command.getKeys(), command.getArguments());
                        Response.setUnknownError(true);
                        return Response;
                }
            case REMOVE:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateRemoveGenre(command);
                    case TRACK:
                        return tracks.validateRemoveTrack(command);
                    default:
                        Response Response = new Response(command.getKeys(), command.getArguments());
                        Response.setUnknownError(true);
                        return Response;
                }
            case SAVE:
                Response result = new Response(command.getKeys(), command.getArguments());
                String filePath = command.getArguments()[0];
                if(command.getKeys().length == 1) {
                    File file = new File(filePath);
                    if (file.exists() && file.isFile()) {
                        result.setFileExistsError(true);
                        return result;
                    }
                }
                try {
                    new FileOutputStream(command.getArguments()[0]).close();
                } catch (IOException e) {
                    result.setFileError(true);
                }
                return result;
            case LOAD:
                result = new Response(command.getKeys(), command.getArguments());
                try {
                    new FileInputStream(command.getArguments()[0]).close();
                } catch (IOException e) {
                    result.setFileError(true);
                }
                return result;
            case GET:
                if (command.getArguments()[0].equals("all"))
                    return new Response(command.getKeys(), command.getArguments());
            case FIND:
                return new Response(command.getKeys(), command.getArguments());
            default:
                Response Response = new Response(command.getKeys(), command.getArguments());
                Response.setUnknownError(true);
                return Response;

        }
    }

    public Track[] findTracks(Response command) {
        String[] args = command.getArguments();
        return tracks.find(args[0], args[1], args[2]);
    }

    private synchronized void executeAdd(Response command) {
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                genres.addGenre(arguments [0]);
                break;
            case TRACK:
                String genreName = "";
                if(arguments.length == 3) {
                    Genre genre = genres.getGenre(arguments[2]);
                    if(genre != null)
                        genreName = genre.getName();
                }
                if(genreName.equals(""))
                    command.setTrackWithoutGenreWarning(true);
                tracks.addTrack(arguments[0], arguments[1], genreName);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private synchronized void executeEdit(Response command) {
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                tracks.editGenreName(genres.getGenre(arguments[0]).getName(), arguments[1]);
                genres.editName(arguments[0], arguments[1]);
                genres.unLockGenre(command);
                break;
            case TRACK:
                int id = Parser.parseID(arguments[0]);
                tracks.editName(id, arguments[1]);
                tracks.editArtist(id, arguments[2]);
                Genre genre = genres.getGenre(arguments[3]);
                String newGenre = genre == null ? "" : genre.getName();
                if(newGenre.equals(""))
                    command.setTrackWithoutGenreWarning(true);
                tracks.editGenre(id, newGenre);
                tracks.unLockTrack(command);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }


    private synchronized void executeRemove(Response command) {
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                if (arguments[0].equals("all")) {
                    tracks.setAllGenreToNull();
                    genres.removeAllGenres();
                }
                else {
                    Genre genre = genres.getGenre(arguments[0]);
                    if(genre != null)
                        tracks.setGenreToNull(genre.getName());
                    genres.removeGenre(arguments[0]);
                }
                break;
            case TRACK:
                if (arguments[0].equals("all"))
                    tracks.removeAllTracks();
                else
                    tracks.removeTrack(Parser.parseID(arguments[0]));
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private synchronized void executeSaveIntoFile(Response command) {
        String filePath = command.getArguments()[0];
        try {
            JAXBContext context = JAXBContext.newInstance(Wrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(new Wrapper(tracks.getAllTracks(), genres.getAllGenres()), new File(filePath));
        } catch (JAXBException e) {
            command.setFileIsCorruptedError(true);
        }
    }

    private synchronized void executeLoadFromFile(Response command) {
        Wrapper res = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Wrapper.class);
            Unmarshaller un = jaxbContext.createUnmarshaller();
            res = (Wrapper) un.unmarshal(new File(command.getArguments()[0]));
        } catch (JAXBException e) {
            command.setFileIsCorruptedError(true);
            e.printStackTrace();
        }
        Track[] tracks = null;
        Genre[] genres = null;
        try {
            tracks = res.getT();
            genres = res.getG();
            for (Track track : tracks) {
                if(track.getName() == null || track.getArtist() == null || track.getGenre() == null) {
                    command.setFileIsCorruptedError(true);
                    return;
                }
            }
            for (Genre genre : genres) {
                if(genre.getName() == null) {
                    command.setFileIsCorruptedError(true);
                    return;
                }
            }
            if(tracks.length == 0 && genres.length == 0)
                command.setFileIsEmptyWarning(true);
        } catch (Exception e) {
            command.setFileIsCorruptedError(true);
            e.printStackTrace();
            return;
        }
        if(command.getKeys()[1] == Key.DUPLICATE) {
            this.genres.addReadGenres(genres, tracks, true);
            this.tracks.addReadTracks(tracks, true);
        }
        else {
            this.genres.addReadGenres(genres, tracks, false);
            this.tracks.addReadTracks(tracks, false);
        }
    }

    public synchronized void executeLock(Response command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                genres.lockGenre(command);
                break;
            case TRACK:
                tracks.lockTrack(command);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public synchronized void executeFind(Response command){
        findTracks(command);
    }

    public void execute(Response command) {
        if(command.hasErrors())
            throw new IllegalArgumentException();
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[0]) {
            case ADD:
                executeAdd(command);
                break;
            case EDIT:
                executeEdit(command);
                break;
            case REMOVE:
                executeRemove(command);
                break;
            case SAVE:
                executeSaveIntoFile(command);
                break;
            case LOAD:
                executeLoadFromFile(command);
                break;
            case LOCK:
                executeLock(command);
                break;
            case FIND:
                executeFind(command);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Wrapper getAllData() {
        Wrapper data = new Wrapper();
        synchronized (this) {
            data.setGenres(genres.getAllGenres());
            data.setTracks(tracks.getAllTracks());
        }
        return data;
    }
}