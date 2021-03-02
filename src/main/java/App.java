
import domain.Book;
import java.util.List;
import dao.BookDao;
import domain.Search;
import io.IO;

public class App {

    private final BookDao dao;
    private final IO io;
    private final Search search;

    public App(BookDao dao, IO io, Search search) {
        this.dao = dao;
        this.io = io;
        this.search = search;
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
        List<Book> books = dao.getAll();

        List<Book> matching = search.findBooksByTitle(title, books);

        printBooks(matching);
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
    
    public void markAsRead() {
        io.print("Luetuksi merkittään lukuvinkin linkki: ");
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
