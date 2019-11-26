package Model;

import Controller.InputDataHolder;
import Parse.Parser;
import Parse.Key;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class Model {

    private Tracks tracks;
    private Genres genres;

    public Model(TrackList tracks, Genres genres) {
        this.tracks = tracks;
        this.genres = genres;
    }

    //убрать излишнюю вложенность(доделаю позже)
    public OutputDataHolder validate(InputDataHolder command) {
        Key[] keys = command.getKeys();
        switch (keys[0]) {
            case ADD:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateAddGenre(command);
                    case TRACK:
                        return tracks.validateAddTrack(command);//ошибка при добавлении без жанра
                    default:
                        throw new IllegalArgumentException();//?
                }
            case EDIT:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateEditGenre(command);
                    case TRACK:
                        if (keys[2] == Key.GENRE) return tracks.validateEditByGenreTrack(command);
                        else
                            return tracks.validateEditByArtistOrNameTrack(command);
                    default:
                        throw new IllegalArgumentException(); //?
                }
            case REMOVE:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateRemoveGenre(command);
                    case TRACK:
                        return tracks.validateRemoveTrack(command);
                    default:
                        throw new IllegalArgumentException(); //?
                }
            case SAVE:
                OutputDataHolder result = new OutputDataHolder(command.getKeys(), command.getArguments());
                try {
                    new FileOutputStream(command.getArguments()[0]).close();
                } catch (IOException e) {
                    result.setFileError(true);
                }
                return result;
            case LOAD:
                result = new OutputDataHolder(command.getKeys(), command.getArguments());
                try {
                    new FileInputStream(command.getArguments()[0]).close();
                } catch (IOException e) {
                    result.setFileError(true);
                }
                return result;
            default:
                throw new IllegalArgumentException(); //?
        }
    }

    public Track[] viewTrack(InputDataHolder command) {
        String[] arguments = command.getArguments();
        int id;
        if (arguments[0].equals("all")) return tracks.getAllTracks();
        else {
            id = Parser.parseID(arguments[0]);
            Track track = tracks.getTrack(id);
            return track == null ?
                    new Track[0]
                    :new Track[]{track};
        }
    }

    public Genre[] viewGenre(InputDataHolder command) {
        String[] arguments = command.getArguments();
        if (arguments[0].equals("all")) return genres.getAllGenres();
        else {
            Genre genre = genres.getGenre(arguments[0]);
            return genre == null ?
                    new Genre[0]
                    :new Genre[]{genre};
        }
    }

    public Track[] findTracks(InputDataHolder command) {
        String[] args = command.getArguments();
        return tracks.find(args[0], args[1], args[2]);
    }

    private void executeAdd(OutputDataHolder command) {
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

    private void executeEdit(OutputDataHolder command) {
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                genres.editName(arguments[0], arguments[1]);
                tracks.editGenreName(arguments[0], arguments[1]);
                break;
            case TRACK:
                int id = Parser.parseID(arguments[0]);
                switch (keys[2]) {
                    case NAME:
                        tracks.editName(id, arguments[1]);
                        break;
                    case ARTIST:
                        tracks.editArtist(id, arguments[1]);
                        break;
                    case GENRE:
                        Genre genre = genres.getGenre(arguments[1]);
                        String newGenre = genre == null ? "" : genre.getName();
                        if(newGenre.equals(""))
                            command.setTrackWithoutGenreWarning(true);
                        tracks.editGenre(id, newGenre);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void executeRemove(OutputDataHolder command) {
        Key[] keys = command.getKeys();
        String[] arguments = command.getArguments();
        switch (keys[1]) {
            case GENRE:
                if (arguments[0].equals("all")) {
                    tracks.setAllGenreToNull();
                    genres.removeAllGenres();
                }
                else {
                    tracks.setGenreToNull(arguments[0]);
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

    public void execute(OutputDataHolder command) {
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
                saveIntoFile(arguments[0]);
                break;
            case LOAD:
                loadFromFile(arguments[0]);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void saveIntoFile(String filePath) {
        XMLEncoder encoder= null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filePath)));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }
        encoder.writeObject(tracks.getAllTracks());
        encoder.writeObject(genres.getAllGenres());
        encoder.close();
    }

    private void loadFromFile(String filePath) {
        XMLDecoder decoder = null;
        try {
            decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filePath)));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }
        Track[] tracks = (Track[]) decoder.readObject();
        Genre[] genres = (Genre[]) decoder.readObject();
        decoder.close();
        this.genres.addReadGenres(genres);
        this.tracks.addReadTracks(tracks);
    }
}