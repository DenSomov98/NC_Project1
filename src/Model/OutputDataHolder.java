package Model;

import Parse.Key;

public class OutputDataHolder {

    private boolean indexError;
    private boolean genreEqualsNameError;
    private boolean objectNotFoundError;
    private boolean fileError;
    private boolean fileIsCorruptedError;
    private boolean fileIsEmptyWarning;
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

    public boolean isFileIsCorruptedError() {
        return fileIsCorruptedError;
    }

    void setFileIsCorruptedError(boolean fileIsCorruptedError) {
        this.fileIsCorruptedError = fileIsCorruptedError;
    }

    public boolean isTrackWithoutGenreWarning() {
        return trackWithoutGenreWarning;
    }

    void setTrackWithoutGenreWarning(boolean trackWithoutGenreWarning) {
        this.trackWithoutGenreWarning = trackWithoutGenreWarning;
    }

    public boolean isFileIsEmptyWarning() {
        return fileIsEmptyWarning;
    }

    void setFileIsEmptyWarning(boolean fileIsEmptyWarning) {
        this.fileIsEmptyWarning = fileIsEmptyWarning;
    }

    public boolean hasErrors() {return indexError || genreEqualsNameError || objectNotFoundError || fileError || fileIsCorruptedError;}
    public boolean hasWarnings() {return trackWithoutGenreWarning || fileIsEmptyWarning;}
}
