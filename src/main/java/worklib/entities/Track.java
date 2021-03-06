package worklib.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(propOrder = {"name", "artist", "genre"})
public class Track implements Comparable<Track>, Serializable {

    private String name;
    private String artist;
    private String genre;
    @XmlTransient
    private int id;
    @XmlTransient
    private int lockID = -1;

    public static class CounterOfTrack {
        private static int counter = 1;
    }

    public Track() {}

    public Track(String name, String artist, String genre) {
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.id = CounterOfTrack.counter++;
    }

    public Track(String name, String artist, String genre, boolean noID) {
        this.name = name;
        this.artist = artist;
        this.genre = genre;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "artist")
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @XmlElement(name = "genre")
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setNewId() { this.id = CounterOfTrack.counter++; }

    @XmlTransient
    public int getLockID() {
        return lockID;
    }

    public void setLockID(int id) {
        this.lockID = id;
    }

    public void unLock() {
        this.lockID = -1;
    }

    public String toString(){
        if (genre.equals("")) return name+":"+" "+artist+","+" "+"жанр отсутствует";
        return name+":"+" "+artist+","+" "+genre;
    }

    public boolean equals(Track track){
        return name.equalsIgnoreCase(track.name) &&
                artist.equalsIgnoreCase(track.artist) &&
                        genre.equalsIgnoreCase(track.genre);
        /*name.compareToIgnoreCase(track.name) == 0 ?
                artist.compareToIgnoreCase(track.artist) == 0 ?
                genre.compareToIgnoreCase(track.genre)
                : artist.compareToIgnoreCase(track.artist)
                : name.compareToIgnoreCase(track.name);*/
    }

    public int compareTo(Track track){
        return Integer.compare(id, track.id);
    }

    @Override
    public Track clone() {
        Track clone = new Track(name, artist, genre, true);
        clone.id = id;
        return clone;
    }
}
