package Model;

import DataHolder.InputDataHolder;

public interface Tracks {

    OutputDataHolder validateAddTrack(InputDataHolder command, Genre genre);

    void addTrack(String name, String artist, Genre genre);

    OutputDataHolder validateRemoveTrack(InputDataHolder command);

    void removeTrack(int index);

    void removeAllTracks();

    OutputDataHolder validateEditByArtistOrNameTrack(InputDataHolder command);

    void editName(int index, String newName);

    void editArtist(int index, String newArtist);

    OutputDataHolder validateEditByGenreTrack(InputDataHolder command, Genre genre);

    void editGenre(int index, Genre newGenre);

    //void setGenreToNull(String genreName);//////////мб станет не нужен

    Track getTrack(int id);

    Track[] getAllTracks();

}
