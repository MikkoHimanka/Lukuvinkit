package domain;

import io.IO;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BookList {
    public static List<Book> filterBooks(List<Book> books, String title, String url) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title))
                .filter(b -> b.getLink().toLowerCase().contains(url))
                .collect(Collectors.toList());
    }

    public static List<Book> narrowingSearch(List<Book> books, IO io) {
        String url = "";
        String title = "";
        loop:
        while (true) {
            io.print("Tarkenna hakuehtojasi:");
            io.print("Tarkenna (L)inkki");
            io.print("Tarkenna (O)tsikko");
            io.print("Takaisin (P)äävalikkoon");
            String input = io.getInput().toLowerCase();
            switch (input) {
                case ("l"):
                    io.print("Anna hakuparametri:");
                    url = io.getInput().toLowerCase();
                    break loop;
                case ("o"):
                    io.print("Anna hakuparametri:");
                    title = io.getInput().toLowerCase();
                    break loop;
                case ("p"):
                    return null;
                default:
                    io.print("Virhe: komento oli puutteellinen!");
                    break;
            }
        }
        return filterBooks(books, title, url);
    }

    public static void printBooksWithNumbers(List<Book> books, IO io) {
        if (books.isEmpty()) {
            io.print("Lukuvinkkejä ei löytynyt.");
        } else {
            io.print("Löytyi " + books.size() + " lukuvinkkiä:");
            io.print("****");
            for (int i = 0; i < books.size(); ++i) {
                io.print("(" + (i+1) + ")");
                printBook(books.get(i), io);
                io.print("****");
            }
        }
    }

    public static void printBooks(List<Book> books, IO io) {
        if (books.isEmpty()) {
            io.print("Lukuvinkkejä ei löytynyt.");
        } else {
            io.print("Löytyi " + books.size() + " lukuvinkkiä:");
            io.print("****");
            for (int i = 0; i < books.size(); ++i) {
                printBook(books.get(i), io);
                io.print("****");
            }
        }
    }

    private static void printBook(Book book, IO io) {
        io.print("Linkki: " + book.getLink());
        if (!(book.getTitle().isEmpty())) {
            io.print("Otsikko: " + book.getTitle());
        }
    }

    public static Book choose(List<Book> books, IO io) {
        io.print("Valitse lukuvinkin numero");
        String input = io.getInput();
        int number = -1;
        try {
            number += Integer.parseInt(input);
        } catch (Exception e) {
            io.print("Valinnan täytyy olla numero!");
            io.print("Paina (Enter)");
            io.getInput();
        }
        if (number <= books.size() && number > 0) {
            return books.get(number);
        }
        io.print("Valinta oli virheellinen!");
        return null;
    }
}
