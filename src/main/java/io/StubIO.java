package io;
import java.util.ArrayList;

public class StubIO implements IO {
    private ArrayList<String> prints;
    private ArrayList<String> inputs;

    public StubIO() {
        prints = new ArrayList<>();

        inputs = new ArrayList<>();
        inputs.add("www.linkki.com");
        inputs.add("Titteli");
    }

    public void print(String s) {
        prints.add(s);
    }

    public ArrayList<String> getPrints() {
        return prints;
    }

    public String getInput() {
        if (inputs.size() > 0) {
            return inputs.remove(0);
        } else {
            return "";
        }
    }
}
