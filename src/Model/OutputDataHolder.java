package Model;

import Parse.Key;

public class OutputDataHolder {

    private boolean indexError;
    private boolean genreEqualsNameError;
    private boolean objectNotFoundError;
    private boolean trackWithoutGenreWarning;
    private boolean fileError;
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

    public boolean isObjectNotFoundError() {
        return objectNotFoundError;
    }

    void setObjectNotFoundError(boolean objectNotFoundError) {
        this.objectNotFoundError = objectNotFoundError;
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

    public boolean isFileError() {return fileError;}

    void setFileError(boolean fileError) {this.fileError = fileError;}

    public boolean isTrackWithoutGenreWarning() {
        return trackWithoutGenreWarning;
    }

    void setTrackWithoutGenreWarning(boolean trackWithoutGenreWarning) {
        this.trackWithoutGenreWarning = trackWithoutGenreWarning;
    }

    public boolean hasErrors() {return indexError || genreEqualsNameError || objectNotFoundError;}
    public boolean hasWarnings() {return trackWithoutGenreWarning;}
}
