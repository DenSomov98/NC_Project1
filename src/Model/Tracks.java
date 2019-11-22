package Model;

import DataHolder.InputDataHolder;

public interface Tracks {

    public OutputDataHolder validateAddTrack(InputDataHolder command, Genre genre);

    public void addTrack(String name, String artist, Genre genre);

    public OutputDataHolder validateRemoveTrack(InputDataHolder command);

    public void removeTrack(int index);

    public void editName(int index, String newName);

    public void editArtist(int index, String newArtist);

    public OutputDataHolder validateEditTrack(InputDataHolder command, Genre genre);

    public void editGenre(int index, Genre newGenre);

    //public void setGenreToNull(String genreName);//////////мб станет не нужен

    public Track getTrack(int id);

    public Track[] getAllTracks();
}
