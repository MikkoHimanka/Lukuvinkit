
import domain.Book;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import dao.BookDao;
import domain.BookList;
import domain.Search;
import io.IO;
import domain.URLVerifier;
import domain.URLVerificationResult;

public class App {

    private final BookDao dao;
    private final IO io;
    private final Search search;
    private final URLVerifier urlVerifier;

    public App(BookDao dao, IO io, Search search, URLVerifier urlVerifier) {
        this.dao = dao;
        this.io = io;
        this.search = search;
        this.urlVerifier = urlVerifier;
    }

    public void listAll() {
        List<Book> books = dao.getAll();
        BookList.printBooks(books, io);
    }
    
    public void listAllUnread() {
        List<Book> books = dao.getUnread();
        BookList.printBooks(books, io);
    }

    public void listByTitle(String title) {
        List<Book> books = dao.getAll();

        List<Book> matching = search.findBooksByTitle(title, books);

        BookList.printBooks(matching, io);
    }

    public void createBook() {
        io.print("Lisää linkki: (pakollinen)");
        String link = io.getInput();
        URLVerificationResult verificationResult = urlVerifier.verify(link);
        urlVerifier.printVerificationResult(verificationResult, io);
        if (verificationResult == URLVerificationResult.NETWORK_UNREACHABLE) {
            if (!confirmLinkAddition()) {
                io.print("Lukuvinkin lisäys ei onnistunut!");
                return;
            }
        }
        else if (verificationResult != URLVerificationResult.OK) {
            io.print("Lukuvinkin lisäys ei onnistunut!");
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

    private boolean confirmLinkAddition() {
        io.print("Haluatko varmasti lisätä linkin (k/E)?");
        String input = io.getInput().toLowerCase();
        switch (input) {
            case ("k"):
                return true;
            default:
                return false;
        }
    }

    private List<Book> searchValidBooks(List<Book> bookList) {
        while (bookList.size() > 5) {
            io.print("Löytyi " + bookList.size() + " lukuvinkkiä");
            bookList = BookList.narrowingSearch(bookList, io);
            if (bookList == null) {
                return null;
            }
        }
        return bookList;
    }
    
    private void findCorrectBook(List<Book> bookList, Command command) {
        if (bookList == null || bookList.isEmpty()) {
            io.print("Lukuvinkkejä ei löytynyt.");
            return;
        }
        while (true) {
            bookList = searchValidBooks(bookList);
            if (switchBookListSize(bookList, command) == null) {
                return;
            }
            command.actionQuestion();
            BookList.printBooksWithNumbers(bookList, io);
            io.print("\n(V)alitse");
            io.print("(T)arkenna hakuehtojasi");
            io.print("Takaisin (P)äävalikkoon");
            if (switchOptions(bookList, command) == null) {
                return;
            }
        }
    }
    
    private List<Book> switchBookListSize (List<Book> bookList, Command command) {
        if (bookList == null) {
            return null;
        }
        switch (bookList.size()) {
            case 0:
                io.print("Lukuvinkkejä ei löytynyt annetulla haulla.");
                return null;
            case 1:
                command.run(bookList.get(0));
                return null;
            default:
                return bookList;
        }
    }
    
    private List<Book> switchOptions (List<Book> bookList, Command command) {
        String input = io.getInput().toLowerCase();
        switch (input) {
            case ("v"):
                Book choosen = BookList.choose(bookList, io);
                if (command.run(choosen)) {
                    return null;
                }
                return bookList;
            case ("t"):
                return BookList.narrowingSearch(bookList, io);
            case ("p"):
                return null;
            default:
                io.print("Virhe: komento oli puutteellinen!");
                return bookList;
        }
    }
    
    public void removeBook() {
        Command command = new RemoveBook(io, dao);
        List<Book> bookList = dao.getAll();
        findCorrectBook(bookList, command);
    }

    public void markAsRead() {
        Command command = new MarkBook(io, dao);
        List<Book> bookList = dao.getUnread();
        findCorrectBook(bookList, command);
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
                case "p":
                    removeBook();
                    break;
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
        io.print("(P)oista lukuvinkki");
        io.print("(S)ulje sovellus");
    }
}
