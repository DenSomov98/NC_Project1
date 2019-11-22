package View;

import Model.*;

import java.util.LinkedList;

public class View {
    //private Tracks trackModel;
    //private GenreList genreModel;
    private Model model;

    public View(Model model){
        this.model = model;
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
        System.out.println("exit - Выход из программы ");
    }

    public void printListTrack(){
        LinkedList<Track> linkedListTrack = null;//trackModel.getViewList();
        for(int i = 0; i < linkedListTrack.size(); i++){
            System.out.println(i + ". " + linkedListTrack.get(i).toString());
        }
    }

    public void printListGenre(){
        LinkedList<Genre> linkedListGenre = null;//genreModel.getViewList();
        for(int i = 0; i < linkedListGenre.size(); i++){
            System.out.println(i + ". " + linkedListGenre.get(i).toString());
        }
    }

    public void printError(OutputDataHolder outputDataHolder){
        if(outputDataHolder.isIndexError()){
            System.out.println("Ошибка! Введен неверный индекс. ");
        }
        else if(outputDataHolder.isTrackWithoutGenreError()){
            System.out.println("Внимание! Добавлен трек без жанра. ");
        }
        else if(outputDataHolder.isGenreEqualsNameError()){
            System.out.println("Ошибка! Жанр с таким именем уже содержится в списке. ");
        }
    }
}
