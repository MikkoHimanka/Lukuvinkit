package io;
import java.util.ArrayList;

public class StubIO implements IO {
    private ArrayList<String> prints;

    public StubIO() {
        prints = new ArrayList<>();
    }

    public void print(String s) {
        prints.add(s);
    }

    public ArrayList<String> getPrints() {
        return prints;
    }
}
