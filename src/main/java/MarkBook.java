import dao.BookDao;
import domain.Book;
import io.IO;

public class MarkBook extends Command {
    
    public MarkBook(IO io, BookDao dao) {
        super(io, dao);
    }
    
    @Override
    public boolean run(Book book) {
        if (dao.setRead(book)) {
            io.print("Lukuvinkki merkitty luetuksi!");
            return true;
        }
        io.print("Lukuvinkin merkitseminen luetuksi ei onnistunut!");
        return false;
    }
    
    @Override
    public void actionQuestion() {
        io.print("Mika lukuvinkki merkitaan luetuksi?\n");
    }
}
