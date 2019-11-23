package Model;

import DataHolder.InputDataHolder;
import DataHolder.Key;

public class Model {
    private Tracks tracks;
    private Genres genres;

    public Model(Tracks tracks, Genres genres) {
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
                        return tracks.validateAddTrack(command, genres.getGenre(command.getArguments()[2]));
                    default:
                        return null; //?
                }
            case EDIT:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateAddGenre(command);
                    case TRACK:
                        return tracks.validateEditTrack(command, genres.getGenre(command.getArguments()[2]));
                    default:
                        return null; //?
                }
            case REMOVE:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateRemoveGenre(command);
                    case TRACK:
                        return tracks.validateRemoveTrack(command);
                    default:
                        return null; //?
                }
            default:
                return null; //?
        }
    }

    public Track[] viewTrack(InputDataHolder command) {
        String[] arguments = command.getArguments();
        int id = -1;
        if (arguments[0].equals("all")) return tracks.getAllTracks();
        else {
            try {
                id = Integer.parseInt(arguments[0]);
                Track[] track = new Track[1];
                track[0] = tracks.getTrack(id);
                System.out.println(track[0].getName());
                return track;
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    public Genre[] viewGenre(InputDataHolder command) {
        String[] arguments = command.getArguments();
        int id = -1;
        if (arguments[0].equals("all")) return genres.getAllGenres();
        else {
            try {
                id = Integer.parseInt(arguments[0]);
                Genre[] genre = new Genre[1];
                genre[0] = genres.getGenre(id);
                return genre;
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private int parseID(String s) {
        int id;
        try {
            id = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {throw new IllegalArgumentException();}
        return id;
    }

    private void executeAdd(Key[] keys, String[] arguments) {
        switch (keys[1]) {
            case GENRE:
                genres.addGenre(arguments [0]);
                break;
            case TRACK:
                Genre genre = null;
                if(arguments.length == 3)
                    genre = genres.getGenre(arguments[2]);
                tracks.addTrack(arguments[0], arguments[1], genre);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void executeEdit(Key[] keys, String[] arguments) {
        switch (keys[1]) {
            case GENRE:
                Genre genre = genres.getGenre(arguments[0]);
                if(genre == null) {
                    genre = genres.getGenre(parseID(arguments[0]));
                }
                genre.setName(arguments[1]);
                break;
            case TRACK:
                int id = parseID(arguments[0]);
                switch (keys[2]) {
                    case NAME:
                        tracks.editName(id, arguments[1]);
                        break;
                    case ARTIST:
                        tracks.editArtist(id, arguments[1]);
                        break;
                    case GENRE:
                        genre = genres.getGenre(arguments[0]);
                        if(genre == null) {
                            genre = genres.getGenre(parseID(arguments[0]));
                        }
                        tracks.editGenre(id, genre);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void executeRemove(Key[] keys, String[] arguments) {
        switch (keys[1]) {
            case GENRE:
                if (arguments[0].equals("all"))
                    genres.removeAllGenres();
                else
                    genres.removeGenre(parseID(arguments[0]));
                break;
            case TRACK:
                if (arguments[0].equals("all"))
                    tracks.removeAllTracks();
                else
                    tracks.removeTrack(parseID(arguments[0]));
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
                executeAdd(keys, arguments);
                break;
            case EDIT:
                executeEdit(keys, arguments);
                break;
            case REMOVE:
                executeRemove(keys, arguments);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}