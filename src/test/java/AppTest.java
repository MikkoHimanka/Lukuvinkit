import dao.SqliteBookDao;
import domain.Book;
import io.StubIO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class AppTest {

    private SqliteBookDao sqliteDb;
    private StubIO io;

    @Before
    public void initDb() throws SQLException {
        sqliteDb = new SqliteBookDao("test.db");
    }

    @Before
    public void initIO() {
        io = new StubIO();
    }

    @Test
    public void testListAllEmpty() {
        App app = new App(sqliteDb, io);
        app.listAll();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Lukuvinkkej� ei l�ytynyt.");
	assertEquals(out.size(), 1);
    }
    
    @Test
    public void testListAllSimple() {
        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("www.google.com", "haku"));
        App app = new App(sqliteDb, io);
        app.listAll();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "L�ytyi 2 lukuvinkki�:");
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
        App app = new App(sqliteDb, io);
        app.listAllUnread();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "L�ytyi 1 lukuvinkki�:");
        assertEquals(out.get(1), "****");
        assertEquals(out.get(2), "Linkki: www.google.com");
        assertEquals(out.get(3), "Otsikko: haku");
        assertEquals(out.get(4), "****");
	assertEquals(out.size(), 5);
    }
    
    @Test
    public void testSwichContextWelcomeAndExit() {
        io.addInput("s");
        App app = new App(sqliteDb, io);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(1), "Komennot:");
        assertEquals(out.get(2), "(L)is�� uusi lukuvinkki");
        assertEquals(out.get(3), "(N)�yt� tallennetut lukuvinkit");
        assertEquals(out.get(4), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(5), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(6), "(E)tsi lukuvinkkej�");
        assertEquals(out.get(7), "(S)ulje sovellus");
        assertEquals(out.get(8), "Kiitos k�ynnist�, sovellus sulkeutuu.");
        assertEquals(out.size(), 9);
    }

    @Test
    public void testSwichContextListAllAndExit() {
        io.addInput("n");
        io.addInput("s");
        App app = new App(sqliteDb, io);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(1), "Komennot:");
        assertEquals(out.get(2), "(L)is�� uusi lukuvinkki");
        assertEquals(out.get(3), "(N)�yt� tallennetut lukuvinkit");
        assertEquals(out.get(4), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(5), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(6), "(E)tsi lukuvinkkej�");
        assertEquals(out.get(7), "(S)ulje sovellus");
        assertEquals(out.get(8), "Lukuvinkkej� ei l�ytynyt.");
        assertEquals(out.get(9), "Komennot:");
        assertEquals(out.get(10), "(L)is�� uusi lukuvinkki");
        assertEquals(out.get(11), "(N)�yt� tallennetut lukuvinkit");
        assertEquals(out.get(12), "(M)erkitse lukuvinkki luetuksi");
        assertEquals(out.get(13), "(Li)staa lukemattomat lukuvinkit");
        assertEquals(out.get(14), "(E)tsi lukuvinkkej�");
        assertEquals(out.get(15), "(S)ulje sovellus");
        assertEquals(out.get(16), "Kiitos k�ynnist�, sovellus sulkeutuu.");
        assertEquals(out.size(), 17);
    }

    @Test
    public void testSwichContextFailedCommand() {
        io.addInput("lol");
        io.addInput("s");
        App app = new App(sqliteDb, io);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(8), "Virhe: komento oli puutteellinen!");
    }

    @Test
    public void testFindByTitleFindsBooksWithKeyword() {
        sqliteDb.create(new Book("link", "title"));
        App app = new App(sqliteDb, io);
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
        App app = new App(sqliteDb, io);
        io.addInput("lepakko");
        app.findByTitle();
        io.getInput();
        io.getInput();
        List<String> out = io.getPrints();

        assertEquals(out.get(2), "Lukuvinkkej� ei l�ytynyt.");
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

