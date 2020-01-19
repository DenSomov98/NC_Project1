package server.controllers;

import worklib.parse.*;
import server.model.*;
import server.view.*;
import worklib.transfer.*;

import java.util.Scanner;

public class OldController {

    private Model model = new Model(new TrackList(), new GenreList());
    private View view = new View(model);

    private void performData(Request parsed) {
        Response result = model.validate(parsed);
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
            Request parsed = Parser.parsing(command);
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
                    case GET:
                        view.print(parsed);
                        break;
                    case FIND:
                        //view.printSearchResults(parsed);
                        break;
                    default:
                        performData(parsed);
                }
            }
        }
    }
}

