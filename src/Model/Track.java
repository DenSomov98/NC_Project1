package Model;


public class Track implements Comparable<Track> {

    private String name;
    private String artist;
    private Genre genre;

    public Track(String name, String artist, Genre genre) {
        this.name = name;
        this.artist = artist;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String toString(){
        if (genre == null) return name+":"+" "+artist+","+" "+"жанр отсутствует";
        return name+":"+" "+artist+","+" "+genre.getName();
    }

    public boolean searchCompare(String name, String artist, String genre) {
        if(!this.name.equals(name) && !name.equals("*"))
            return false;
        if(!this.artist.equals(artist) && !artist.equals("*"))
            return false;
        if(this.genre != null && !this.genre.getName().equals(genre) && !genre.equals("*"))
            return false;
        return true;
    }

    public int compareTo(Track track){
        return name.compareTo(track.getName());
    }
}
