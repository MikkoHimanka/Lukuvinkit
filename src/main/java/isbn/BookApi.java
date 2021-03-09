package isbn;

import domain.Book;

public interface BookApi {
    boolean hasApiKey();
    Book getBook(String isbn);
}
