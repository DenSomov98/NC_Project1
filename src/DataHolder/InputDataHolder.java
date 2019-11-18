package DataHolder;

public class InputDataHolder {

    private boolean isIncorrect;
    private Key[] keys;
    private int id = -1;
    private String[] arguments;

    public InputDataHolder(boolean isIncorrect, Key[] keys) {
        this.isIncorrect = isIncorrect;
        this.keys = keys;
        this.arguments = null;
    }

    public InputDataHolder(boolean isIncorrect, Key[] keys, String[] arguments) {
        this.isIncorrect = isIncorrect;
        this.keys = keys;
        this.arguments = arguments;
    }

    public InputDataHolder(boolean isIncorrect, Key[] keys, int id, String[] arguments) {
        this.isIncorrect = isIncorrect;
        this.keys = keys;
        this.id = id;
        this.arguments = arguments;
    }

    public boolean isIncorrect() { return isIncorrect; }

    public Key[] getKeys() { return keys; }

    public boolean hasID() { return id>0; }

    public int getID() { return id; }

    public boolean hasArguments() { return arguments!=null;}

    public String[] getArguments() { return arguments;}

    public void print() {
        System.out.println("incorrect = " + isIncorrect);
        System.out.println("ID: " + hasID() + " " + id);
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