import dao.SqliteBookDao;
import domain.Book;
import io.ConsoleIO;

public class Main {
    public static void main(String[] args) {
        ConsoleIO consoleIO = new ConsoleIO();
        SqliteBookDao sql = new SqliteBookDao("testi.db");
        SqliteBookDao.getSchema();
        App app = new App(sql, consoleIO);

        app.switchContext();
    }
}
