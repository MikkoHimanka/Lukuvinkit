package io;
import java.util.ArrayList;
import java.util.List;

public class StubIO implements IO {
    private final ArrayList<String> prints;
    private final ArrayList<String> inputs;

    public StubIO() {
        prints = new ArrayList<>();
        inputs = new ArrayList<>();
    }

    public void addInput(String input) {
        this.inputs.add(input);
    }

    public void addInput(List<String> inputs) {
        this.inputs.addAll(inputs);
    }

    @Override
    public void print(String s) {
        prints.add(s);
    }

    public ArrayList<String> getPrints() {
        return prints;
    }

    @Override
    public String getInput() {
        if (inputs.size() > 0) {
            return inputs.remove(0);
        } else {
            return "";
        }
    }
}
