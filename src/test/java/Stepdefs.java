
import dao.SqliteBookDao;
import domain.Book;
import domain.Search;
import domain.URLVerifier;
import io.StubIO;
import io.StubNetworkConnection;
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
import java.net.URL;
import java.util.ArrayList;
import java.net.MalformedURLException;
import static org.junit.Assert.assertTrue;

public class Stepdefs {

    App app;
    StubIO ioStub;
    SqliteBookDao sqliteDb;
    Search search;
    URLVerifier verifier;

    @Before
    public void setup() {
        ioStub = new StubIO();
        search = new Search(3.0);
    }

    @Before
    public void initVerifier() {
        List<URL> validUrls = new ArrayList<URL>();
        try {
            validUrls.add(new URL("http://www.google.com"));
            validUrls.add(new URL("http://www.bing.com"));
            validUrls.add(new URL("http://www.is.fi"));
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        StubNetworkConnection connection;
        connection = new StubNetworkConnection(true, validUrls);
        verifier = new URLVerifier(connection);
    }

    @Given("tietokanta on alustettu")
    public void databaseIsInitialized() throws SQLException {
        sqliteDb = new SqliteBookDao("test.db");
    }

    @When("tietokantaan tallennetaan kaksi lukuvinkkia")
    public void addExampleBooks() {
        initializeBooks();
    }

    @When("valitaan komento {string} ja syotetaan linkki {string} seka otsikko {string}, eika syoteta tageja tietoja pyydettaessa")
    public void linkAndTitleAreEntered(String command, String link, String title) {
        ioStub.addInput(command);
        ioStub.addInput(link);
        ioStub.addInput(title);
        ioStub.addInput("");
        ioStub.addInput("");
        ioStub.addInput("s");
        app = new App(sqliteDb, ioStub, search, verifier);
        app.switchContext();
    }

    @When("valitaan komento {string}")
    public void commandIsEntered(String command) {
        ioStub.addInput(command);
        ioStub.addInput("s");
        app = new App(sqliteDb, ioStub, search, verifier);
        app.switchContext();
    }

    @When("valitaan komento {string} ja syotetaan linkki {string} linkkia pyydettaessa")
    public void invalidLinkAndTitleAreEntered(String command, String link) {
        ioStub.addInput(command);
        ioStub.addInput(link);
        ioStub.addInput("s");
        app = new App(sqliteDb, ioStub, search, verifier);
        app.switchContext();
    }

    @When("merkataan toinen lukuvinkki luetuksi valitsemalla komennot {string}, {string} ja {string}")
    public void bookIsMarkedAsRead(String cmd1, String cmd2, String cmd3) {
        ioStub.addInput(cmd1);
        ioStub.addInput(cmd2);
        ioStub.addInput(cmd3);
        ioStub.addInput("s");
        app = new App(sqliteDb, ioStub, search, verifier);
        app.switchContext();
    }

    @When("merkataan lukuvinkit luetuiksi valitsemalla komennot {string}, {string} ja {string} seka {string}")
    public void booksAreMarkedAsRead(String cmd1, String cmd2, String cmd3, String cmd4) {
        ioStub.addInput(cmd1);
        ioStub.addInput(cmd2);
        ioStub.addInput(cmd3);
        ioStub.addInput(cmd4);
        ioStub.addInput("s");
        app = new App(sqliteDb, ioStub, search, verifier);
        app.switchContext();
    }

    @Then("sovellus hyvaksyy syotteet ja tulostaa {string}")
    public void bookIsAddedToList(String print) throws SQLException {
        List<String> out = ioStub.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(out.size() - 1), "Kiitos kaynnista, sovellus sulkeutuu.");
        assertTrue(out.contains(print));
        assertTrue(out.contains("Lisaa otsikko:"));
        deleteFile();
    }

    @Then("sovellus tulostaa {string}")
    public void systemPrints(String print) throws SQLException {
        List<String> out = ioStub.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(out.size() - 1), "Kiitos kaynnista, sovellus sulkeutuu.");
        assertTrue(out.contains(print));
        deleteFile();
    }

    @Then("sovellus listaa lukuvinkit")
    public void systemListsBooks() {
        List<String> out = ioStub.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(out.size() - 1), "Kiitos kaynnista, sovellus sulkeutuu.");;
        assertTrue(out.contains("Linkki: www.bing.com"));
        assertTrue(out.contains("Linkki: www.bing.com"));
        assertTrue(out.contains("Linkki: www.is.fi"));
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
