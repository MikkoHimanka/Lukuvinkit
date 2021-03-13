
import dao.BookDao;
import domain.Book;
import io.IO;

public class RemoveBook extends Command {

    public RemoveBook(IO io, BookDao dao) {
        super(io, dao);
    }

    @Override
    public boolean run(Book book) {
        if (confirmRemoval(book) == true) {
            if (removeBook(book)) {
                return true;
            }
        }
        io.print("Lukuvinkin poistaminen ei onnistunut");
        return false;
    }

    @Override
    public void actionQuestion() {
        io.print("Mika lukuvinkki poistetaan?\n");
    }

    private boolean confirmRemoval(Book book) {
        try {
            io.print(book.getLink());
            io.print(book.getTitle());
            io.print(book.getDescription());
            io.print("Haluatko varmasti poistaa taman lukuvinkin? (K, E)\n");
            if (io.getInput().toLowerCase().equals("k")) {
                return true;
            }
        } catch (Exception e) { }
        return false;
    }

    private boolean removeBook(Book book) {
        if (dao.removeBook(book)) {
            io.print("Lukuvinkki on poistettu!");
            return true;
        }
        return false;
    }

}
