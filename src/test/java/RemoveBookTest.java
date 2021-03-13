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

import static org.junit.Assert.assertEquals;

public class RemoveBookTest {
    private SqliteBookDao sqliteDb;
    private StubIO io;

    @Before
    public void initDb() {
        sqliteDb = new SqliteBookDao("delete.db");
        sqliteDb.create(new Book("www.is.fi", "Iltasanomat", 1, "Uutisten lukemiseen"));
        sqliteDb.create(new Book("www.google.com", "Google", 2, "Hakukone"));
    }

    @Before
    public void initIO() {
        io = new StubIO();
    }

    @After
    public void deleteFile() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:delete.db");
        Statement s = conn.createStatement();
        s.execute("DROP TABLE Books;");
        conn.close();
        File db = new File("delete.db");
        db.delete();
    }

    @Test
    public void bookDeletionSuccessIfDeletionIsConfirmed() {
        Book book = new Book("www.is.fi", "Iltasanomat", 1, "Uutisten lukemiseen");
        Command command = new RemoveBook(io, sqliteDb);
        io.addInput("K");
        assertEquals(true, command.run(book));
        List<Book> bookList = sqliteDb.getAll();
        assertEquals(1, bookList.size());
    }

    @Test
    public void bookDeletionFailsIfDeletionIsNotConfirmed() {
        Book book = new Book("www.is.fi", "Iltasanomat", 1, "Uutisten lukemiseen");
        Command command = new RemoveBook(io, sqliteDb);
        io.addInput("E");
        assertEquals(false, command.run(book));
        List<Book> bookList = sqliteDb.getAll();
        assertEquals(2, bookList.size());
    }

}
