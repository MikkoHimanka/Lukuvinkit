package io;

import java.util.Scanner;

public class ConsoleIO implements IO {
    private Scanner scanner;

    public ConsoleIO() {
        this.scanner = new Scanner(System.in);
    }

    public void print(String s) {
        System.out.println(s);
    }

    public String getInput() {
        return this.scanner.nextLine();
    }
}
