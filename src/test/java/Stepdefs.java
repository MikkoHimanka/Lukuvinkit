
import dao.SqliteBookDao;
import domain.Book;
import io.StubIO;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class Stepdefs {
    
    App app;
    StubIO ioStub;
    SqliteBookDao sqliteDb;
    
    @Before
    public void setup() {
        ioStub = new StubIO();
    }
    
    @Given("tietokanta on alustettu")
    public void databaseIsInitialized() throws SQLException {
        sqliteDb = new SqliteBookDao("test.db");
        initializeBooks();
    }
    
    @When("linkki {string} ja otsikko {string} ovat annettu")
    public void validLinkAndTitleAreEntered(String link, String title) {
        sqliteDb.create(new Book(link, title));
        app = new App(sqliteDb, ioStub);
        app.listAll();
    }
    
    @Then("lukuvinkki on lisatty listalle")
    public void bookIsAddedToList() throws SQLException {
        List<String> out = ioStub.getPrints();
        assertEquals(out.get(0), "Löytyi 3 lukuvinkkiä:");
        assertEquals(out.get(1), "****");
        assertEquals(out.get(2), "Linkki: www.google.com");
        assertEquals(out.get(3), "****");
        assertEquals(out.get(4), "Linkki: www.bing.com");
        assertEquals(out.get(5), "****");
        assertEquals(out.get(6), "Linkki: www.is.fi");
        assertEquals(out.get(7), "Otsikko: lehti");
        assertEquals(out.get(8), "****");
        assertEquals(out.size(), 9);
        deleteFile();
    }
    
    @When("lukemattoman lukuvinkin linkki {string} on annettu")
    public void unreadBookIsGiven(String link) {
        sqliteDb.setRead(new Book(link, "", 1));
        app = new App(sqliteDb, ioStub);
        app.listAllUnread();
    }
    
    @Then("lukuvinkki on merkitty luetuksi")
    public void linkIsMarkedAsRead() throws SQLException {
        List<String> out = ioStub.getPrints();
        assertEquals(out.get(0), "Löytyi 1 lukuvinkkiä:");
        assertEquals(out.get(1), "****");
        assertEquals(out.get(2), "Linkki: www.bing.com");
        assertEquals(out.get(3), "****");
        assertEquals(out.size(), 4);
        deleteFile();
    }
    
    private void initializeBooks() {
        sqliteDb.create(new Book("www.google.com", "", 1));
        sqliteDb.create(new Book("www.bing.com", "", 2));
    }
    
    private void deleteFile() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = conn.createStatement();
        s.execute("DROP TABLE Books;");
        conn.close();
        File db = new File("test.db");
        db.delete();
    }
}
