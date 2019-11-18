package Model;

public class Genre implements Comparable<Genre> {
    private String name;

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
        return name.compareTo(genre.getName());
    }
}
