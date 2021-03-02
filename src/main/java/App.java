
import domain.Book;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import dao.BookDao;
import io.IO;

public class App {

    private final BookDao dao;
    private final IO io;

    public App(BookDao dao, IO io) {
        this.dao = dao;
        this.io = io;
    }

    public void listAll() {
        List<Book> books = dao.getAll();
        printBooks(books);
    }
    
    public void listAllUnread() {
        List<Book> books = dao.getUnread();
        printBooks(books);
    }

    public void listByTitle(String title) {
        List<Book> books = dao.findByTitle(title);
        printBooks(books);
    }
    
    private void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            io.print("Lukuvinkkejä ei löytynyt.");
        } else {
            io.print("Löytyi " + books.size() + " lukuvinkkiä:");
            io.print("****");
            for (int i = 0; i < books.size(); ++i) {
                printBook(books.get(i));
                io.print("****");
            }
        }
    }

    private void printBook(Book book) {
        io.print("Linkki: " + book.getLink());
        if (!(book.getTitle().isEmpty())) {
            io.print("Otsikko: " + book.getTitle());
        }

    }

    public void createBook() {
        io.print("Lisää linkki: (pakollinen)");
        String link = io.getInput();
        if (link.isEmpty()) {
            io.print("Lukuvinkin lisäys ei onnistunut!");
            io.print("Linkki ei voi olla tyhjä.");
            return;
        }
        io.print("Lisää otsikko:");
        String title = io.getInput();

        Book result = dao.create(new Book(link, title));
        if (result != null) {
            io.print("Lukuvinkki lisätty onnistuneesti");
        } else {
            io.print("Lukuvinkin lisäys ei onnistunut!");
        }
    }

    private List<Book> filterBooks(List<Book> books, String title, String url) {
        return books.stream()
                .filter(b -> b.getTitle().contains(title))
                .filter(b -> b.getLink().contains(url))
                .collect(Collectors.toList());
    }

    private List<Book> narrowingSearch(List<Book> books) {
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
        books = filterBooks(books, title, url);
        return books;
    }

    public void markAsRead() {
        List<Book> bookList = dao.getUnread();
        String title = "";
        String url = "";

        if (bookList.isEmpty()) {
            io.print("Lukuvinkkejä ei löytynyt.");
            return;
        }

        while (bookList.size() > 5) {
            io.print("Löytyi " + bookList.size() + " lukuvinkkiä");

            bookList = narrowingSearch(bookList);
        }

        if (bookList.isEmpty()) {
            io.print("Lukuvinkkejä ei löytynyt annetulla haulla.");
            return;
        }

        io.print("Mikä lukuvinkki merkitään luetuksi?\n");
        printBooks(bookList);
        io.print("\n(V)alitse");
        io.print("(T)arkenna hakuehtojasi");
        io.print("Takaisin (P)äävalikkoon");
        io.print(""+bookList.size());
//        String link = io.getInput();
//        for (Book book : bookList) {
//            if (book.getLink().equals(link)) {
//                dao.setRead(book);
//                io.print("Lukuvinkki merkitty luetuksi");
//                return;
//            }
//        }
        io.print("Lukuvinkin merkitseminen luetuksi ei onnistunut!");
    }

    public void findByTitle() {
        io.print("Etsi lukuvinkkejä otsikon perusteella");
        io.print("Anna hakuparametri");
        String title = io.getInput();
        if (title.isEmpty()) {
            io.print("Haku ei onnistunut!");
            io.print("Hakuparametri ei voi olla tyhjä");
            return;
        }
        listByTitle(title);
    }

    public void switchContext() {
        io.print("Tervetuloa Lukuvinkit-sovellukseen!\n");
        loop:
        while (true) {
            switchMessage();
            String selection = io.getInput().toLowerCase();
            switch (selection) {
                case "l":
                    createBook();
                    break;
                case "n":
                    listAll();
                    break;
                case "m":
                    markAsRead();
                    break;
                case "li":
                    listAllUnread();
                    break;
                case "s":
                    io.print("Kiitos käynnistä, sovellus sulkeutuu.");
                    break loop;
                case "e":
                    findByTitle();
                    break;
                default:
                    io.print("Virhe: komento oli puutteellinen!");
                    break;
            }
        }

    }

    public void switchMessage() {
        io.print("Komennot:");
        io.print("(L)isää uusi lukuvinkki");
        io.print("(N)äytä tallennetut lukuvinkit");
        io.print("(M)erkitse lukuvinkki luetuksi");
        io.print("(Li)staa lukemattomat lukuvinkit");
        io.print("(E)tsi lukuvinkkejä");
        io.print("(S)ulje sovellus");
    }
}
