
import domain.Book;
import java.util.List;
import dao.BookDao;
import io.IO;

public class App {

    private final BookDao dao;
    private final IO io;

    public App(BookDao dao, IO io) {
        this.dao = dao;
        this.io = io;
    }
    
    public void listBook(List<Book> books) {
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
    
    public void listAllUnread() {
        listBook(dao.getUnread());
    }

    public void listAll() {
        listBook(dao.getAll());
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
    
    public void markAsRead() {
        io.print("Luetuksi merkittävän lukuvinkin linkki: ");
        String link = io.getInput();
        List<Book> bookList = dao.getUnread();
        for (Book book : bookList) {
            if (book.getLink().equals(link)) {
                dao.setRead(book);
                io.print("Lukuvinkki merkitty luetuksi");
                return;
            }
        }
        io.print("Lukuvinkin merkitseminen luetuksi ei onnistunut!");
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
        io.print("(S)ulje sovellus");
    }
}