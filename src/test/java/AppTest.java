import dao.SqliteBookDao;
import domain.Book;
import domain.Search;
import domain.URLVerifier;
import io.StubIO;
import io.StubNetworkConnection;
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

    @Before
    public void initDb() throws SQLException {
        sqliteDb = new SqliteBookDao("test.db");
    }

    @Before
    public void initIO() {
        io = new StubIO();
    }

    @Before
    public void initSearch() {
        search = new Search(3.0);
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
        App app = new App(sqliteDb, io, search, verifier);
        app.listAll();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Lukuvinkkeja ei loytynyt.");
    assertEquals(out.size(), 1);
    }
    
    @Test
    public void testListAllSimple() {
        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("www.google.com", "haku"));
        App app = new App(sqliteDb, io, search, verifier);
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
        App app = new App(sqliteDb, io, search, verifier);
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
        App app = new App(sqliteDb, io, search, verifier);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(1), "Komennot:");
        assertEquals(out.get(2), "(L)isaa uusi lukuvinkki");
        assertEquals(out.get(3), "(N)ayta tallennetut lukuvinkit");
        assertEquals(out.get(4), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(5), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(6), "(E)tsi lukuvinkkeja");
        assertEquals(out.get(7), "(P)oista lukuvinkki");
        assertEquals(out.get(8), "(Mu)okkaa lukuvinkkia");
        assertEquals(out.get(9), "(S)ulje sovellus");      
        assertEquals(out.get(10), "Kiitos kaynnista, sovellus sulkeutuu.");
        assertEquals(out.size(), 11);
    }

    @Test
    public void testSwichContextListAllAndExit() {
        io.addInput("n");
        io.addInput("s");
        App app = new App(sqliteDb, io, search, verifier);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(1), "Komennot:");
        assertEquals(out.get(2), "(L)isaa uusi lukuvinkki");
        assertEquals(out.get(3), "(N)ayta tallennetut lukuvinkit");
        assertEquals(out.get(4), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(5), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(6), "(E)tsi lukuvinkkeja");
        assertEquals(out.get(7), "(P)oista lukuvinkki");
        assertEquals(out.get(8), "(Mu)okkaa lukuvinkkia");
        assertEquals(out.get(9), "(S)ulje sovellus");
        assertEquals(out.get(10), "Lukuvinkkeja ei loytynyt.");
        assertEquals(out.get(11), "Komennot:");
        assertEquals(out.get(12), "(L)isaa uusi lukuvinkki");
        assertEquals(out.get(13), "(N)ayta tallennetut lukuvinkit");
        assertEquals(out.get(14), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(15), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(16), "(E)tsi lukuvinkkeja");
        assertEquals(out.get(17), "(P)oista lukuvinkki");
        assertEquals(out.get(18), "(Mu)okkaa lukuvinkkia");
        assertEquals(out.get(19), "(S)ulje sovellus");
        assertEquals(out.get(20), "Kiitos kaynnista, sovellus sulkeutuu.");
        assertEquals(out.size(), 21);
    }

    @Test
    public void testSwichContextFailedCommand() {
        io.addInput("lol");
        io.addInput("s");
        App app = new App(sqliteDb, io, search, verifier);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(10), "Virhe: komento oli puutteellinen!");
    }

    @Test
    public void testFindByTitleFindsBooksWithKeyword() {
        sqliteDb.create(new Book("link", "title"));
        App app = new App(sqliteDb, io, search, verifier);
        io.addInput("itl");
        app.findByTitle();
        io.getInput();
        io.getInput();
        List<String> out = io.getPrints();

        assertEquals(out.get(5), "Otsikko: title");
    }

    @Test
    public void testFindByTitleDoesntFindBooksWithBadKeyword() {
        sqliteDb.create(new Book("link", "title"));
        App app = new App(sqliteDb, io, search, verifier);
        io.addInput("lepakko");
        app.findByTitle();
        io.getInput();
        io.getInput();
        List<String> out = io.getPrints();

        assertEquals(out.get(2), "Lukuvinkkeja ei loytynyt.");
    }

    @Test
    public void testCreateInvalidLink() {
        App app = new App(sqliteDb, io, search, verifier);
        connection.setConnectionStatus(false);
        io.addInput("");
        app.createBook();
        List<String> out = io.getPrints();

        assertEquals("Annetun linkin formaatti ei kelpaa.", out.get(1));
        assertEquals("Lukuvinkin lisays ei onnistunut!", out.get(2));
    }

    @Test
    public void testCreateBookWithoutInternet() {
        App app = new App(sqliteDb, io, search, verifier);
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
        App app = new App(sqliteDb, io, search, verifier);
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


