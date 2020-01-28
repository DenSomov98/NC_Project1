package worklib.transfer;

import worklib.parse.Key;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {

    private boolean unknownError;
    private boolean indexError;
    private boolean equalsNameError;
    private boolean objectNotFoundError;
    private boolean fileError;
    private boolean fileIsCorruptedError;
    private boolean fileIsEmptyWarning;
    private boolean objMatchesNoLongerWarning;
    private boolean trackWithoutGenreWarning;
    private boolean fileExistsError;
    private boolean alreadyLockedError;
    private boolean accessError;
    private int clientID;
    private Key[] keys;
    private String[] arguments;

    public Response(int clientID, Key[] keys, String[] arguments) {
        this.clientID = clientID;
        this.keys = keys.clone();
        this.arguments = arguments.clone();
    }

    public int getClientID() { return clientID; }

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

    public boolean isEqualsNameError() {
        return equalsNameError;
    }

    public void setEqualsNameError(boolean genreEqualsNameError) {
        this.equalsNameError = genreEqualsNameError;
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

    public boolean isAccessError() {
        return accessError;
    }

    public void setAccessError(boolean accessError) {
        this.accessError = accessError;
    }

    public boolean isObjMatchesNoLongerWarning() {return objMatchesNoLongerWarning; }

    public void setObjMatchesNoLongerWarning(boolean objMatchesNoLongerWarning) {
        this.objMatchesNoLongerWarning = objMatchesNoLongerWarning;
    }

    public boolean hasErrors() {return unknownError || indexError || equalsNameError || objectNotFoundError
            || fileError || fileIsCorruptedError || fileExistsError || alreadyLockedError || accessError;}

    public boolean hasWarnings() {return trackWithoutGenreWarning || fileIsEmptyWarning || objMatchesNoLongerWarning;}

}
