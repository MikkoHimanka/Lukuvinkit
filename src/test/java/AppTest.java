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
        Connection db = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = db.createStatement();
        s.execute("CREATE TABLE IF NOT EXISTS Books ( id INTEGER PRIMARY KEY, link TEXT, title TEXT );");
        sqliteDb = new SqliteBookDao("test.db");
    }

    @Before
    public void initIO() {
        io = new StubIO();
    }

    @Test
    public void testListAllEmpty() {
        App app = new App(sqliteDb, io);
        System.out.println(app);
        app.listAll();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Found 0 books:");
        assertEquals(out.get(1), "****");
    }

    @Test
    public void testListAllSimple() {
        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("www.google.com", "haku"));
        App app = new App(sqliteDb, io);
        app.listAll();
        List<String> out = io.getPrints();
        assertEquals(out.get(0), "Found 2 books:");
        assertEquals(out.get(1), "****");
        assertEquals(out.get(2), "Link: link");
        assertEquals(out.get(3), "Title: title");
        assertEquals(out.get(4), "****");
        assertEquals(out.get(5), "Link: www.google.com");
        assertEquals(out.get(6), "Title: haku");
    }

    @After
    public void deleteFile() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = conn.createStatement();
        s.execute("DROP TABLE Books;");
        File db = new File("test.db");
        db.delete();
    }
}

