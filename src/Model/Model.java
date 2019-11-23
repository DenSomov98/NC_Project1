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

    /*public void execute(OutputDataHolder ) {

    }*/
}