
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
import org.junit.After;
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
    public void commandAddIsSelected() throws SQLException {
        sqliteDb = new SqliteBookDao("test.db");
    }
    
    @When("linkki {string} ja otsikko {string} ovat annettu")
    public void validLinkAndTitleAreEntered(String link, String title) {
        sqliteDb.create(new Book(link, title));
        app = new App(sqliteDb, ioStub);
        app.listAll();
    }
    
    @Then("lukuvinkki on lisatty listalle")
    public void bookIsAddedToList() {
        List<String> out = ioStub.getPrints();
        assertEquals(out.get(0), "Löytyi 1 lukuvinkkiä:");
        assertEquals(out.get(1), "****");
        assertEquals(out.get(2), "Linkki: www.is.fi");
        assertEquals(out.get(3), "Otsikko: lehti");
        assertEquals(out.get(4), "****");
    }
    
    @After
    public void deleteFile() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = conn.createStatement();
        s.execute("DROP TABLE Books;");
        conn.close();
        System.out.println("here");
        File db = new File("test.db");
        db.delete();
    }
}
