import dao.SqliteBookDao;
import domain.Book;
import domain.URLVerifier;
import io.StubIO;
import io.StubNetworkConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EditBookTest {
    private SqliteBookDao sqliteDb;
    private StubNetworkConnection connection;
    private URLVerifier verifier;
    private StubIO io;

    @Before
    public void initDb() {
        sqliteDb = new SqliteBookDao("test.db");
    }

    @Before
    public void initIO() {
        io = new StubIO();
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
    public void testQuestion() {
        EditBook e = new EditBook(io, sqliteDb, verifier);
        e.actionQuestion();
        List<String> out = io.getPrints();
        assertEquals(out.size(), 1);
        assertEquals(out.get(0), "Mita lukuvinkkia muokataan?\n");
    }

    @Test
    public void testChoosePropertyWithCorrectLink() {
        Book book = new Book("link", "title", 1, "desc");
        EditBook e = new EditBook(io, sqliteDb, verifier);
        io.addInput("l");
        io.addInput("http://www.google.com");
        book = e.chooseProperty(book);
        assertEquals("http://www.google.com", book.getLink());
    }
    @Test
    public void testChoosePropertyWithWrongLink() {
        Book book = new Book("link", "title", 1, "desc");
        EditBook e = new EditBook(io, sqliteDb, verifier);
        io.addInput("l");
        io.addInput("x");
        assertNull(e.chooseProperty(book));

        List<String> out = io.getPrints();
        assertEquals(out.size(), 7);
        assertEquals(out.get(5), "Anna uusi linkki (aiempi link):");
        assertEquals(out.get(6), "Annetun linkin sisaltoa ei ole saatavilla.");
    }

    @Test
    public void testChoosePropertyWithDesc() {
        Book book = new Book("link", "title", 1, "desc");
        EditBook e = new EditBook(io, sqliteDb, verifier);
        io.addInput("k");
        io.addInput("x");
        assertNotNull(e.chooseProperty(book));

        List<String> out = io.getPrints();
        assertEquals(out.size(), 6);
        assertEquals(out.get(5), "Anna uusi kuvaus:");
    }

    @Test
    public void testRunReturnsFalseWithBadParamsOrCancel() {
        EditBook e = new EditBook(io, sqliteDb, verifier);
        assertEquals(e.run(null), false);

        Book book = new Book("1", "2", "3");
        io.addInput("v");
        boolean res = e.run(book);
        assertEquals(res, false);
    }
    @Test
    public void testChoosePropertyAfterFalseSelection() {
        Book book = new Book("link", "title", 1, "desc");
        EditBook e = new EditBook(io, sqliteDb, verifier);
        io.addInput("lol");
        io.addInput("l");
        io.addInput("http://www.google.com");
        book = e.chooseProperty(book);

        List<String> out = io.getPrints();
        assertTrue(out.contains("Virhe: komento oli puutteellinen!\n"));
        assertEquals("http://www.google.com", book.getLink());
    }

    @After
    public void deleteFile() throws SQLException {
        File db = new File("test.db");
        db.delete();
    }

}
