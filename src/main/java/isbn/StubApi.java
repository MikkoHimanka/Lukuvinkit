package isbn;

import domain.Book;

public class StubApi implements BookApi {
    @Override
    public boolean hasApiKey() {
        return false;
    }

    @Override
    public Book getBook(String isbn) {
        return null;
    }
}
