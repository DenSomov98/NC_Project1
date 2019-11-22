package Model;

import DataHolder.Key;
import Model.Genre;

public class OutputDataHolder {

    private boolean indexError;
    private boolean genreEqualsNameError;
    private boolean trackWithoutGenreError;
    //private int id;
    private Key[] keys;
    private String[] arguments;

    //package-private
    OutputDataHolder(int id, String[] arguments) {
        //this.id = id;
        this.arguments = arguments;
    }

    //public int getId() {
     //   return id;
    ///}

    public String[] getArguments() {
        return arguments;
    }

    public boolean isIndexError() {
        return indexError;
    }

    public void setIndexError(boolean indexError) {
        this.indexError = indexError;
    }

    public boolean isGenreEqualsNameError() {
        return genreEqualsNameError;
    }

    public void setGenreEqualsNameError(boolean genreEqualsNameError) {
        this.genreEqualsNameError = genreEqualsNameError;
    }

    public boolean isTrackWithoutGenreError() {
        return trackWithoutGenreError;
    }

    public void setTrackWithoutGenreError(boolean trackWithoutGenreError) {
        this.trackWithoutGenreError = trackWithoutGenreError;
    }
}
