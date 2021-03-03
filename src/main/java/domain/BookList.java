package domain;

import io.IO;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BookList {
    public static List<Book> filterBooks(List<Book> books, String url, String title) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title))
                .filter(b -> b.getLink().toLowerCase().contains(url))
                .collect(Collectors.toList());
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
}
