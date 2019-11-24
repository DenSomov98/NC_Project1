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
                        Genre genre;
                        if (command.getArguments().length < 3) genre = null;
                        else genre = genres.getGenre(command.getArguments()[2]);
                        return tracks.validateAddTrack(command, genre);//ошибка при добавлении без жанра
                    default:
                        throw new IllegalArgumentException();//?
                }
            case EDIT:
                switch (keys[1]) {
                    case GENRE:
                        return genres.validateEditGenre(command);
                    case TRACK:
                        if (keys[2] == Key.GENRE) return tracks.validateEditByGenreTrack(command, genres.getGenre(command.getArguments()[1]));
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
            default:
                throw new IllegalArgumentException(); //?
        }
    }

    public Track[] viewTrack(InputDataHolder command) {
        String[] arguments = command.getArguments();
        int id = -1;
        if (arguments[0].equals("all")) return tracks.getAllTracks();
        else {
            id = parseID(arguments[0]);
            Track track = tracks.getTrack(id);
            return track == null ?
                    new Track[0]
                    :new Track[]{track};
        }
    }

    public Genre[] viewGenre(InputDataHolder command) {
        String[] arguments = command.getArguments();
        int id = -1;
        if (arguments[0].equals("all")) return genres.getAllGenres();
        else {
            id = parseID(arguments[0]);
            Genre genre = genres.getGenre(id);
            return genre == null ?
                    new Genre[0]
                    :new Genre[]{genre};
        }
    }

    static int parseID(String s) {
        int id = -1;
        try {
            id = Integer.parseInt(s);
        }
        catch (NumberFormatException ignored) {}
        return id;
    }

    private void executeAdd(Key[] keys, String[] arguments) {
        switch (keys[1]) {
            case GENRE:
                genres.addGenre(arguments [0]);
                break;
            case TRACK:
                Genre genre = null;
                int id;
                if(arguments.length == 3) {
                    genre = genres.getGenre(arguments[2]);
                    id = parseID(arguments[2]);
                    if (genre == null && id >= 0) {
                        genre = genres.getGenre(id);
                    }
                }
                tracks.addTrack(arguments[0], arguments[1], genre);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void executeEdit(Key[] keys, String[] arguments) {
        switch (keys[1]) {
            case GENRE:
                Genre genre = null;
                int id;
                if(arguments.length == 3) {
                    genre = genres.getGenre(arguments[2]);
                    id = parseID(arguments[2]);
                    if (genre == null && id >= 0) {
                        genre = genres.getGenre(id);
                    }
                }
                tracks.addTrack(arguments[0], arguments[1], genre);
                break;
            case TRACK:
                id = parseID(arguments[0]);
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
                if (arguments[0].equals("all")) {
                    tracks.setAllGenreToNull();
                    genres.removeAllGenres();
                }
                else {
                    tracks.setGenreToNull(genres.getGenre(parseID(arguments[0])));
                    genres.removeGenre(parseID(arguments[0]));
                }
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