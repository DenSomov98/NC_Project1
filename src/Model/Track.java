package Model;

import java.io.Serializable;

public class Track implements Comparable<Track>, Serializable {

    private String name;
    private String artist;
    private String genre;

    public Track() {}

    public Track(String name, String artist, String genre) {
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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String toString(){
        if (genre == null) return name+":"+" "+artist+","+" "+"жанр отсутствует";
        return name+":"+" "+artist+","+" "+genre;
    }

    public boolean searchCompare(String name, String artist, String genre) {
        if(!this.name.equals(name) && !name.equals("*"))
            return false;
        if(!this.artist.equals(artist) && !artist.equals("*"))
            return false;
        if(this.genre == null || !this.genre.equals(genre) && !genre.equals("*"))
            return false;
        return true;
    }

    public int compareTo(Track track){
        return name.compareTo(track.name) == 0 ?
                artist.compareTo(track.artist) == 0 ?
                genre.compareTo(track.genre)
                : artist.compareTo(track.artist)
                : name.compareTo(track.name);
    }
}
