package worklib.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(propOrder = {"name"})
public class Genre implements Comparable<Genre>, Serializable {

    private String name;
    private int id;
    @XmlTransient
    private boolean isLocked = false;

    public static class CounterOfGenre {
        private static int counter = 1;
    }

    public Genre() {}

    public Genre(String name) {
        this.name = name;
        this.id = CounterOfGenre.counter++;
    }

    public Genre(String name, boolean noID) {
        this.name = name;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setNewId() { this.id = CounterOfGenre.counter++; }

    @XmlTransient
    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String toString(){
        return name;
    }

    public int compareTo(Genre genre){
        return Integer.compare(id, genre.id);
    }

    @Override
    public Genre clone() {
        Genre clone = new Genre(name, true);
        clone.id = id;
        return clone;
    }
}
