
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
            io.print("Lukuvinkkej� ei l�ytynyt.");
        } else {
            io.print("L�ytyi " + books.size() + " lukuvinkki�:");
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
        io.print("Lis�� linkki: (pakollinen)");
        String link = io.getInput();
        if (link.isEmpty()) {
            io.print("Lukuvinkin lisäys ei onnistunut!");
            io.print("Linkki ei voi olla tyhjä.");
            return;
        }
        io.print("Lis�� otsikko:");
        String title = io.getInput();

        Book result = dao.create(new Book(link, title));
        if (result != null) {
            io.print("Lukuvinkki lis�tty onnistuneesti");
        } else {
            io.print("Lukuvinkin lis�ys ei onnistunut!");
        }
    }
    
    public void markAsRead() {
        io.print("Luetuksi merkitt��n lukuvinkin linkki: ");
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
        io.print("Etsi lukuvinkkej� otsikon perusteella");
        io.print("Anna hakuparametri");
        String title = io.getInput();
        if (title.isEmpty()) {
            io.print("Haku ei onnistunut!");
            io.print("Hakuparametri ei voi olla tyhj�");
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
                    io.print("Kiitos k�ynnist�, sovellus sulkeutuu.");
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
        io.print("(L)is�� uusi lukuvinkki");
        io.print("(N)�yt� tallennetut lukuvinkit");
        io.print("(M)erkitse lukuvinkki luetuksi");
        io.print("(Li)staa lukemattomat lukuvinkit");
        io.print("(E)tsi lukuvinkkej�");
        io.print("(S)ulje sovellus");
    }
}