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

    //нужен метод(ы) для работы с view

    /*public void execute(OutputDataHolder ) {

    }*/
}