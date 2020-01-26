package server.view;

import worklib.transfer.*;
import worklib.parse.Key;
import worklib.entities.*;
import server.model.*;

public class View {
    private Model model;

    public View(Model model){
        this.model = model;
    }

    public void showInputError() {
        System.out.println("Введена некорректная команда, попробуйте снова.");
    }

    public void printHelpMenu(){
        System.out.println("Список доступных команд: (Скобки - опционально, пробелы не допускаются)");
        System.out.println("server.view genre \"all\" - Вывод всех жанров на экран ");
        System.out.println("server.view genre \"id\" - Вывод жанра по id ");
        System.out.println("server.view track \"all\" - Вывод всех треков на экран ");
        System.out.println("server.view track \"id\" - Вывод трека по id ");
        System.out.println("add track \"name\" \"artist\" \"genre name\" - Добавление трека с заданными параметрами ");
        System.out.println("(Жанр с указанным именем должен быть добавлен заранее, иначе трек будет добавлен без жанра) ");
        System.out.println("add track <name artist> - Добавление трека без жанра (без названия/исполнителя не допускается) ");
        System.out.println("add genre <name> - Добавление жанра (Дубликаты не допускаются) ");
        System.out.println("edit genre name <id new_name> - Изменение названия жанра по id(отразится на всех его треках) ");
        System.out.println("edit genre name <genre_name new_genre_name> - Изменение названия жанра по старому названию ");
        System.out.println("edit track name <id new_name> - Изменение названия трека по id ");
        System.out.println("edit track artist <id new_artist> - Изменение исполнителя трека ");
        System.out.println("edit track genre <id new_genre_name> - Изменениие жанра трека(также на существующий) ");
        System.out.println("remove genre <all> - Удаление всех жанров ");
        System.out.println("remove genre <id> - Удаление жанра по id ");
        System.out.println("remove genre <name> - Удаление жанра по имени ");
        System.out.println("remove track <all> - Удаление всех треков ");
        System.out.println("remove track <id> - Удаление трека по id ");
        System.out.println("find track \"name\" \"artist\" genre> - Поиск трека по параметрам(Укажите * вместо ненужных)");
        System.out.println("save \"filename\" - Сохранение в указанный XML - файл");
        System.out.println("save overwrite \"filename\" - Сохранение в указанный XML - файл(При совпадении имён - перезапись");
        System.out.println("load duplicate \"filename\" - Загрузка из указанного XML - файла(C дубликатами)");
        System.out.println("load overwrite \"filename\" - Загрузка из указанного XML - файла(Без дубликатов)");
        System.out.println("help - Вывод списка доступных команд ");
        System.out.println("exit - Выход из программы ");
    }

    public void printTrack(Request command){
        Track[] arrayTrack = model.viewTrack(command);
        if(arrayTrack.length == 0)
            System.out.println("Список пуст.\n");
        else
            for(int i = 0; i < arrayTrack.length; i++){
                System.out.println(i + ". " + arrayTrack[i].toString());
            }
    }

    public void printGenre(Request command){
        Genre[] arrayGenre = model.viewGenre(command);
        if(arrayGenre.length == 0)
            System.out.println("Список пуст.\n");
        else
            for(int i = 0; i < arrayGenre.length; i++){
                System.out.println(i + ". " + arrayGenre[i].toString());
            }
    }

    /*public void printSearchResults(Request command) {
        Track[] result = model.findTracks(command);
        if(result.length == 0)
            System.out.println("Ничего не найдено.\n");
        else
            for(Track track : result) {
                System.out.println(track.toString());
            }
    }*/

    public void printError(Response Response){
        if(Response.isIndexError()){
            System.out.println("Ошибка! Введен неверный индекс. ");
        }
        else if(Response.isTrackWithoutGenreWarning()){
            System.out.println("Внимание! Добавлен трек без жанра. ");
        }
        else if(Response.isEqualsNameError()){
            System.out.println("Ошибка! Жанр с таким именем уже содержится в списке. ");
        }
        else if(Response.isObjectNotFoundError()){
            System.out.println("Ошибка! Жанр с таким именем не существует. ");
        }
        else if(Response.isFileError()) {
            System.out.println("Ошибка при работе с файлом!");
        }
        else if(Response.isFileExistsError()) {
            System.out.println("Ошибка! Такой файл уже существует.");
        }
        else if(Response.isUnknownError()){
            System.out.println("Неизвестная ошибка!!!");
        }
    }

    public void printResult(Response data) {
        Key[] keys = data.getKeys();
        switch (keys[0]) {
            case ADD:
                if(keys[1]==Key.GENRE)
                    System.out.println("Жанр добавлен.");
                else
                    System.out.println("Трек добавлен. ");
                break;
            case REMOVE:
                if(keys[1]==Key.GENRE)
                    System.out.println("Жанр(ы) удален(ы).");
                else
                    System.out.println("Трек(и) удален(ы).");
                break;
            case EDIT:
                if(keys[1]==Key.GENRE)
                    System.out.println("Название у выбранного жанра изменено. ");
                else if(keys[1]==Key.TRACK&&keys[2]==Key.NAME)
                    System.out.println("Название у выбранного трека изменено. ");
                else if(keys[1]==Key.TRACK&&keys[2]==Key.ARTIST)
                    System.out.println("Исполнитель у выбранного трека изменен. ");
                else
                    System.out.println("Жанр у выбранного трека изменен. ");
                break;
            case SAVE:
                System.out.println("Данные сохранены.");
                break;
            case LOAD:
                if(data.isFileIsCorruptedError())
                    System.out.println("Ошибка при чтении. Возможно, файл повреждён.");
                 else if(!data.hasWarnings())
                    System.out.println("Данные загружены");
                break;
        }
        printWarnings(data);
    }

    private void printWarnings(Response data) {
        if(data.isTrackWithoutGenreWarning())
            System.out.println("Внимание, трек добавлен без жанра!");
        if (data.isFileIsEmptyWarning())
            System.out.println("Внимание, файл пуст!");
    }

    public void print(Request parsed) {
        Key[] keys = parsed.getKeys();
        switch (keys[1]){
            case GENRE:
                printGenre(parsed);
                break;
            case TRACK:
                printTrack(parsed);
                break;
        }
    }
}
