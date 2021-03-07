import dao.SqliteBookDao;
import domain.Book;
import domain.Search;
import domain.URLVerifier;
import io.ConsoleIO;
import io.NetworkConnectionImpl;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class Main {
    public static void main(String[] args) {
        NetworkConnectionImpl connection;
        try {
            connection = new NetworkConnectionImpl("https://google.com", 4000);
        } catch(MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
        URLVerifier urlVerifier = new URLVerifier(connection);
        ConsoleIO consoleIO = new ConsoleIO();
        SqliteBookDao sql = new SqliteBookDao("testi.db");
        Search search = new Search(3.0);
        App app = new App(sql, consoleIO, search, urlVerifier);

        app.switchContext();
    }
}
