package Parse;

import Controller.InputDataHolder;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Parser {

    public static int parseID(String num) {
        int id = -1;
        try {
            id = Integer.parseInt(num);
        }
        catch (NumberFormatException ignored) {}
        return Integer.toString(id).equals(num) ? id : -1;
    }

    public static boolean matchesThePattern(String string, String subString) {
        return string.toLowerCase().contains(subString.toLowerCase());
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
            case "duplicate":
                return Key.DUPLICATE;
            case "overwrite":
                return Key.OVERWRITE;
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

    private static InputDataHolder incorrectCommand() { return InputDataHolder.makeIncorrect();}

    private static InputDataHolder firstIsViewOrRemove(ArrayList<Key> keys, StringTokenizer stringTokenizer,
                                                ArrayList<String> arguments) {
        if(stringTokenizer.countTokens()!=2)
            return incorrectCommand();
        String token = stringTokenizer.nextToken();
        Key k = Parser.tokenCode(token);
        keys.add(k);
        switch (k) {
            case GENRE:
            case TRACK:
                token = stringTokenizer.nextToken();
                arguments.add(token);
                int id = Parser.parseID(token);
                return token.equals("all") || id >= 0 || k == Key.GENRE ?
                        new InputDataHolder(true, keys, arguments)
                        : incorrectCommand();
            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder firstIsAdd(ArrayList<Key> keys, StringTokenizer stringTokenizer,
                                       ArrayList<String> arguments) {
        if(stringTokenizer.countTokens() < 2)
            return incorrectCommand();
        String token = stringTokenizer.nextToken();
        Key k = Parser.tokenCode(token);
        keys.add(k);
        switch (k) {
            case GENRE:
                token = stringTokenizer.nextToken();
                arguments.add(token);
                return stringTokenizer.hasMoreTokens() ?
                        incorrectCommand()
                        : new InputDataHolder(true, keys, arguments);
            case TRACK:
                while (stringTokenizer.hasMoreTokens()) {
                    token = stringTokenizer.nextToken();
                    arguments.add(token);
                }
                return (arguments.size() < 2 || arguments.size() > 3) ?
                        incorrectCommand()
                        : new InputDataHolder(true, keys, arguments);
            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder firstIsEdit(ArrayList<Key> keys, StringTokenizer stringTokenizer,
                                        ArrayList<String> arguments) {
        if(stringTokenizer.countTokens() != 4)
            return incorrectCommand();
        String token = stringTokenizer.nextToken();
        Key k = Parser.tokenCode(token);
        keys.add(k);
        switch (k) {
            case GENRE:
                token = stringTokenizer.nextToken();
                k = Parser.tokenCode(token);
                if (k != Key.NAME)
                    return incorrectCommand();
                keys.add(k);
                arguments.add(stringTokenizer.nextToken());
                arguments.add(stringTokenizer.nextToken());
                return new InputDataHolder(true, keys, arguments);

            case TRACK:
                token = stringTokenizer.nextToken();
                k = Parser.tokenCode(token);
                if (k != Key.NAME && k != Key.ARTIST && k != Key.GENRE)
                    return incorrectCommand();
                keys.add(k);
                token = stringTokenizer.nextToken();
                arguments.add(token);
                arguments.add(stringTokenizer.nextToken());
                int id = Parser.parseID(token);
                return  id >= 0 ?
                        new InputDataHolder(true, keys, arguments)
                        : incorrectCommand();
            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder firstIsFind(ArrayList<Key> keys, StringTokenizer stringTokenizer,
                                        ArrayList<String> arguments) {
        if (stringTokenizer.countTokens() != 4)
            return incorrectCommand();
        String token = stringTokenizer.nextToken();
        Key k = Parser.tokenCode(token);
        keys.add(k);
        switch (k) {

            case TRACK:
                for (int i = 0; i < 3; i++) {
                    arguments.add(stringTokenizer.nextToken());
                }
                return new InputDataHolder(true, keys, arguments);
            default:
                return incorrectCommand();
        }
    }

    public static InputDataHolder parsing(String command) {
        StringTokenizer stringTokenizer = new StringTokenizer(command, " <>");
        if(!stringTokenizer.hasMoreTokens())
            return incorrectCommand();
        ArrayList<Key> keys = new ArrayList<>(3);
        ArrayList<String> arguments = new ArrayList<>(3);

        String token = stringTokenizer.nextToken();
        Key k = Parser.tokenCode(token);
        keys.add(k);
        switch (k) {
            case VIEW: case REMOVE:
                return firstIsViewOrRemove(keys, stringTokenizer,arguments);

            case ADD:
                return firstIsAdd(keys, stringTokenizer, arguments);

            case EDIT:
                return firstIsEdit(keys, stringTokenizer, arguments);

            case FIND:
                return firstIsFind(keys, stringTokenizer, arguments);

            case SAVE: case LOAD:
                if( stringTokenizer.countTokens() != 1 )
                    return incorrectCommand();
                else {
                    arguments.add(stringTokenizer.nextToken());
                    return new InputDataHolder(true, keys, arguments);
                }

            case HELP: case EXIT:
                return stringTokenizer.hasMoreTokens() ?
                        incorrectCommand()
                        : new InputDataHolder(true, keys);
            default:
                return incorrectCommand();
        }
    }

    public static void RegEx(StringBuilder string, String s){
        int index = 0;
        string.insert(index, "^");
        while(string.indexOf(s, index)>=0){
                int indexOfElement = string.indexOf(s, index);
                string.insert(indexOfElement , ".");
                index = indexOfElement + 2;
        }
        string.append("$");
    }
}
