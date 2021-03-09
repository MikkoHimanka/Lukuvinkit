package domain;

import io.StubIO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BookListTest {
    private List<Book> books;
    private StubIO io;

    @Before
    public void setUp(){
        io = new StubIO();
        this.books = new ArrayList<>();
        this.books.add(new Book("www.url.com", "Kuusipuu", 0));
        this.books.add(new Book("www.orkki.com", "orkki", 1));
        this.books.add(new Book("www.kukkakauppa.com", "Kukkakauppa", 2));
        this.books.add(new Book("www.abcdefg.com", "abcdefg", 3));
        this.books.add(new Book("www.qwerty.se", "QwErTy", 4));
        this.books.add(new Book("linkki", "Otsikko", 5));
    }

    @Test
    public void filterBooksFiltersCorrectlyByParams(){
        books = BookList.filterBooks(books, "a", "");
        assertEquals(books.size(), 2);
        books = BookList.filterBooks(books, "", "kukka");
        assertEquals(books.size(), 1);
    }

    @Test
    public void narrowingSearchFiltersWithCorrectInput() {
        io.addInput(Arrays.asList("L", "com", "o", "puu"));
        books = BookList.narrowingSearch(books, io);
        assertEquals(books.size(), 4);
        books = BookList.narrowingSearch(books, io);
        assertEquals(books.size(), 1);
    }

    @Test
    public void chooseWorksWithCorrectInput() {
        Book correct = books.get(2);
        io.addInput("3");
        Book chosen = BookList.choose(books, io);
        assertEquals(chosen, correct);
    }
}
