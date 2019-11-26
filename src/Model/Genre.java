package Model;

import java.io.Serializable;

public class Genre implements Comparable<Genre>, Serializable {
    private String name;

    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }

    public int compareTo(Genre genre){
        return name.compareToIgnoreCase(genre.getName());
    }
}
