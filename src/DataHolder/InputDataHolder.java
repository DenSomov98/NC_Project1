package DataHolder;

import java.util.ArrayList;

public class InputDataHolder {

    private boolean isCorrect;
    private ArrayList<Key> keys;
    //private int id = -1;/////////////////////////////////////////
    private ArrayList<String> arguments;

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

    /*public InputDataHolder(boolean isCorrect, ArrayList<Key> keys, int id, ArrayList<String> arguments) {
        this.isCorrect = isCorrect;
        this.keys = keys;
        this.id = id;
        this.arguments = arguments;
    }*/

    public boolean isCorrect() { return isCorrect; }

    public Key[] getKeys() { return keys.toArray(new Key[0]); }

    /*public boolean hasID() { return id>0; }

    public int getID() { return id; }*/

    public boolean hasArguments() { return arguments!=null;}

    public String[] getArguments() { return arguments.toArray(new String[0]);}

    public void print() {
        System.out.println("incorrect = " + isCorrect);
        //System.out.println("ID: " + hasID() + " " + id);
        if(keys!=null)
            for(Key key: keys) {
                System.out.print(key + " ");
            }
        if(arguments!=null)
            for(String arg: arguments) {
                System.out.print(arg + " ");
            }
        System.out.println();
    }
}