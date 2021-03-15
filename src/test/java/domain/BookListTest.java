package domain;
import dao.SqliteBookDao;

import io.StubIO;
import kotlin.Triple;
import org.javatuples.Triplet;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BookListTest {
    private SqliteBookDao sqliteDb;
    private List<Book> books;
    private StubIO io;
    private Triplet<List<Book>, String, String> booksTriplet;

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
        this.booksTriplet = new Triplet<>(this.books, "", "");

    }

    @Before
    public void initDb() {
        sqliteDb = new SqliteBookDao("test.db");
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
        books = BookList.narrowingSearch(booksTriplet, io).getValue0();
        assertEquals(books.size(), 4);
        books = BookList.narrowingSearch(booksTriplet, io).getValue0();
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
        books = BookList.narrowingSearch(booksTriplet, io).getValue0();
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
        BookList.printBooksWithNumbers(books2, io, sqliteDb);
        assertEquals(io.getPrints().contains("Lukuvinkkeja ei loytynyt."), true);
    }

    @Test
    public void printBooksWithNumbersWithoutTags() {
        List<Book> books2 = new ArrayList<Book>();
        books2.add(new Book("www.url.com", "Kuusipuu", 0, "01-01-2000 kello 00:00"));
        BookList.printBooksWithNumbers(books2, io, sqliteDb);
        List<String> out = io.getPrints();
        assertEquals("Loytyi 1 lukuvinkkia:", out.get(0));
        assertEquals("****", out.get(1));
        assertEquals("(1)", out.get(2));
        assertEquals("Linkki: www.url.com", out.get(3));
        assertEquals("Otsikko: Kuusipuu", out.get(4));
        assertEquals("Luotu: 01-01-2000 kello 00:00", out.get(5));
        assertEquals("****", out.get(6));
    }

    @Test
    public void printBooksWithoutNumbersWithoutTags() {
        List<Book> books2 = new ArrayList<Book>();
        books2.add(new Book("www.url.com", "Kuusipuu", 0, "01-01-2000 kello 00:00"));
        BookList.printBooks(books2, io, sqliteDb);
        List<String> out = io.getPrints();
        assertEquals("Loytyi 1 lukuvinkkia:", out.get(0));
        assertEquals("****", out.get(1));
        assertEquals("Linkki: www.url.com", out.get(2));
        assertEquals("Otsikko: Kuusipuu", out.get(3));
        assertEquals("Luotu: 01-01-2000 kello 00:00", out.get(4));
        assertEquals("****", out.get(5));
    }

    @Test
    public void printBooksWithNumbersWithTags() {
        List<Book> books2 = new ArrayList<Book>();
        List<String> tags = new ArrayList<String>();
        books2.add(new Book("www.url.com", "Kuusipuu", 0, "01-01-2000 kello 00:00"));
        tags.add("tagi1");
        tags.add("tagi2");
        sqliteDb.create(books2.get(0));
        sqliteDb.addTags(books2.get(0), tags);

        BookList.printBooksWithNumbers(books2, io, sqliteDb);
        List<String> out = io.getPrints();
        assertEquals("Loytyi 1 lukuvinkkia:", out.get(0));
        assertEquals("****", out.get(1));
        assertEquals("(1)", out.get(2));
        assertEquals("Linkki: www.url.com", out.get(3));
        assertEquals("Otsikko: Kuusipuu", out.get(4));
        assertEquals("Tagit: tagi1, tagi2", out.get(5));
        assertEquals("Luotu: 01-01-2000 kello 00:00", out.get(6));
        assertEquals("****", out.get(7));
    }

    @Test
    public void printBooksWithoutNumbersWithTags() {
        List<Book> books2 = new ArrayList<Book>();
        books2.add(new Book("www.url.com", "Kuusipuu", 0, "01-01-2000 kello 00:00"));
        List<String> tags = new ArrayList<String>();
        tags.add("tagi1");
        tags.add("tagi2");

        sqliteDb.create(books2.get(0));
        sqliteDb.addTags(books2.get(0), tags);
        BookList.printBooks(books2, io, sqliteDb);
        List<String> out = io.getPrints();
        assertEquals("Loytyi 1 lukuvinkkia:", out.get(0));
        assertEquals("****", out.get(1));
        assertEquals("Linkki: www.url.com", out.get(2));
        assertEquals("Otsikko: Kuusipuu", out.get(3));
        assertEquals("Tagit: tagi1, tagi2", out.get(4));
        assertEquals("Luotu: 01-01-2000 kello 00:00", out.get(5));
        assertEquals("****", out.get(6));
    }


    @Test
    public void printBookPrintsDescriptionOnlyIfFieldPopulated() {
        BookList.printBooks(Arrays.asList(books.get(0)), io, sqliteDb);
        assertEquals(io.getPrints().contains("Kuvaus: "), false);
        BookList.printBooks(Arrays.asList(books.get(6)), io, sqliteDb);
        assertEquals(io.getPrints().contains("Kuvaus: kuvaus"), true);
    }

    @After
    public void deleteFile() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        Statement s = conn.createStatement();
        s.execute("DROP TABLE Books;");
        s.execute("DROP TABLE Tags");
        conn.close();
        File db = new File("test.db");
        db.delete();
    }
}
