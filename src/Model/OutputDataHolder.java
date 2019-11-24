package Model;

import DataHolder.Key;

public class OutputDataHolder {

    private boolean indexError;
    private boolean genreEqualsNameError;
    private boolean objectNotFoundedError;
    private boolean trackWithoutGenreWarning;
    private Key[] keys;
    private String[] arguments;

    OutputDataHolder(Key[] keys, String[] arguments) {
        this.keys = keys;
        this.arguments = arguments;
    }

    public Key[] getKeys() {
        return keys.clone();
    }

    public String[] getArguments() {
        return arguments.clone();
    }

    public boolean isObjectNotFoundedError() {
        return objectNotFoundedError;
    }

    public void setObjectNotFoundedError(boolean objectNotFoundedError) {
        this.objectNotFoundedError = objectNotFoundedError;
    }

    public boolean isIndexError() {
        return indexError;
    }

    void setIndexError(boolean indexError) {
        this.indexError = indexError;
    }

    public boolean isGenreEqualsNameError() {
        return genreEqualsNameError;
    }

    void setGenreEqualsNameError(boolean genreEqualsNameError) {
        this.genreEqualsNameError = genreEqualsNameError;
    }

    public boolean isTrackWithoutGenreWarning() {
        return trackWithoutGenreWarning;
    }

    void setTrackWithoutGenreWarning(boolean trackWithoutGenreWarning) {
        this.trackWithoutGenreWarning = trackWithoutGenreWarning;
    }

    public boolean hasErrors() {return indexError || genreEqualsNameError || objectNotFoundedError;}
    public boolean hasWarnings() {return trackWithoutGenreWarning;}
}
