package dao;

import domain.Book;
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

public class SqliteBookDaoTest {

    private SqliteBookDao sqliteDb;

    @Before
    public void initDb() throws SQLException {
        sqliteDb = new SqliteBookDao("test.db");
    }

    @Test
    public void testCreateWithLink() {
        Book book = sqliteDb.create(new Book("link"));
        assertNotNull(book);
    }

    @Test
    public void testCreateWithTitle() {
        Book book = sqliteDb.create(new Book("link", "title"));
        assertNotNull(book);
    }

    @Test
    public void testGetAllWithLink() {
        sqliteDb.create(new Book("link"));
        sqliteDb.create(new Book("link2"));
        sqliteDb.create(new Book("link3"));

        List<Book> books = sqliteDb.getAll();

        assertEquals(books.get(0).getLink(), "link");
        assertEquals(books.get(1).getLink(), "link2");
        assertEquals(books.get(2).getLink(), "link3");
    }

    @Test
    public void testGetAllWithTitle() {
        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("link2", "title2"));

        List<Book> books = sqliteDb.getAll();

        assertEquals(books.get(0).getTitle(), "title");
        assertEquals(books.get(1).getTitle(), "title2");
    }

    @Test
    public void testDatabaseId() {

        List<Book> books = sqliteDb.getAll();
        assertEquals(books.size(), 0);

        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("link2", "title2"));

        books = sqliteDb.getAll();

        assertEquals(books.get(0).getId(), 1);
        assertEquals(books.get(1).getId(), 2);
    }

    @Test
    public void testMarking() {
        Book b = new Book("link", "title");
        sqliteDb.create(b);
        sqliteDb.setRead(b);

        List<Book> unread = sqliteDb.getUnread();
        assertEquals(unread.size(), 0);
    }

    @Test
    public void testListingUnread() {
        sqliteDb.create(new Book("link", "title"));
        sqliteDb.create(new Book("link2", "title2"));

        List<Book> books = sqliteDb.getUnread();

        assertEquals(books.size(), 2);
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
