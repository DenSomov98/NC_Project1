package Controller;

import Parse.Key;
import Model.*;
import Parse.Parser;
import View.View;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Controller {

    private Model model = new Model(new TrackList(), new GenreList());
    private View view = new View(model);

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
        view.printHelpMenu();
        while (true) {
            String command = scanner.nextLine();
            InputDataHolder parsed = Parser2test.parsing(command);
            if(!parsed.isCorrect())
                view.showInputError();
            else{
                Key first = parsed.getKeys()[0];
                switch (first) {
                    case EXIT:
                        return;
                    case HELP:
                        view.printHelpMenu();
                        break;
                    case VIEW:
                        view.print(parsed);
                        break;
                    case FIND:
                        view.printSearchResults(parsed);
                        break;
                    default:
                        performData(parsed);
                }
            }
        }
    }
}

