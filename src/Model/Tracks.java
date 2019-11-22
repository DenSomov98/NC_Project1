package Model;

public interface Tracks {

    public void addTrack(String name, String artist, String genreName);

    public void removeTrack(int index);

    public void editName(int index, String newName);

    public void editArtist(int index, String newArtist);

    public void editGenre(int index, String newGenreName);

    //public void setGenreToNull(String genreName);//////////мб станет не нужен

    public Track getTrack(int id);

    public Track[] getAllTracks();
}
