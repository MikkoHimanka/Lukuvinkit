import dao.SqliteBookDao;
import domain.Book;
import domain.Search;
import domain.URLVerifier;
import io.StubIO;
import io.StubNetworkConnection;
import isbn.BookApi;
import isbn.StubApi;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

import java.net.URL;
import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class AppTest {

    private SqliteBookDao sqliteDb;
    private StubIO io;
    private Search search;
    private StubNetworkConnection connection;
    private URLVerifier verifier;
    private StubApi bookApi;

    @Before
    public void initDb() {
        sqliteDb = new SqliteBookDao("test.db");
    }

    @Before
    public void init() {
        io = new StubIO();
        search = new Search(3.0);
        bookApi = new StubApi();
    }

    @Before
    public void initVerifier() {
        List<URL> validUrls = new ArrayList<URL>();
        try {
            validUrls.add(new URL("http://www.google.com"));
        } catch(MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        connection = new StubNetworkConnection(true, validUrls);
        verifier = new URLVerifier(connection);
    }

    @Test
    public void testListAllEmpty() {
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.listAll();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Lukuvinkkeja ei loytynyt.");
        assertEquals(out.size(), 1);
    }
    
    @Test
    public void testListAllSimple() {
        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("www.google.com", "haku"));
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.listAll();
        List<String> out = io.getPrints();

        assertEquals(out.get(0), "Loytyi 2 lukuvinkkia:");
        assertEquals(out.get(1), "****");
        assertEquals(out.get(2), "Linkki: link");
        assertEquals(out.get(3), "Otsikko: title");
        assertEquals(out.get(4), "****");
        assertEquals(out.get(5), "Linkki: www.google.com");
        assertEquals(out.get(6), "Otsikko: haku");
        assertEquals(out.get(7), "****");
    assertEquals(out.size(), 8);
    }
    
    @Test
    public void testListAllUnread() {
        sqliteDb.create(new Book("link", "title", 1));
        sqliteDb.create(new Book("www.google.com", "haku", 2));
        sqliteDb.setRead(new Book("link", "title", 1));
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.listAllUnread();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Loytyi 1 lukuvinkkia:");
        assertEquals(out.get(1), "****");
        assertEquals(out.get(2), "Linkki: www.google.com");
        assertEquals(out.get(3), "Otsikko: haku");
        assertEquals(out.get(4), "****");
    assertEquals(out.size(), 5);
    }
    
    @Test
    public void testSwichContextWelcomeAndExit() {
        io.addInput("s");
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 12);
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(1), "Komennot:");
        assertEquals(out.get(2), "(L)isaa uusi lukuvinkki");
        assertEquals(out.get(3), "(I)sbn-tunnuksen avulla lisaaminen");
        assertEquals(out.get(4), "(N)ayta tallennetut lukuvinkit");
        assertEquals(out.get(5), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(6), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(7), "(E)tsi lukuvinkkeja");
        assertEquals(out.get(8), "(P)oista lukuvinkki");
        assertEquals(out.get(9), "(Mu)okkaa lukuvinkkia");
        assertEquals(out.get(10), "(S)ulje sovellus");
        assertEquals(out.get(11), "Kiitos kaynnista, sovellus sulkeutuu.");
    }

    @Test
    public void testSwichContextListAllAndExit() {
        io.addInput("n");
        io.addInput("s");
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 23);
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(1), "Komennot:");
        assertEquals(out.get(2), "(L)isaa uusi lukuvinkki");
        assertEquals(out.get(3), "(I)sbn-tunnuksen avulla lisaaminen");
        assertEquals(out.get(4), "(N)ayta tallennetut lukuvinkit");
        assertEquals(out.get(5), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(6), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(7), "(E)tsi lukuvinkkeja");
        assertEquals(out.get(8), "(P)oista lukuvinkki");
        assertEquals(out.get(9), "(Mu)okkaa lukuvinkkia");
        assertEquals(out.get(10), "(S)ulje sovellus");
        assertEquals(out.get(11), "Lukuvinkkeja ei loytynyt.");
        assertEquals(out.get(12), "Komennot:");
        assertEquals(out.get(13), "(L)isaa uusi lukuvinkki");
        assertEquals(out.get(14), "(I)sbn-tunnuksen avulla lisaaminen");
        assertEquals(out.get(15), "(N)ayta tallennetut lukuvinkit");
        assertEquals(out.get(16), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(17), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(18), "(E)tsi lukuvinkkeja");
        assertEquals(out.get(19), "(P)oista lukuvinkki");
        assertEquals(out.get(20), "(Mu)okkaa lukuvinkkia");
        assertEquals(out.get(21), "(S)ulje sovellus");
        assertEquals(out.get(22), "Kiitos kaynnista, sovellus sulkeutuu.");

    }

    @Test
    public void testSwichContextFailedCommand() {
        io.addInput("lol");
        io.addInput("s");
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(11), "Virhe: komento oli puutteellinen!");
    }

    @Test
    public void testFindByTitleFindsBooksWithKeyword() {
        sqliteDb.create(new Book("link", "title"));
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        io.addInput("itl");
        app.findByTitle();
        io.getInput();
        io.getInput();
        List<String> out = io.getPrints();

        assertEquals(out.get(5), "Otsikko: title");
    }

    @Test
    public void testFindByTitleFindsBooksWithoutTitle() {
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        io.addInput("");
        app.findByTitle();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 4);
        assertEquals(out.get(2), "Haku ei onnistunut!");
        assertEquals(out.get(3), "Hakuparametri ei voi olla tyhja");
    }

    @Test
    public void testFindByTitleDoesntFindBooksWithBadKeyword() {
        sqliteDb.create(new Book("link", "title"));
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        io.addInput("lepakko");
        app.findByTitle();
        io.getInput();
        io.getInput();
        List<String> out = io.getPrints();

        assertEquals(out.get(2), "Lukuvinkkeja ei loytynyt.");
    }

    @Test
    public void testCreateInvalidLink() {
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        connection.setConnectionStatus(false);
        io.addInput("");
        app.createBook();
        List<String> out = io.getPrints();

        assertEquals("Annetun linkin formaatti ei kelpaa.", out.get(1));
        assertEquals("Lukuvinkin lisays ei onnistunut!", out.get(2));
    }

    @Test
    public void testCreateBookWithoutInternet() {
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        connection.setConnectionStatus(false);
        io.addInput("https://is.fi");
        io.addInput("k");
        io.addInput("iltasanomat urheilu");
        io.addInput("");
        io.addInput("tag");
        app.createBook();
        List<String> out = io.getPrints();

        assertEquals("Internet-yhteyden luonti epaonnistui: Linkin oikeellisuus epavarmaa.", out.get(1));
        assertEquals("Haluatko varmasti lisata linkin (k/E)?", out.get(2));
        assertEquals("Lukuvinkki lisatty onnistuneesti", out.get(6));
        assertEquals("Tagien lisays onnistui", out.get(7));
        assertEquals(1, sqliteDb.findByTitle("iltasanomat urheilu").size());
    }

    @Test
    public void testCreateBookWithoutInternetCancel() {
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        connection.setConnectionStatus(false);
        io.addInput("https://is.fi");
        io.addInput("n");
        app.createBook();
        List<String> out = io.getPrints();

        assertEquals("Internet-yhteyden luonti epaonnistui: Linkin oikeellisuus epavarmaa.", out.get(1));
        assertEquals("Haluatko varmasti lisata linkin (k/E)?", out.get(2));
        assertEquals("Lukuvinkin lisays ei onnistunut!", out.get(3));
        assertEquals(0, sqliteDb.findByTitle("iltasanomat urheilu").size());
    }

    @Test
    public void testCreateFromIsbnValid() {
        Book book = new Book("x", "y");
        bookApi.addBook(book);
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        io.addInput("9780134092669");
        io.addInput("k");
        app.createFromIsbn();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 6);
        assertEquals(out.get(0), "Anna kirjan isbn-tunnus:");
        assertEquals(out.get(1), "Loytyi kirja tiedoilla:");
        assertEquals(out.get(2), "Linkki: x");
        assertEquals(out.get(3), "Otsikko: y");
    }

    @Test
    public void testCreateFromIsbnWithoutApiKey() {
        bookApi.setKey(false);
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.createFromIsbn();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 1);
        assertEquals(out.get(0), "Et ole asettanut ymparistomuuttujaa!");
    }

    @Test
    public void testCreateFromIsbnWithoutBook() {
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        io.addInput("x");
        app.createFromIsbn();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 3);
        assertEquals(out.get(0), "Anna kirjan isbn-tunnus:");
        assertEquals(out.get(1), "Kirjan hakeminen epaonnistui!");
        assertEquals(out.get(2), "Tarkista etta isbn-tunnus on annettu oikein");
    }

    @Test
    public void testRemoveBook() {
        sqliteDb.create(new Book("x"));
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        io.addInput("k");
        app.removeBook();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 5);
        assertEquals(out.get(4), "Lukuvinkki on poistettu!");
    }

    @Test
    public void testFindbook() {
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.removeBook();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 1);
        assertEquals(out.get(0), "Lukuvinkkeja ei loytynyt.");
    }

    @Test
    public void testEditBook() {
        sqliteDb.create(new Book("x", "y"));
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        io.addInput("o");
        io.addInput("z");
        app.editBook();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 7);
        assertEquals(out.get(0), "Valitse muokattava ominaisuus:");
        assertEquals(out.get(1), "Muokkaa (L)inkkia");
        assertEquals(out.get(2), "Muokkaa (O)tsikkoa");
        assertEquals(out.get(3), "Muokkaa (K)uvausta");
        assertEquals(out.get(4), "Takaisin (V)alintaan");
        assertEquals(out.get(5), "Anna uusi otsikko (aiempi y):");
        assertEquals(out.get(6), "Lukuvinkin tiedot paivitetty onnistuneesti.");
    }

    @Test
    public void testSwitchContext() {
        io.addInput("mu");
        io.addInput("p");
        io.addInput("s");
        App app = new App(sqliteDb, io, search, verifier, bookApi);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 34);
        assertEquals(out.get(11), "Lukuvinkkeja ei loytynyt.");
    }

    @After
    public void deleteFile() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = conn.createStatement();
        s.execute("DROP TABLE Books;");
        conn.close();
        File db = new File("test.db");
        db.delete();
    }
}


