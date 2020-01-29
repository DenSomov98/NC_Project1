package worklib.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(propOrder = {"name"})
public class Artist implements Comparable<Artist>, Serializable {

    private String name;
    private int id;
    @XmlTransient
    private int lockID = -1;

    public static class CounterOfArtist {
        private static int counter = 1;
    }

    public Artist() {}

    public Artist(String name) {
        this.name = name;
        this.id = worklib.entities.Artist.CounterOfArtist.counter++;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Artist(String name, boolean noID) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setNewId() { this.id = worklib.entities.Artist.CounterOfArtist.counter++; }

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
        return name;
    }

    public int compareTo(worklib.entities.Artist artist){
        return Integer.compare(id, artist.id);
    }

    @Override
    public Artist clone() {
        Artist clone = new Artist(name, true);
        clone.id = id;
        return clone;
    }
}

