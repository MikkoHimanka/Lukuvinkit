package io;

import java.util.Scanner;

public class ConsoleIO implements IO {
    private final Scanner scanner;

    public ConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void print(String s) {
        System.out.println(s);
    }

    @Override
    public String getInput() {
        return this.scanner.nextLine();
    }
}
