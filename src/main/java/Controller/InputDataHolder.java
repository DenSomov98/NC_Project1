package Controller;

import Parse.Key;

import java.util.ArrayList;

public class InputDataHolder {

    private boolean isCorrect;
    private ArrayList<Key> keys;
    private ArrayList<String> arguments;

    private InputDataHolder() {isCorrect=false;}
    public static InputDataHolder makeIncorrect() {return new InputDataHolder();}

    public InputDataHolder(boolean isCorrect, ArrayList<Key> keys) {
        this.isCorrect = isCorrect;
        this.keys = (ArrayList<Key>) keys.clone();
        this.arguments = null;
    }

    public InputDataHolder(boolean isCorrect, ArrayList<Key> keys, ArrayList<String> arguments) {
        this.isCorrect = isCorrect;
        this.keys = (ArrayList<Key>) keys.clone();
        this.arguments = (ArrayList<String>) arguments.clone();
    }

    public boolean isCorrect() { return isCorrect; }

    public Key[] getKeys() { return keys.toArray(new Key[0]); }

    public boolean hasArguments() { return arguments!=null;}

    public String[] getArguments() { return arguments.toArray(new String[0]);}

}