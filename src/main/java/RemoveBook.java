
import dao.BookDao;
import domain.Book;
import io.IO;

public class RemoveBook extends Command {

    public RemoveBook(IO io, BookDao dao) {
        super(io, dao);
    }

    @Override
    public boolean run(Book book) {
        if (dao.removeBook(book)) {
            io.print("Lukuvinkki on poistettu!");
            return true;
        }
        io.print("Lukuvinkin poistaminen ei onnistunut");
        return false;
    }

    @Override
    public void actionQuestion() {
        io.print("Mikä lukuvinkki poistetaan?\n");
    }
    
}
