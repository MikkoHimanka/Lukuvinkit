package domain;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SearchTest {

    private Search search;

    @Before
    public void setUp() {
        search = new Search(3.0);
    }

    @Test
    public void testListing() {
        List<Book> b = new ArrayList<>();
        // link doesn't matter
        b.add(new Book("x", "AA"));
        b.add(new Book("x", "AAB"));
        b.add(new Book("x", "XXXX"));

        List<Book> found = search.findBooksByTitle("A", b);

        assertEquals(found.size(), 2);
        assertEquals(found.get(0).getTitle(), "AA");
        assertEquals(found.get(1).getTitle(), "AAB");
    }

    @Test
    public void testDistance() {
        String a = "NEABJPJOI";
        String b = "RFMQRJKJKIA";
        assertEquals(search.calculateEditDistance(a, b), 8);
    }

    @Test
    public void testDistance2() {
        String a = "TWXFUABGBNLTBFNSUVQW";
        String b = "GPNJILFXJUIZPLTVUIB";
        assertEquals(search.calculateEditDistance(a, b), 19);
    }

    @Test
    public void testDistance3() {
        String a = "HSMOWJXKGRWSMD";
        String b = "JMRTLLNPXKKXZC";
        assertEquals(search.calculateEditDistance(a, b), 14);
    }

}
