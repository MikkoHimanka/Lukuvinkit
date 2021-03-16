import dao.SqliteBookDao;
import domain.Book;
import domain.Search;
import domain.URLVerifier;
import io.ConsoleIO;
import io.NetworkConnectionImpl;
import isbn.BookApi;
import isbn.GoogleBookApi;

import java.net.MalformedURLException;

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
        SqliteBookDao sql = new SqliteBookDao("sampledata.db");
        Search search = new Search(3.0);
        GoogleBookApi api = new GoogleBookApi();
        App app = new App(sql, consoleIO, search, urlVerifier, api);

        app.switchContext();
    }
}
