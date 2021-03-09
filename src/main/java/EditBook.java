
import dao.BookDao;
import domain.Book;
import domain.URLVerificationResult;
import domain.URLVerifier;
import io.IO;

public class EditBook extends Command {

    private URLVerifier urlVerifier;

    public EditBook(IO io, BookDao dao, URLVerifier urlVerifier) {
        super(io, dao);
        this.urlVerifier = urlVerifier;
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
            io.print("Lukuvinkin tiedot paivitetty onnistuneesti.");
            return true;
        }
        io.print("Lukuvinkin tietojen muokkaus ei onnistunut!");
        return false;
    }

    @Override
    public void actionQuestion() {
        io.print("Mita lukuvinkkia muokataan?\n");
    }

    public Book chooseProperty(Book book) {
        String link = book.getLink();
        String title = book.getTitle();
        loop:
        while (true) {
            io.print("Valitse muokattava ominaisuus:");
            io.print("Muokkaa (L)inkkia");
            io.print("Muokkaa (O)tsikkoa");
            io.print("Takaisin (V)alintaan");
            String input = io.getInput().toLowerCase();

            switch (input) {
                case ("l"):
                    io.print("Anna uusi linkki (aiempi " + link + "):");
                    link = io.getInput();
                    URLVerificationResult verificationResult = urlVerifier.verify(link);
                    URLVerifier.printVerificationResult(verificationResult, io);
                    if (verificationResult != URLVerificationResult.OK) {
                        return null;
                    }
                    book.setLink(link);
                    break loop;
                case ("o"):
                    io.print("Anna uusi otsikko (aiempi " + title + "):");
                    book.setTitle(io.getInput());
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
