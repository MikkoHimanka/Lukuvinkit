package domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BookListTest {
    private List<Book> books;

    @Before
    public void setUp(){
        this.books = new ArrayList<>();
        this.books.add(new Book("www.url.com", "Kuusipuu", 0));
        this.books.add(new Book("www.orkki.com", "Örkki", 1));
        this.books.add(new Book("www.kukkakauppa.com", "Kukkakauppa", 2));
        this.books.add(new Book("www.abcdefg.com", "abcdefg", 3));
        this.books.add(new Book("www.qwerty.se", "QwErTy", 4));
        this.books.add(new Book("linkki", "Otsikko", 5));
    }

    @Test
    public void filterBooksFiltersCorrectlyByParams(){
        this.books = BookList.filterBooks(this.books, "a", "");
        assertEquals(this.books.size(), 2);
        this.books = BookList.filterBooks(this.books, "", "kukka");
        assertEquals(this.books.size(), 1);
    }
}
