import domain.Book;
import java.util.List;
import dao.BookDao;
import io.IO;
public class App {
    private BookDao dao;
    private IO io;

    public App(BookDao dao, IO io) {
        this.dao = dao;
        this.io = io;
    }

    public void listAll() {
        List<Book> books = dao.getAll();
        io.print("Found " + String.valueOf(books.size()) + " books:");
        io.print("****");
        for(int i = 0; i < books.size(); ++i) {
            printBook(books.get(i));
            io.print("****");
        }
    }

    private void printBook(Book book) {
        io.print("Link: " + book.getLink());
        io.print("Title: " + book.getTitle());
    }
}
