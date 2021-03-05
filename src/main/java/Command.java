
import dao.BookDao;
import domain.Book;
import io.IO;

public abstract class Command {
    protected IO io;
    protected BookDao dao;
    
    public Command(IO io, BookDao dao) {
        this.io = io;
        this.dao = dao;
    }
    public abstract boolean run(Book book);
    
    public abstract void actionQuestion();
}
