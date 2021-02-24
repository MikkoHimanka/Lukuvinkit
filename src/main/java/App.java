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
        io.print("Found " + books.size() + " books:");
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

    public void createBook() {
        io.print("Add link:");
        String link = io.getInput();
        io.print("Add title:");
        String title = io.getInput();

        Book result = dao.create(new Book(link, title));
        if (result != null) {
            io.print("Book added successfully!");
        } else {
            io.print("There was a problem adding a book!");
        }
    }

    public void switchContext() {
        io.print("What do you want to do? (A)dd a new book / (L)ist all books");
        String selection = io.getInput().toLowerCase();
        switch (selection) {
            case "a":   createBook();
                        break;
            case "l":   listAll();
                        break;
            default:    io.print("ERROR: selection not valid!");
                        break;
        }
    }
}
