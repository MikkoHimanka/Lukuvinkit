package isbn;

import domain.Book;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GoogleBookApi implements BookApi {

    private String apiKey;

    public GoogleBookApi() {
        try {
            Dotenv env = Dotenv.load();
            apiKey = env.get("API_KEY");
        } catch (Exception ignored) {
            apiKey = null;
        }
    }

    @Override
    public boolean hasApiKey() {
        return apiKey != null;
    }

    @Override
    public Book getBook(String isbn) {
        JSONObject data = performRequest(isbn);
        if (data == null) return null;

        String title = getTitle(data);
        String link = getLink(data);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy 'kello' HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(formatter);
        if (link == null) return null;

        return new Book(link, title, time);
    }

    private String getTitle(JSONObject blob) {
        return (String) blob.get("title");
    }

    private String getLink(JSONObject blob) {
        return (String) blob.get("infoLink");
    }

    private JSONObject performRequest(String isbn) {
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

            return parseRequest(response.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private JSONObject parseRequest(String data) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(data);
        JSONArray array = (JSONArray) obj.get("items");
        obj = (JSONObject) array.get(0);
        return (JSONObject) obj.get("volumeInfo");
    }
}
