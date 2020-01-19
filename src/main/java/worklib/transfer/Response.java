package worklib.transfer;

import worklib.parse.Key;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {

    private boolean unknownError;
    private boolean indexError;
    private boolean genreEqualsNameError;
    private boolean objectNotFoundError;
    private boolean fileError;
    private boolean fileIsCorruptedError;
    private boolean fileIsEmptyWarning;
    private boolean trackWithoutGenreWarning;
    private boolean fileExistsError;
    private boolean alreadyLockedError;
    private Key[] keys;
    private String[] arguments;

    public Response(boolean isSuccessful, Key[] keys) {
        this.keys = keys.clone();
        this.arguments = null;
    }

    public Response(Key[] keys, String[] arguments) {
        this.keys = keys.clone();
        this.arguments = arguments.clone();
    }

    public Key[] getKeys() {
        return keys.clone();
    }

    public String[] getArguments() {
        return arguments.clone();
    }

    public boolean isUnknownError() {
        return unknownError;
    }

    public void setUnknownError(boolean unknownError) {
        this.unknownError = unknownError;
    }

    public boolean isObjectNotFoundError() {
        return objectNotFoundError;
    }

    public void setObjectNotFoundError(boolean objectNotFoundError) {
        this.objectNotFoundError = objectNotFoundError;
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

    public boolean isFileError() {return fileError;}

    public void setFileError(boolean fileError) {this.fileError = fileError;}

    public boolean isFileIsCorruptedError() {
        return fileIsCorruptedError;
    }

    public void setFileIsCorruptedError(boolean fileIsCorruptedError) {
        this.fileIsCorruptedError = fileIsCorruptedError;
    }

    public boolean isTrackWithoutGenreWarning() {
        return trackWithoutGenreWarning;
    }

    public void setTrackWithoutGenreWarning(boolean trackWithoutGenreWarning) {
        this.trackWithoutGenreWarning = trackWithoutGenreWarning;
    }

    public boolean isFileIsEmptyWarning() {
        return fileIsEmptyWarning;
    }

    public boolean isFileExistsError() {
        return fileExistsError;
    }

    public void setFileExistsError(boolean fileExistsError) {
        this.fileExistsError = fileExistsError;
    }

    public void setFileIsEmptyWarning(boolean fileIsEmptyWarning) {
        this.fileIsEmptyWarning = fileIsEmptyWarning;
    }

    public boolean isAlreadyLockedError() {
        return alreadyLockedError;
    }

    public void setAlreadyLockedError(boolean alreadyLockedError) {
        this.alreadyLockedError = alreadyLockedError;
    }

    public boolean hasErrors() {return unknownError || indexError || genreEqualsNameError || objectNotFoundError
            || fileError || fileIsCorruptedError || fileExistsError || alreadyLockedError;}

    public boolean hasWarnings() {return trackWithoutGenreWarning || fileIsEmptyWarning;}

}
