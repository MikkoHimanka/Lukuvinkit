package isbn;

import domain.Book;

import java.util.ArrayList;
import java.util.List;

public class StubApi implements BookApi {

    private List<Book> books =  new ArrayList<>();
    private boolean apiKey = true;

    public void addBook(Book book) {
        books.add(book);
    }

    public void setKey(boolean key) {
        apiKey = key;
    }

    @Override
    public boolean hasApiKey() {
        return apiKey;
    }

    @Override
    public Book getBook(String isbn) {
        if (books.size() == 0) return null;
        Book b = books.get(0);
        books.remove(0);
        return b;
    }
}
