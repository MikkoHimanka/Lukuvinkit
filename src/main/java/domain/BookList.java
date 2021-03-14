package domain;

import io.IO;
import org.javatuples.Triplet;

import dao.BookDao;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BookList {
    public static List<Book> filterBooks(List<Book> books, String title, String url) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title))
                .filter(b -> b.getLink().toLowerCase().contains(url))
                .collect(Collectors.toList());
    }

    public static Triplet<List<Book>, String, String> narrowingSearch(Triplet<List<Book>, String, String> books, IO io) {
        String url = "";
        String title = "";
        loop:
        while (true) {
            io.print("Tarkenna hakuehtojasi:");
            io.print("Tarkenna (L)inkki");
            io.print("Tarkenna (O)tsikko");
            io.print("Takaisin (P)aavalikkoon");
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
                    return new Triplet<>(null, title, url);
                default:
                    io.print("Virhe: komento oli puutteellinen!");
                    break;
            }
        }
        return new Triplet<>(filterBooks(books.getValue0(), title, url), title, url);
    }

    public static void printBooksWithNumbers(List<Book> books, IO io, BookDao dao) {
        if (books.isEmpty()) {
            io.print("Lukuvinkkeja ei loytynyt.");
        } else {
            io.print("Loytyi " + books.size() + " lukuvinkkia:");
            io.print("****");
            for (int i = 0; i < books.size(); ++i) {
                io.print("(" + (i+1) + ")");
                printBook(books.get(i), io, dao);
                io.print("****");
            }
        }
    }

    public static void printBooks(List<Book> books, IO io, BookDao dao) {
        if (books.isEmpty()) {
            io.print("Lukuvinkkeja ei loytynyt.");
        } else {
            io.print("Loytyi " + books.size() + " lukuvinkkia:");
            io.print("****");
            for (int i = 0; i < books.size(); ++i) {
                printBook(books.get(i), io, dao);
                io.print("****");
            }
        }
    }

    private static void printBook(Book book, IO io, BookDao dao) {
        io.print("Linkki: " + book.getLink());
        if (!(book.getTitle().isEmpty())) {
            io.print("Otsikko: " + book.getTitle());
        }
        if (book.getDescription() != null) {
            io.print("Kuvaus: " + book.getDescription());
        }
        List<String> tags = dao.findTagsByBook(book);
        if (tags != null && tags.size() > 0) {
            String tag_string = "";
            for (int i = 0; i < tags.size(); ++i) {
                if(i > 0) {
                    tag_string = tag_string + ", " + tags.get(i);
                }
                else {
                    tag_string = tags.get(i);
                }
            }
            io.print("Tagit: " + tag_string);
        }
        io.print("Luotu: " + book.getTime());
    }

    public static Book choose(List<Book> books, IO io) {
        io.print("Valitse lukuvinkin numero");
        String input = io.getInput();
        int number = -1;
        try {
            number += Integer.parseInt(input);
        } catch (Exception e) {
            io.print("Valinnan taytyy olla numero!");
            io.print("Paina (Enter)");
            io.getInput();
        }
        if (number < books.size() && number >= 0) {
            return books.get(number);
        }
        io.print("Valinta oli virheellinen!");
        return null;
    }
}
