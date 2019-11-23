package Controller;

import DataHolder.Key;
import DataHolder.InputDataHolder;
import Model.*;
import View.View;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Controller {

    private Model model = new Model(new TrackList(), new GenreList());
    private View view = new View(model);

    private Key tokenCode(String token) {
        switch (token) {
            case "view":
                return Key.VIEW;
            case "add":
                return Key.ADD;
            case "edit":
                return Key.EDIT;
            case "remove":
                return Key.REMOVE;
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

    private InputDataHolder incorrectCommand() { return InputDataHolder.makeIncorrect();}

    private InputDataHolder firstIsViewOrRemove(ArrayList<Key> keys, StringTokenizer stringTokenizer,
                                        ArrayList<String> arguments) {
        if(stringTokenizer.countTokens()!=2)
            return incorrectCommand();
        String token = stringTokenizer.nextToken();
        Key k = tokenCode(token);
        keys.add(k);
        switch (k) {
            case GENRE:
            case TRACK:
                token = stringTokenizer.nextToken();
                arguments.add(token);
                int id = -1;
                try {
                    id = Integer.parseInt(token);
                } catch (NumberFormatException ignored) {}
                return token.equals("all") || id > 0 && Integer.toString(id).equals(token) ?
                        new InputDataHolder(true, keys, arguments)
                        : incorrectCommand();
            default:
                return incorrectCommand();
        }
    }

    private InputDataHolder firstIsAdd(ArrayList<Key> keys, StringTokenizer stringTokenizer,
                                        ArrayList<String> arguments) {
        if(stringTokenizer.countTokens() < 2)
            return incorrectCommand();
        String token = stringTokenizer.nextToken();
        Key k = tokenCode(token);
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

    private InputDataHolder firstIsEdit(ArrayList<Key> keys, StringTokenizer stringTokenizer,
                                       ArrayList<String> arguments) {
        if(stringTokenizer.countTokens() != 4)
            return incorrectCommand();
        String token = stringTokenizer.nextToken();
        Key k = tokenCode(token);
        keys.add(k);
        switch (k) {
            case GENRE:
                token = stringTokenizer.nextToken();
                k = tokenCode(token);
                if (k != Key.NAME)
                    return incorrectCommand();
                keys.add(k);
                arguments.add(stringTokenizer.nextToken());
                arguments.add(stringTokenizer.nextToken());
                return new InputDataHolder(true, keys, arguments);

            case TRACK:
                token = stringTokenizer.nextToken();
                k = tokenCode(token);
                if (k != Key.NAME && k != Key.ARTIST && k != Key.GENRE)
                    return incorrectCommand();
                keys.add(k);
                token = stringTokenizer.nextToken();
                arguments.add(token);
                arguments.add(stringTokenizer.nextToken());
                int id = -1;
                try {
                    id = Integer.parseInt(token);
                } catch (NumberFormatException ignored) {}
                return Integer.toString(id).equals(token) && id > 0 ?
                        new InputDataHolder(true, keys, arguments)
                        : incorrectCommand();
            default:
                return incorrectCommand();
        }
    }

    private InputDataHolder parsing(String command) {
        StringTokenizer stringTokenizer = new StringTokenizer(command, " <>");
        if(!stringTokenizer.hasMoreTokens())
            return incorrectCommand();
        ArrayList<Key> keys = new ArrayList<>(3);
        ArrayList<String> arguments = new ArrayList<>(3);
        String token = stringTokenizer.nextToken();
        Key k = tokenCode(token);
        keys.add(k);
        switch (k) {
            case VIEW:

            case REMOVE:
                return firstIsViewOrRemove(keys, stringTokenizer,arguments);

            case ADD:
                return firstIsAdd(keys, stringTokenizer, arguments);

            case EDIT:
                return firstIsEdit(keys, stringTokenizer, arguments);

            case HELP: case EXIT:
                return stringTokenizer.hasMoreTokens() ?
                        incorrectCommand()
                        : new InputDataHolder(true, keys);
            default:
                return incorrectCommand();
        }
    }

    private void performData(InputDataHolder parsed) {
        OutputDataHolder result = model.validate(parsed);
        if(result.hasErrors())
            view.printError(result);
        else {
            model.execute(result);
            view.printResult(result);
        }
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();
            InputDataHolder parsed = parsing(command);
            if(!parsed.isCorrect())
                View.showInputError();
            else{
                Key first = parsed.getKeys()[0];
                switch (first) {
                    case EXIT:
                        return;
                    case HELP:
                        view.printHelpMenu();
                        break;
                    case VIEW:
                        view.show(parsed);
                        break;
                    default:
                        performData(parsed);
                }
            }
        }
    }
}

