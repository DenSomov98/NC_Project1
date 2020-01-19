package worklib.transfer;

import worklib.parse.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {

    private boolean isCorrect;
    private ArrayList<Key> keys;
    private ArrayList<String> arguments;

    private Request() {isCorrect=false;}
    public static Request makeIncorrect() {return new Request();}

    public Request(boolean isCorrect, ArrayList<Key> keys) {
        this.isCorrect = isCorrect;
        this.keys = (ArrayList<Key>) keys.clone();
        this.arguments = null;
    }

    public Request(boolean isCorrect, ArrayList<Key> keys, ArrayList<String> arguments) {
        this.isCorrect = isCorrect;
        this.keys = (ArrayList<Key>) keys.clone();
        this.arguments = (ArrayList<String>) arguments.clone();
    }

    public boolean isCorrect() { return isCorrect; }

    public Key[] getKeys() { return keys.toArray(new Key[0]); }

    public boolean hasArguments() { return arguments!=null;}

    public String[] getArguments() { return arguments.toArray(new String[0]);}

}