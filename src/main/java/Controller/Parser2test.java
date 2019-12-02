package Controller;

import Parse.Key;
import Parse.Parser;

import java.util.ArrayList;

public class Parser2test {
    private static InputDataHolder incorrectCommand() { return InputDataHolder.makeIncorrect();}

    private static InputDataHolder firstIsViewOrRemove(ArrayList<Key> keys, ArrayList<String> arguments) {
        if(keys.size() != 2 || arguments.size() !=1)
            return incorrectCommand();
        switch (keys.get(1)) {
            case GENRE:
            case TRACK:
                String arg = arguments.get(0);
                int id = Parser.parseID(arg);
                return arg.equals("all") || id >= 0 || keys.get(1) == Key.GENRE ?
                        new InputDataHolder(true, keys, arguments)
                        : incorrectCommand();
            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder firstIsAdd(ArrayList<Key> keys, ArrayList<String> arguments) {
        if(keys.size() != 2)
            return incorrectCommand();
        switch (keys.get(1)) {
            case GENRE:
                return arguments.size() != 1 ?
                        incorrectCommand()
                        : new InputDataHolder(true, keys, arguments);
            case TRACK:
                return (arguments.size() < 2 || arguments.size() > 3) ?
                        incorrectCommand()
                        : new InputDataHolder(true, keys, arguments);
            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder firstIsEdit(ArrayList<Key> keys, ArrayList<String> arguments) {
        if(keys.size() != 3 || arguments.size() != 2)
            return incorrectCommand();
        switch (keys.get(1)) {
            case GENRE:
                if (keys.get(2) != Key.NAME)
                    return incorrectCommand();
                return new InputDataHolder(true, keys, arguments);

            case TRACK:
                Key k = keys.get(2);
                if (k != Key.NAME && k != Key.ARTIST && k != Key.GENRE)
                    return incorrectCommand();
                int id = Parser.parseID(arguments.get(0));
                return  id >= 0 ?
                        new InputDataHolder(true, keys, arguments)
                        : incorrectCommand();
            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder firstIsFind(ArrayList<Key> keys, ArrayList<String> arguments) {
        if (keys.size() != 2 || arguments.size() != 3)
            return incorrectCommand();
        switch (keys.get(1)) {

            case TRACK:
                return new InputDataHolder(true, keys, arguments);
            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder firstIsSave(ArrayList<Key> keys, ArrayList<String> arguments) {
        if (keys.size() > 2 || arguments.size() != 1)
            return incorrectCommand();
        if(keys.size() == 1 || keys.size() ==2  && keys.get(1) == Key.OVERWRITE)
            return new InputDataHolder(true, keys, arguments);
        else
            return incorrectCommand();
    }

    private static InputDataHolder firstIsLoad(ArrayList<Key> keys, ArrayList<String> arguments) {
        if (keys.size() != 2 || arguments.size() != 1)
            return incorrectCommand();
        switch (keys.get(1)) {
            case OVERWRITE:
            case DUPLICATE:
                return new InputDataHolder(true, keys, arguments);

            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder parsedCommand(ArrayList<Key> keys, ArrayList<String> arguments) {
        switch (keys.get(0)) {
            case VIEW:
            case REMOVE:
                return firstIsViewOrRemove(keys, arguments);

            case ADD:
                return firstIsAdd(keys, arguments);

            case EDIT:
                return firstIsEdit(keys, arguments);

            case FIND:
                return firstIsFind(keys, arguments);

            case SAVE:
                return firstIsSave(keys, arguments);

            case LOAD:
                return firstIsLoad(keys, arguments);

            default:
                return incorrectCommand();
        }
    }

    private static InputDataHolder withoutArgs(String[] command) {
        ArrayList<Key> keys = new ArrayList<>(1);
        Key k = Parser.tokenCode(command[0].trim());
        switch (k) {
            case HELP: case EXIT:
                keys.add(k);
                return command.length != 1 ?
                        incorrectCommand()
                        : new InputDataHolder(true, keys);
            default:
                return incorrectCommand();
        }
    }

    private static ArrayList<Key> getCorrectRawKeys(String command) {
        ArrayList<Key> result = new ArrayList<>();
        String[] rawKeys = command.split(" ");
        if(rawKeys.length > 3 || rawKeys.length == 0)
            return null;
        for(String s : rawKeys) {
            Key k = Parser.tokenCode(s);
            if(k==Key.NOT_A_KEY)
                return null;
            else result.add(k);
        }
        return result;
    }

    private static ArrayList<String> getCorrectRawArgs(String command){
        String[] args = command.split("\"");
        if(args.length > 5 || args.length % 2 == 0)
            return null;

        ArrayList<String> result = new ArrayList<>(3);
        for (int i = 1; i < args.length; i+=2) {
            if(!args[i].trim().isEmpty())
                return null;
        }
        for (int i = 0; i < args.length; i+=2) {
            String arg = args[i].trim();
            if(arg.isEmpty())
                return null;
            else result.add(arg);
        }
        return result;
    }

    public static InputDataHolder parsing(String command) {
        String[] halves = command.split(" \"", 2);
        if(halves.length == 1)
            return withoutArgs(halves[0].split(" "));
        ArrayList<Key> keys = getCorrectRawKeys(halves[0]);
        ArrayList<String> arguments = getCorrectRawArgs(halves[1]);
        if(keys == null || arguments == null)
            return incorrectCommand();
        else
            return parsedCommand(keys, arguments);
    }
}
