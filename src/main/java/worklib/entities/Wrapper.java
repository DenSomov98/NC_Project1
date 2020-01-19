package worklib.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "music-storage")
public class Wrapper implements Serializable {
    @XmlElementWrapper(nillable = true, name = "track-list")
    @XmlElement(name = "track")
    private Track[] tracks;
    @XmlElementWrapper(nillable = true, name = "genre-list")
    @XmlElement(name = "genre")
    private Genre[] genres;

    public Wrapper() {}
    public Wrapper(Track[] tracks, Genre[] genres) {
        this.tracks = tracks;
        this.genres = genres;
    }

    public Track[] getT() {
        return tracks;
    }

    public void setTracks(Track[] tracks) {
        this.tracks = tracks;
    }

    public Genre[] getG() {
        return genres;
    }

    public void setGenres(Genre[] genres) {
        this.genres = genres;
    }
}

