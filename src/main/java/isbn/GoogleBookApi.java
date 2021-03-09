package isbn;

import domain.Book;
import io.github.cdimascio.dotenv.Dotenv;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.util.Scanner;

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

    public String performRequest(String isbn) {
        try {
            String baseUrl = "https://www.googleapis.com/books/v1/volumes?";
            String isbnUrl = "q=isbn%3D" + isbn;
            String keyUrl = "&key=" + apiKey;
            URL url = new URL(baseUrl + isbnUrl + keyUrl);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            StringBuilder response = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                response.append(scanner.next());
            }

            scanner.close();

            return response.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
