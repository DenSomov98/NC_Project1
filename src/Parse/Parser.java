package Parse;

public class Parser {

    public static int parseID(String num) {
        int id = -1;
        try {
            id = Integer.parseInt(num);
        }
        catch (NumberFormatException ignored) {}
        return Integer.toString(id).equals(num) ? id : -1;
    }


    public static Key tokenCode(String token) {
        switch (token) {
            case "view":
                return Key.VIEW;
            case "add":
                return Key.ADD;
            case "edit":
                return Key.EDIT;
            case "remove":
                return Key.REMOVE;
            case "find":
                return Key.FIND;
            case "save":
                return Key.SAVE;
            case "load":
                return Key.LOAD;
            case "track":
                return Key.TRACK;
            case "genre":
                return Key.GENRE;
            case "name":
                return Key.NAME;
            case "artist":
                return Key.ARTIST;
            case "exit":
                return Key.EXIT;
            case "help":
                return Key.HELP;
            default:
                return Key.NOT_A_KEY;
        }
    }
}
