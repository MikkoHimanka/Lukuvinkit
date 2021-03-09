package isbn;

import domain.Book;
import io.github.cdimascio.dotenv.Dotenv;

public class GoogleBookApi implements BookApi {

    private final String apiKey;

    public GoogleBookApi() {
        Dotenv env = Dotenv.load();
        apiKey = env.get("API_KEY");
    }

    @Override
    public boolean hasApiKey() {
        return apiKey != null;
    }

    @Override
    public Book getBook(String isbn) {
        return null;
    }
}
