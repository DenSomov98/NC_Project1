package View;

import DataHolder.InputDataHolder;
import DataHolder.Key;
import Model.*;

import java.util.LinkedList;

public class View {
    private Model model;

    public View(Model model){
        this.model = model;
    }

    public void showInputError() {
        System.out.println("Введена некорректная команда, попробуйте снова.");
    }

    public void printHelpMenu(){
        System.out.println("Список доступных команд: ");
        System.out.println("view genre <all> - Вывод всех жанров на экран ");
        System.out.println("view genre <id> - Вывод жанра по id ");
        System.out.println("view track <all> - Вывод всех треков на экран ");
        System.out.println("view track <id> - Вывод трека по id ");
        System.out.println("add track <name artist genre_name> - Добавление трека с заданными параметрами ");
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
        System.out.println("remove track <all> - Удаление всех треков ");
        System.out.println("remove track <id> - Удаление трека по id ");
        System.out.println("help - Вывод списка доступных команд ");
        System.out.println("exit - Выход из программы ");
    }

    public void printTrack(InputDataHolder command){
        Track[] arrayTrack = model.viewTrack(command);//trackModel.getViewList();
        for(int i = 0; i < arrayTrack.length; i++){
            System.out.println(i + ". " + arrayTrack[i].toString());
        }
    }

    public void printGenre(InputDataHolder command){
        Genre[] arrayGenre = model.viewGenre(command);//genreModel.getViewList();
        for(int i = 0; i < arrayGenre.length; i++){
            System.out.println(i + ". " + arrayGenre[i].toString());
        }
    }

    public void printError(OutputDataHolder outputDataHolder){
        if(outputDataHolder.isIndexError()){
            System.out.println("Ошибка! Введен неверный индекс. ");
        }
        else if(outputDataHolder.isTrackWithoutGenreWarning()){
            System.out.println("Внимание! Добавлен трек без жанра. ");
        }
        else if(outputDataHolder.isGenreEqualsNameError()){
            System.out.println("Ошибка! Жанр с таким именем уже содержится в списке. ");
        }
    }

    public void printResult(OutputDataHolder data) {
        Key[] keys = data.getKeys();
        switch (keys[0]) {
            case ADD:
                if(keys[1]==Key.GENRE)
                    System.out.println("Жанр добавлен.");
                else
                    System.out.println("Трек добавлен. ");
            case REMOVE:
                if(keys[1]==Key.GENRE)
                    System.out.println("Жанр удален.");
                else
                    System.out.println("Трек удален.");
            case EDIT:
                if(keys[1]==Key.GENRE)
                    System.out.println("Название у выбранного жанра изменено. ");
                else if(keys[1]==Key.TRACK&&keys[2]==Key.NAME)
                    System.out.println("Название у выбранного трека изменено. ");
                else if(keys[1]==Key.TRACK&&keys[2]==Key.ARTIST)
                    System.out.println("Исполнитель у выбранного трека изменен. ");
                else
                    System.out.println("Жанр у выбранного трека изменен. ");
        }
    }

    public void show(InputDataHolder parsed) {
        Key[] keys = parsed.getKeys();
        switch (keys[1]){
            case GENRE:
                printGenre(parsed);
            case TRACK:
                printTrack(parsed);
        }
    }
}
