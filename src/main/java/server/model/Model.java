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
    private Artists artists;

    public Model(Tracks tracks, Artists artists, Genres genres) {
        this.tracks = tracks;
        this.genres = genres;
        this.artists = artists;
    }

    public synchronized Response validate(Request request, int id) {
        Key[] keys = request.getKeys();
        String[] arguments = request.getArguments();
        Response response = new Response(id, keys, arguments);
        switch (keys[0]) {
            case ADD:
                switch (keys[1]) {
                    case GENRE:
                        genres.validateAddGenre(response);
                        return response;
                    case ARTIST:
                        artists.validateAddArtist(response);
                    case TRACK:
                        tracks.validateAddTrack(response);
                        return response;
                    default:
                        response.setUnknownError(true);
                        return response;
                }
            case LOCK:
                switch (keys[1]) {
                    case GENRE:
                        genres.validateLockGenre(response);
                        return response;
                    case ARTIST:
                        artists.validateLockArtist(response);
                        return response;
                    case TRACK:
                        tracks.validateLockTrack(response);
                        return response;
                    default:
                        response.setUnknownError(true);
                        return response;
                }
            case UNLOCK:
                switch (keys[1]) {
                    case GENRE:
                        genres.validateUnlockGenre(response);
                        return response;
                    case ARTIST:
                        artists.validateUnlockArtist(response);
                        return response;
                    case TRACK:
                        tracks.validateUnlockTrack(response);
                        return response;
                    default:
                        response.setUnknownError(true);
                        return response;
                }
            case EDIT:
                switch (keys[1]) {
                    case GENRE:
                        genres.validateEditGenre(response);
                        return response;
                    case ARTIST:
                        artists.validateEditArtist(response);
                        return response;
                    case TRACK:
                        boolean isGenreCorrect = true;
                        boolean isArtistCorrect = true;
                        Artist artist = artists.getArtist(response.getArguments()[2]);
                        Genre genre = genres.getGenre(response.getArguments()[3]);
                        if(genre == null)
                            isGenreCorrect = false;
                        if(artist == null)
                            isArtistCorrect = false;
                        tracks.validateEditTrack(response, isArtistCorrect, isGenreCorrect);
                        return response;
                    default:
                        response.setUnknownError(true);
                        return response;
                }
            case REMOVE:
                switch (keys[1]) {
                    case GENRE:
                        genres.validateRemoveGenre(response);
                        return response;
                    case ARTIST:
                        artists.validateRemoveArtist(response);
                        return response;
                    case TRACK:
                        tracks.validateRemoveTrack(response);
                        return response;
                    default:
                        response.setUnknownError(true);
                        return response;
                }
            case SAVE:
                String filePath = response.getArguments()[0];
                if(response.getKeys().length == 1) {
                    File file = new File(filePath);
                    if (file.exists() && file.isFile()) {
                        response.setFileExistsError(true);
                        return response;
                    }
                }
                try {
                    new FileOutputStream(response.getArguments()[0]).close();
                } catch (IOException e) {
                    response.setFileError(true);
                }
                return response;
            case LOAD:
                try {
                    new FileInputStream(response.getArguments()[0]).close();
                } catch (IOException e) {
                    response.setFileError(true);
                }
                return response;
            case GET:
                if (response.getArguments()[0].equals("all"))
                    return response;
            case FIND:
                return response;
            default:
                response.setUnknownError(true);
                return response;
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
            case ARTIST:
                artists.addArtist(arguments[0]);
                break;
            case TRACK:
                String genreName = "";
                String artistName = "";
                if(arguments.length == 3) {
                    Artist artist = artists.getArtist(arguments[1]);
                    Genre genre = genres.getGenre(arguments[2]);
                    if(artist != null)
                        artistName = artist.getName();
                    if(genre != null)
                        genreName = genre.getName();
                }
                if(artistName.equals(""))
                    command.setTrackWithoutArtistWarning(true);
                if(genreName.equals(""))
                    command.setTrackWithoutGenreWarning(true);
                tracks.addTrack(arguments[0], artistName, genreName);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private synchronized void executeEdit(Response command) {
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        int clientID = command.getClientID();
        switch (keys[1]) {
            case GENRE:
                tracks.editGenreName(genres.getGenre(arguments[0]).getName(), arguments[1]);
                genres.editName(arguments[0], arguments[1]);
                genres.unlockGenre(arguments[0]);
                break;
            case ARTIST:
                tracks.editArtistName(artists.getArtist(arguments[0]).getName(), arguments[1]);
                artists.editName(arguments[0], arguments[1]);
                artists.unlockArtist(arguments[0]);
                break;
            case TRACK:
                int id = Parser.parseID(arguments[0]);
                tracks.editName(id, arguments[1]);
                //tracks.editArtist(id, arguments[2]);
                Artist artist = artists.getArtist(arguments[2]);
                String newArtist = artist == null ? "" : artist.getName();
                if(newArtist.equals(""))
                    command.setTrackWithoutArtistWarning(true);
                tracks.editArtist(id, newArtist);
                Genre genre = genres.getGenre(arguments[3]);
                String newGenre = genre == null ? "" : genre.getName();
                if(newGenre.equals(""))
                    command.setTrackWithoutGenreWarning(true);
                tracks.editGenre(id, newGenre);
                tracks.unlockTrack(arguments[0]);
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
                Genre genre = genres.getGenre(arguments[0]);
                if(genre != null)
                    tracks.setGenreToNull(genre.getName());
                genres.removeGenre(arguments[0]);
                break;
            case ARTIST:
                Artist artist = artists.getArtist(arguments[0]);
                if(artist != null)
                    tracks.setArtistToNull(artist.getName());
                artists.removeArtist(arguments[0]);
                break;
            case TRACK:
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
            marshaller.marshal(new Wrapper(tracks.getAllTracks(), artists.getAllArtists(), genres.getAllGenres()), new File(filePath));
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
        Artist[] artists = null;
        Genre[] genres = null;
        try {
            tracks = res.getT();
            artists = res.getA();
            genres = res.getG();
            for (Track track : tracks) {
                if(track.getName() == null || track.getArtist() == null || track.getGenre() == null) {
                    command.setFileIsCorruptedError(true);
                    return;
                }
            }
            for (Artist artist : artists) {
                if(artist.getName() == null) {
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
            this.artists.addReadArtist(artists, tracks, true);
            this.tracks.addReadTracks(tracks, true);
        }
        else {
            this.genres.addReadGenres(genres, tracks, false);
            this.artists.addReadArtist(artists, tracks, false);
            this.tracks.addReadTracks(tracks, false);
        }
    }

    public synchronized void executeLock(Response command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                genres.lockGenre(arguments[0], command.getClientID());
                break;
            case ARTIST:
                artists.lockArtist(arguments[0], command.getClientID());
                break;
            case TRACK:
                tracks.lockTrack(arguments[0], command.getClientID());
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public synchronized void executeUnlock(Response command){
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                genres.unlockGenre(arguments[0]);
                break;
            case ARTIST:
                artists.unlockArtist(arguments[0]);
                break;
            case TRACK:
                tracks.unlockTrack(arguments[0]);
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
            case UNLOCK:
                executeUnlock(command);
                break;
            case FIND:
                executeFind(command);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void unlockByID(int id) {
        synchronized (this) {
            genres.unlockAll(id);
            artists.unlockAll(id);
            tracks.unlockAll(id);
        }
    }

    public Wrapper getAllData() {
        Wrapper data = new Wrapper();
        synchronized (this) {
            data.setGenres(genres.getAllGenres());
            data.setArtists(artists.getAllArtists());
            data.setTracks(tracks.getAllTracks());
        }
        return data;
    }
}