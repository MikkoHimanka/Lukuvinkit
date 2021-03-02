import dao.SqliteBookDao;
import domain.Search;
import io.ConsoleIO;

public class Main {
    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO();
        SqliteBookDao sql = new SqliteBookDao("testi.db");
        Search search = new Search(3.0);
        App app = new App(sql, consoleIO, search);

        app.switchContext();
    }
}
