package Controller;

import DataHolder.Key;
import DataHolder.InputDataHolder;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Controller {


    //private TrackModel trackModel = new TrackModel();
    //private GenreModel genreModel = new GenreModel();
    //private View view = new View(trackModel, genreModel);

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
            default:
                return Key.NOT_A_KEY;
        }
    }

    private InputDataHolder incorrectCommand() { return new InputDataHolder(true, null);}

    private InputDataHolder parsing(String command) {
        StringTokenizer stringTokenizer = new StringTokenizer(command, " <>");
        if(!stringTokenizer.hasMoreTokens())
            return incorrectCommand();
        ArrayList<Key> keys = new ArrayList<>(3);
        ArrayList<String> arguments = new ArrayList<>(3);
        String token = stringTokenizer.nextToken();
        Key k = tokenCode(token);
        switch (k) {
            case VIEW: {
                keys.add(k);
                token = stringTokenizer.nextToken();
                k = tokenCode(token);
                switch (k) {
                    case GENRE:
                    case TRACK:
                        keys.add(k);
                        token = stringTokenizer.nextToken();
                        if (stringTokenizer.hasMoreTokens())
                            return incorrectCommand();
                        int id;
                        try {

                            id = Integer.parseInt(token);
                        } catch (NumberFormatException e) {
                            return token.equals("all") ?
                                    new InputDataHolder(false, keys.toArray(new Key[2]), new String[]{token})
                                    : incorrectCommand();
                        }
                        return Integer.toString(id).equals(token) ?
                                new InputDataHolder(false, keys.toArray(new Key[2]), id, new String[]{token})
                                : incorrectCommand();
                    default:
                        return incorrectCommand();
                }
            }
            case ADD: {
                keys.add(k);
                token = stringTokenizer.nextToken();
                k = tokenCode(token);
                switch (k) {
                    case GENRE:
                        keys.add(k);
                        token = stringTokenizer.nextToken();
                        return stringTokenizer.hasMoreTokens() ?
                                incorrectCommand()
                                : new InputDataHolder(false, keys.toArray(new Key[2]), new String[]{token});
                    case TRACK:
                        keys.add(k);
                        while (stringTokenizer.hasMoreTokens() && arguments.size() < 3) {
                            token = stringTokenizer.nextToken();
                            arguments.add(token);
                        }
                        if (arguments.size() < 2 || arguments.size() > 3)
                            return incorrectCommand();
                        int id;
                        if (arguments.size() == 3) {
                            try {
                                id = Integer.parseInt(token);
                            } catch (NumberFormatException e) {
                                return new InputDataHolder(false, keys.toArray(new Key[2]), arguments.toArray(new String[3]));
                            }
                            return Integer.toString(id).equals(token) ?
                                    new InputDataHolder(false, keys.toArray(new Key[2]), id, arguments.toArray(new String[3]))
                                    : new InputDataHolder(false, keys.toArray(new Key[2]), arguments.toArray(new String[3]));
                        }
                        return new InputDataHolder(false, keys.toArray(new Key[2]), arguments.toArray(new String[2]));
                    default:
                        return incorrectCommand();
                }
            }
            case EDIT: {
                keys.add(k);
                token = stringTokenizer.nextToken();
                k = tokenCode(token);
                switch (k) {
                    case GENRE:
                        keys.add(k);
                        token = stringTokenizer.nextToken();
                        k = tokenCode(token);
                        if (k != Key.NAME || stringTokenizer.countTokens() != 2)
                            return incorrectCommand();
                        keys.add(k);
                        token = stringTokenizer.nextToken();
                        arguments.add(token);
                        arguments.add(stringTokenizer.nextToken());
                        int id;
                        try {
                            id = Integer.parseInt(token);
                        } catch (NumberFormatException e) {
                            return new InputDataHolder(false, keys.toArray(new Key[3]), arguments.toArray(new String[2]));
                        }
                        return Integer.toString(id).equals(token) ?
                                new InputDataHolder(false, keys.toArray(new Key[3]), id, arguments.toArray(new String[2]))
                                : new InputDataHolder(false, keys.toArray(new Key[3]), arguments.toArray(new String[2]));
                    case TRACK:
                        keys.add(k);
                        token = stringTokenizer.nextToken();
                        k = tokenCode(token);
                        if ((k != Key.NAME && k != Key.ARTIST && k != Key.GENRE) || stringTokenizer.countTokens() != 2)
                            return incorrectCommand();
                        keys.add(k);
                        token = stringTokenizer.nextToken();
                        arguments.add(token);
                        arguments.add(stringTokenizer.nextToken());
                        try {
                            id = Integer.parseInt(token);
                        } catch (NumberFormatException e) {
                            return incorrectCommand();
                        }
                        return Integer.toString(id).equals(token) ?
                                new InputDataHolder(false, keys.toArray(new Key[3]), id, arguments.toArray(new String[2]))
                                : incorrectCommand();
                    default:
                        return incorrectCommand();
                }
            }
            case REMOVE: {
                keys.add(k);
                token = stringTokenizer.nextToken();
                k = tokenCode(token);
                switch (k) {
                    case GENRE:
                    case TRACK:
                        keys.add(k);
                        token = stringTokenizer.nextToken();
                        if (stringTokenizer.hasMoreTokens())
                            return incorrectCommand();
                        int id;
                        try {
                            id = Integer.parseInt(token);
                        } catch (NumberFormatException e) {
                            return token.equals("all") || k == Key.GENRE ?
                                    new InputDataHolder(false, keys.toArray(new Key[2]), new String[]{token})
                                    : incorrectCommand();
                        }
                        return Integer.toString(id).equals(token) ?
                                new InputDataHolder(false, keys.toArray(new Key[2]), id, new String[]{token})
                                : k == Key.GENRE ?
                                new InputDataHolder(false, keys.toArray(new Key[2]), new String[]{token})
                                : incorrectCommand();
                    default:
                        return incorrectCommand();
                }
            }
            case EXIT: {
                return stringTokenizer.hasMoreTokens() ?
                        incorrectCommand()
                        : new InputDataHolder(false, new Key[]{Key.EXIT});
            }
            default:
                return incorrectCommand();
        }
    }

    /*private String getHelpInfo() {
        return "Список доступных команд:\n" +
                "view genre <all> - Вывод всех жанров на экран\n" +
                "view genre <id> - Вывод жанра по id\n" +
                "view track <all> - Вывод всех треков на экран\n" +
                "view track <id> - Вывод трека по id\n" +
                "add track <name artist genre_name> - Добавление трека с заданными параметрами\n" +
                "(Жанр с указанным именем должен быть добавлен заранее, иначе трек будет добавлен без жанра)\n" +
                "add track <name artist> - Добавление трека без жанра (без названия/исполнителя не допускается)\n" +
                "add genre <name> - Добавление жанра (Дубликаты не допускаются)\n" +
                "edit genre name <id new_name> Изменение названия жанра по id(отразится на всех его треках)\n" +
                "edit genre name <genre_name new_genre_name> - Изменение названия жанра по старому названию\n" +
                "edit track name <id new_name> - Изменение названия трека по id\n" +
                "edit track artist <id new_artist> - Изменение исполнителя трека\n" +
                "edit track genre <id new_genre_name> - Изменениие жанра трека(также на существующий)\n" +
                "remove genre <all> - Удаление всех жанров\n" +
                "remove genre <id> - Удаление жанра по id\n" +
                "remove track <all> - Удаление всех треков\n" +
                "remove track <id> - Удаление трека по id\n" +
                "exit - Выход из программы\n";
    }*/

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean exitPressed = false;
        while (!exitPressed) {
            String command = scanner.nextLine();
            parsing(command).print();
        }
    }

}
