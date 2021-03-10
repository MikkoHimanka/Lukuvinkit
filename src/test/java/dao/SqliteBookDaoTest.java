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
import java.util.ArrayList;
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

    @Test
    public void testEditing() {
        Book book = new Book("www.x.fi", "x");
        sqliteDb.create(book);
        book.setLink("www.y.fi");
        book.setTitle("y");
        sqliteDb.updateBook(book);
        assertEquals(sqliteDb.getAll().size(), 1);
        book = sqliteDb.getAll().get(0);
        assertEquals(book.getTitle(), "y");
        assertEquals(book.getLink(), "www.y.fi");
    }

    @Test
    public void testEditingWithNull() {
        // New book without title
        Book book = new Book("www.x.fi");
        sqliteDb.create(book);
        book.setLink("www.y.fi");
        sqliteDb.updateBook(book);
        assertEquals(sqliteDb.getAll().size(), 1);
        book = sqliteDb.getAll().get(0);
        assertNull(book.getTitle());
        assertEquals(book.getLink(), "www.y.fi");
    }

    @Test
    public void testTags() {
        Book book = new Book("hi");
        book = sqliteDb.create(book);
        ArrayList<String> tags = new ArrayList<>();
        tags.add("1");
        tags.add("2");
        tags.add("3");
        sqliteDb.addTags(book, tags);
        List<Book> res = sqliteDb.findByTag("1");
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).getLink(), book.getLink());
        List<String> t = sqliteDb.findTagsByBook(book);
        assertEquals(t.size(), 3);
        assertEquals(t.get(0), "1");
        assertEquals(t.get(1), "2");
        assertEquals(t.get(2), "3");
    }

    @Test
    public void testDescriptionsOnCreate() {
        Book book = new Book("link", "title", "desc");
        sqliteDb.create(book);
        List<Book> books = sqliteDb.getAll();
        assertEquals(books.get(0).getDescription(), "desc");
    }

    @Test
    public void testDescriptionsOnModify() {
        Book book = new Book("link", "title", "desc1");
        book = sqliteDb.create(book);
        book.setDescription("desc2");
        sqliteDb.updateBook(book);
        List<Book> books = sqliteDb.getAll();

        assertEquals(books.get(0).getDescription(), "desc2");
    }

    @After
    public void deleteFile() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = conn.createStatement();
        s.execute("DROP TABLE Books");
        s.execute("DROP TABLE Tags");
        conn.close();
        File db = new File("test.db");
        db.delete();
    }

}
