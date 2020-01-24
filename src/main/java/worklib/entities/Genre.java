package worklib.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(propOrder = {"name"})
public class Genre implements Comparable<Genre>, Serializable {

    private String name;
    private int id;
    private boolean isLocked = false;

    public static class CounterOfGenre {
        private static int counter = 1;
    }

    public Genre() {}

    public Genre(String name) {
        this.name = name;
        this.id = CounterOfGenre.counter++;
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
}
