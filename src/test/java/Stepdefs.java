
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
import isbn.BookApi;
import isbn.StubApi;

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
    BookApi bookApi;

    @Before
    public void setup() {
        ioStub = new StubIO();
        search = new Search(3.0);
        bookApi = new StubApi();
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

    @When("tietokantaan tallennetaan lukuvinkki aikaleimalla {string}")
    public void addExampleBook(String time) {
        initializeBook(time);
    }

    @When("tietokantaan tallennetaan linkki {string} otsikolla {string}")
    public void addBooks(String link, String title) {
        sqliteDb.create(new Book(link, title));
    }

    @When("suoritetaan komennot:")
    public void the_following_animals(List<String> commands) {
        for (String command : commands) {
            ioStub.addInput(command);
        }
        ioStub.addInput("s");
        startApp();
    }

    @When("haetaan lukuvinkeista komennoilla {string}, {string} ja syotetaan hakuehto {string}")
    public void searchBook(String command1, String command2, String searchParameter) {
        ioStub.addInput(command1);
        ioStub.addInput(command2);
        ioStub.addInput(searchParameter);
        ioStub.addInput("s");
        startApp();
    }
    
    @When("valitaan komento {string} ja syotetaan linkki {string} seka otsikko {string}, eika syoteta tageja tietoja pyydettaessa")
    public void linkAndTitleAreEntered(String command, String link, String title) {
        ioStub.addInput(command);
        ioStub.addInput(link);
        ioStub.addInput(title);
        ioStub.addInput("");
        ioStub.addInput("");
        ioStub.addInput("s");
        startApp();
    }

    @When("valitaan komento {string}")
    public void commandIsEntered(String command) {
        ioStub.addInput(command);
        ioStub.addInput("s");
        startApp();
    }

    @Given("tietokannassa on esimerkkikirjasto")
    public void exampleLibrary() {
        sqliteDb.create(new Book("www.google.com", "Google", 0, "hakukone", "31-01-2021 kello 12:31"));
        sqliteDb.create(new Book("www.bing.com", "Bing", 1, "hakukone", "31-01-2021 kello 12:32"));
        sqliteDb.create(new Book("www.oracle.com", "Oracle", 2, "Java", "01-02-2021 kello 12:33"));
        sqliteDb.create(new Book("www.fox.com", "Fox", 3, "TV kanava", "01-02-2021 kello 22:13"));
        sqliteDb.create(new Book("www.github.com", "Github", 4, "VCS", "01-02-2021 kello 22:13"));
        sqliteDb.create(new Book("www.is.fi", "Ilta Sanomat", 5, "Iltapaivalehti", "01-02-2021 kello 22:13"));
    }

    @When("valitaan komento {string} ja syotetaan linkki {string} linkkia pyydettaessa")
    public void invalidLinkAndTitleAreEntered(String command, String link) {
        ioStub.addInput(command);
        ioStub.addInput(link);
        ioStub.addInput("s");
        startApp();
    }

    @When("merkataan toinen lukuvinkki luetuksi valitsemalla komennot {string}, {string} ja {string}")
    public void bookIsMarkedAsRead(String cmd1, String cmd2, String cmd3) {
        ioStub.addInput(cmd1);
        ioStub.addInput(cmd2);
        ioStub.addInput(cmd3);
        ioStub.addInput("s");
        startApp();
    }

    @When("merkataan toinen lukuvinkki luetuksi valitsemalla komennot {string}, {string}, {string} ja {string}")
    public void booksIsMarkedAsReadWithNarrowing(String cmd1, String cmd2, String cmd3, String cmd4) {
        ioStub.addInput(cmd1);
        ioStub.addInput(cmd2);
        ioStub.addInput(cmd3);
        ioStub.addInput(cmd4);
        ioStub.addInput("s");
        startApp();
    }

    @When("merkataan lukuvinkit luetuiksi valitsemalla komennot {string}, {string} ja {string} seka {string}")
    public void booksAreMarkedAsRead(String cmd1, String cmd2, String cmd3, String cmd4) {
        ioStub.addInput(cmd1);
        ioStub.addInput(cmd2);
        ioStub.addInput(cmd3);
        ioStub.addInput(cmd4);
        ioStub.addInput("s");
        startApp();
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
        assertEquals(out.get(out.size() - 1), "Kiitos kaynnista, sovellus sulkeutuu.");
        assertTrue(out.contains("Linkki: www.bing.com"));
        assertTrue(out.contains("Linkki: www.bing.com"));
        assertTrue(out.contains("Linkki: www.is.fi"));
    }

    @Then("{string} on ylempana kuin {string}")
    public void isBefore(String stringA, String stringB) throws SQLException {
        List<String> out = ioStub.getPrints();
        int stringIndexA = 0;
        int stringIndexB = 0;

        for (int i = 0; i < out.size(); ++i) {
            if (out.get(i).equals(stringA)) {
                stringIndexA = i;
            }
            if (out.get(i).equals(stringB)) {
                stringIndexB = i;
            }
        }

        assertTrue(stringIndexA < stringIndexB);
    }
  
    @Then("sovellus nayttaa lukuvinkille oikean lisaysajan")
    public void timestampIsCorrect() throws SQLException {
        List<String> out = ioStub.getPrints();
        assertEquals(out.get(0), "Tervetuloa Lukuvinkit-sovellukseen!\n");
        assertEquals(out.get(out.size() - 1), "Kiitos kaynnista, sovellus sulkeutuu.");
        boolean timeIsCorrect = false;
        for (String row : out) {
            if (row.matches("Luotu: [0-9]{2}-[0-9]{2}-[0-9]{4} kello [0-9]{2}:[0-9]{2}")) {
                timeIsCorrect = true;
            }
        }
        assertTrue(timeIsCorrect);
        deleteFile();
    }

    private void startApp() {
        app = new App(sqliteDb, ioStub, search, verifier, bookApi);
        app.switchContext();
    }

    private void initializeBooks() {
        sqliteDb.create(new Book("www.google.com", "", 1, "01-01-2000 kello 00:00"));
        sqliteDb.create(new Book("www.bing.com", "", 2, "01-01-2000 kello 00:00"));
    }

    private void initializeBook(String time) {
        sqliteDb.create(new Book("www.google.com", "hakukone", 1, time));
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
