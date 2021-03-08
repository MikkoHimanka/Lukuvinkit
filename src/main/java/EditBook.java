
import dao.BookDao;
import domain.Book;
import io.IO;

public class EditBook extends Command {

    public EditBook(IO io, BookDao dao) {
        super(io, dao);
    }

    @Override
    public boolean run(Book book) {
        if (book == null) {
            return false;
        }
        if (chooseProperty(book) == null) {
            return false;
        }
        if (dao.updateBook(book)) {
            io.print("Lukuvinkin tiedot päivitetty onnistuneesti.");
            return true;
        }
        io.print("Lukuvinkin tietojen muokkaus ei onnistunut!");
        return false;
    }

    @Override
    public void actionQuestion() {
        io.print("Mitä lukuvinkkiä muokataan?\n");
    }

    public Book chooseProperty(Book book) {
        String link = book.getLink();
        String title = book.getTitle();
        loop:
        while (true) {
            io.print("Valitse muokattava ominaisuus:");
            io.print("Muokkaa (L)inkkiä");
            io.print("Muokkaa (O)tsikkoa");
            io.print("Takaisin (V)alintaan");
            String input = io.getInput().toLowerCase();

            switch (input) {
                case ("l"):
                    io.print("Anna uusi linkki (aiempi " + link + "):");
                    link = io.getInput().toLowerCase();
                    book.setLink(link);
                    break loop;
                case ("o"):
                    io.print("Anna uusi otsikko (aiempi " + title + "):");
                    book.setTitle(io.getInput().toLowerCase());
                    break loop;
                case ("v"):
                    return null;
                default:
                    io.print("Virhe: komento oli puutteellinen!\n");
                    break;
            }
        }
        return book;
    }
}
