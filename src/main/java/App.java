import domain.Book;

import java.util.Arrays;
import java.util.List;

import dao.BookDao;
import domain.BookList;
import domain.Search;
import io.IO;
import domain.URLVerifier;
import domain.URLVerificationResult;
import isbn.BookApi;
import kotlin.Triple;
import org.javatuples.Triplet;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {

    private final BookDao dao;
    private final IO io;
    private final Search search;
    private final URLVerifier urlVerifier;
    private final BookApi bookApi;

    public App(BookDao dao, IO io, Search search, URLVerifier urlVerifier, BookApi bookApi) {
        this.dao = dao;
        this.io = io;
        this.search = search;
        this.urlVerifier = urlVerifier;
        this.bookApi = bookApi;
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

    public void listByTag(String tag) {
        List<Book> result = dao.findByTag(tag);
        BookList.printBooks(result, io);
    }

    public void createBook() {
        io.print("Lisaa linkki: (pakollinen)");
        String link = io.getInput();
        URLVerificationResult verificationResult = urlVerifier.verify(link);
        URLVerifier.printVerificationResult(verificationResult, io);
        if (verificationResult == URLVerificationResult.NETWORK_UNREACHABLE) {
            if (!confirmLinkAddition()) {
                io.print("Lukuvinkin lisays ei onnistunut!");
                return;
            }
        } else if (verificationResult != URLVerificationResult.OK) {
            io.print("Lukuvinkin lisays ei onnistunut!");
            return;
        }
        io.print("Lisaa otsikko:");
        String title = io.getInput();
        io.print("Lisaa kuvaus:");
        String description = io.getInput();
        io.print("Lisaa tagit pilkulla eroteltuna:");
        String tagString = io.getInput();
        String[] tags = tagString.split(",");

        Book result;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy 'kello' HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(formatter);
        

        if (description.equals("")) {
            result = dao.create(new Book(link, title, time));
        } else {
            result = dao.create(new Book(link, title, description, time));
        }

        if (result != null) {
            io.print("Lukuvinkki lisatty onnistuneesti");
            if (tagString.length() != 0) {
                boolean res = dao.addTags(result, Arrays.asList(tags));
                if (res) {
                    io.print("Tagien lisays onnistui");
                } else {
                    io.print("Tagien lisays epaonnistui");
                }
            }
        } else {
            io.print("Lukuvinkin lisays ei onnistunut!");
        }
    }

    private boolean confirmLinkAddition() {
        io.print("Haluatko varmasti lisata linkin (k/E)?");
        String input = io.getInput().toLowerCase();
        return input.equals("k");
    }

    private Triplet<List<Book>, String, String> searchValidBooks(Triplet<List<Book>, String, String> bookListTriplet) {
        while (bookListTriplet.getValue0().size() > 5) {
            io.print("Loytyi " + bookListTriplet.getValue0().size() + " lukuvinkkia");
            bookListTriplet = BookList.narrowingSearch(bookListTriplet, io);
            if (bookListTriplet.getValue0() == null) {
                return null;
            }
        }
        return bookListTriplet;
    }

    private void findCorrectBook(Triplet<List<Book>, String, String> bookListTriplet, Command command) {
        if (bookListTriplet.getValue0() == null || bookListTriplet.getValue0().isEmpty()) {
            io.print("Lukuvinkkeja ei loytynyt.");
            return;
        }
        while (true) {
            bookListTriplet = searchValidBooks(bookListTriplet);
            bookListTriplet = switchBookListSize(bookListTriplet, command);
            if (bookListTriplet == null) {
                return;
            }
            command.actionQuestion();
            BookList.printBooksWithNumbers(bookListTriplet.getValue0(), io);
            io.print("\n(V)alitse");
            io.print("(T)arkenna hakuehtojasi");
            io.print("Takaisin (P)aavalikkoon");
            bookListTriplet = switchOptions(bookListTriplet, command);
            if (bookListTriplet == null) {
                return;
            }
        }
    }

    private Triplet<List<Book>, String, String> switchBookListSize(Triplet<List<Book>, String, String> bookListTriplet, Command command) {
        if (bookListTriplet == null || bookListTriplet.getValue0() == null) {
            return null;
        }
        switch (bookListTriplet.getValue0().size()) {
            case 0:
                io.print("Lukuvinkkeja ei loytynyt annetulla haulla.");
                io.print("Tarkoititko jotain naista?");

                String keyword = bookListTriplet.getValue1().equals("") ?
                        bookListTriplet.getValue2() :
                        bookListTriplet.getValue1();

                return new Triplet<>(search.getBooksByRank(keyword, dao.getAll()), "", "");
            case 1:
                command.run(bookListTriplet.getValue0().get(0));
                return null;
            default:
                return bookListTriplet;
        }
    }

    private Triplet<List<Book>, String, String> switchOptions(Triplet<List<Book>, String, String> bookListTriplet, Command command) {
        String input = io.getInput().toLowerCase();
        switch (input) {
            case ("v"):
                Book chosen = BookList.choose(bookListTriplet.getValue0(), io);
                if (command.run(chosen)) {
                    return null;
                }
                return bookListTriplet;
            case ("t"):
                return BookList.narrowingSearch(bookListTriplet, io);
            case ("p"):
                return null;
            default:
                io.print("Virhe: komento oli puutteellinen!");
                return bookListTriplet;
        }
    }

    public void editBook() {
        
        Command command = new EditBook(io, dao, urlVerifier);
        List<Book> bookList = dao.getAll();
        Triplet<List<Book>, String, String> bookListTriplet = new Triplet<>(bookList, "", "");
        findCorrectBook(bookListTriplet, command);
    }

    public void removeBook() {
        Command command = new RemoveBook(io, dao);
        List<Book> bookList = dao.getAll();
        Triplet<List<Book>, String, String> bookListTriplet = new Triplet<>(bookList, "", "");
        findCorrectBook(bookListTriplet, command);
    }

    public void markAsRead() {
        Command command = new MarkBook(io, dao);
        List<Book> bookList = dao.getUnread();
        Triplet<List<Book>, String, String> bookListTriplet = new Triplet<>(bookList, "", "");
        findCorrectBook(bookListTriplet, command);
    }

    public void searchBooks() {
        io.print("Valitse hakutyyppi:");
        io.print("(O)tsikon perusteella.");
        io.print("(T)agien perusteella.");
        io.print("Takaisin (P)aavalikkoon");
        String selection = io.getInput().toLowerCase();
        switch (selection) {
            case "o":
                findByTitle();
                break;
            case "t":
                findByTag();
                break;
            case "p":
                break;
            default:
                io.print("Virhe: komento oli puutteellinen!");
        }
    }

    public void findByTitle() {
        io.print("Etsi lukuvinkkeja otsikon perusteella.");
        String title = askSearchParameter();
        if(title == null) {
            return;
        }
        listByTitle(title);
    }

    public void findByTag() {
        io.print("Etsi lukuvinkkeja tagien perusteella.");
        String tag = askSearchParameter();
        if(tag == null) {
            return;
        }
        listByTag(tag);
    }

    private String askSearchParameter() {
        io.print("Anna hakuparametri:");
        String input = io.getInput();
        if (input.isEmpty()) {
            io.print("Haku ei onnistunut!");
            io.print("Hakuparametri ei voi olla tyhja");
            return null;
        }
        return input;
    }

    public void createFromIsbn() {
        if (!bookApi.hasApiKey()) {
            io.print("Et ole asettanut ymparistomuuttujaa!");
            return;
        }
        io.print("Anna kirjan isbn-tunnus:");
        String isbn = io.getInput();
        Book book = bookApi.getBook(isbn);
        if (book == null) {
            io.print("Kirjan hakeminen epaonnistui!");
            io.print("Tarkista etta isbn-tunnus on annettu oikein");
        } else {
            io.print("Loytyi kirja tiedoilla:");
            io.print("Linkki: " + book.getLink());
            io.print("Otsikko: " + book.getTitle());
            io.print("Haluatko lisata kirjan (k/E)?");
            String choice = io.getInput().toLowerCase();
            if (choice.equals("k")) {
                book = dao.create(book);
                if (book != null) {
                    io.print("Kirja lisattiin onnistuneesti!");
                } else {
                    io.print("Kirjan lisaaminen tietokantaan epaonnistui!");
                }
            }
        }
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
                case "mu":
                    editBook();
                    break;
                case "li":
                    listAllUnread();
                    break;
                case "s":
                    io.print("Kiitos kaynnista, sovellus sulkeutuu.");
                    break loop;
                case "p":
                    removeBook();
                    break;
                case "e":
                    searchBooks();
                    break;
                case "i":
                    createFromIsbn();
                    break;
                default:
                    io.print("Virhe: komento oli puutteellinen!");
                    break;
            }
        }
    }

    public void switchMessage() {
        io.print("Komennot:");
        io.print("(L)isaa uusi lukuvinkki");
        io.print("(I)sbn-tunnuksen avulla lisaaminen");
        io.print("(N)ayta tallennetut lukuvinkit");
        io.print("(M)erkitse lukuvinkki luetuksi");
        io.print("(Li)staa lukemattomat lukuvinkit");
        io.print("(E)tsi lukuvinkkeja");
        io.print("(P)oista lukuvinkki");
        io.print("(Mu)okkaa lukuvinkkia");
        io.print("(S)ulje sovellus");
    }
}
