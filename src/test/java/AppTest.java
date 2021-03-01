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
import java.util.ArrayList;
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
        assertEquals(out.get(0), "Lukuvinkkejä ei löytynyt.");
		assertEquals(out.size(), 1);
    }

    @Test
    public void testListAllSimple() {
        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("www.google.com", "haku"));
        App app = new App(sqliteDb, io);
        app.listAll();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Löytyi 2 lukuvinkkiä:");
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
    public void testSwichContextWelcomeAndExit() {
        io.addInput("s");
        App app = new App(sqliteDb, io);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(1), "Komennot:");
        assertEquals(out.get(2), "(L)isää uusi lukuvinkki");
        assertEquals(out.get(3), "(N)äytä tallennetut lukuvinkit");
        assertEquals(out.get(4), "(E)tsi lukuvinkkejä");
        assertEquals(out.get(5), "(S)ulje sovellus");
        assertEquals(out.get(6), "Kiitos käynnistä, sovellus sulkeutuu.");
        assertEquals(out.size(), 7);
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
        assertEquals(out.get(2), "(L)isää uusi lukuvinkki");
        assertEquals(out.get(3), "(N)äytä tallennetut lukuvinkit");
        assertEquals(out.get(4), "(E)tsi lukuvinkkejä");
        assertEquals(out.get(5), "(S)ulje sovellus");
        assertEquals(out.get(6), "Lukuvinkkejä ei löytynyt.");
        assertEquals(out.get(7), "Komennot:");
        assertEquals(out.get(8), "(L)isää uusi lukuvinkki");
        assertEquals(out.get(9), "(N)äytä tallennetut lukuvinkit");
        assertEquals(out.get(10), "(E)tsi lukuvinkkejä");
        assertEquals(out.get(11), "(S)ulje sovellus");
        assertEquals(out.get(12), "Kiitos käynnistä, sovellus sulkeutuu.");
        assertEquals(out.size(), 13);
    }

    @Test
    public void testSwichContextFailedCommand() {
        io.addInput("lol");
        io.addInput("s");
        App app = new App(sqliteDb, io);
        app.switchContext();
        List<String> out = io.getPrints();
        assertEquals(out.get(6), "Virhe: komento oli puutteellinen!");
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

        assertEquals(out.get(2), "Lukuvinkkejä ei löytynyt.");
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

