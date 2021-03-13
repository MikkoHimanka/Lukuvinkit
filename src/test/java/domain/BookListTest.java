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
        this.books.add(new Book("www.url.com", "Kuusipuu", 0, "01-01-2000 kello 00:00"));
        this.books.add(new Book("www.orkki.com", "orkki", 1, "24-12-2024 kello 09:00"));
        this.books.add(new Book("www.kukkakauppa.com", "Kukkakauppa", 2, "13-03-2021 kello 12:34"));
        this.books.add(new Book("www.abcdefg.com", "abcdefg", 3, "06-06-2006 kello 06:06"));
        this.books.add(new Book("www.qwerty.se", "QwErTy", 4, "01-01-2021 kello 01:01"));
        this.books.add(new Book("linkki", "Otsikko", 5, "13-03-2021 kello 12:36"));
        this.books.add(new Book("123", "321", "kuvaus", "12-03-2321 kello 12:31"));

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

    @Test
    public void narrowingSearchPrintsMessageOnInvalidInput() {
        io.addInput("x");
        io.addInput("p");
        books = BookList.narrowingSearch(books, io);
        assertEquals(io.getPrints().contains("Virhe: komento oli puutteellinen!"), true);
    }

    @Test
    public void choosePrintsMessageOnInvalidInput() {
        io.addInput(Arrays.asList("d", "", "100"));
        Book book = BookList.choose(books, io);
        assertEquals(io.getPrints().contains("Valinnan taytyy olla numero!"), true);
        assertEquals(io.getPrints().contains("Valinta oli virheellinen!"), true);
    }

    @Test
    public void printBooksWithNumbersPrintsMessageOnEmptyList() {
        List<Book> books2 = new ArrayList<Book>();
        BookList.printBooksWithNumbers(books2, io);
        assertEquals(io.getPrints().contains("Lukuvinkkeja ei loytynyt."), true);
    }

    @Test
    public void printBookPrintsDescriptionOnlyIfFieldPopulated() {
        BookList.printBooks(Arrays.asList(books.get(0)), io);
        assertEquals(io.getPrints().contains("Kuvaus: "), false);
        BookList.printBooks(Arrays.asList(books.get(6)), io);
        assertEquals(io.getPrints().contains("Kuvaus: kuvaus"), true);
    }
}
