package server;

import server.controllers.*;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {
        ArrayList<String> s = new ArrayList<>();
        s.add("a");
        s.add("b");
        String[] ss = s.toArray(new String[]{});
        ss[0] = "sss";
        System.out.println(ss[0]);
        System.out.println();
        new Controller().startSever();
    }
}
