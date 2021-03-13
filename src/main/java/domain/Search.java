package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search {

    private final double RANK_CUTOFF;

    static class RankedBook implements Comparable<RankedBook> {
        private final double rank;
        private final Book book;

        public RankedBook(double rank, Book book) {
            this.rank = rank;
            this.book = book;
        }

        public Book getBook() {
            return book;
        }

        public double getRank() {
            return this.rank;
        }

        @Override
        public int compareTo(RankedBook o) {
            return Double.compare(this.rank, o.rank);
        }
    }

    public Search(double cut) {
        this.RANK_CUTOFF = cut;
    }

    public List<Book> getBooksByRank(String keyword, List<Book> books) {
        List<RankedBook> rankedBooks = new ArrayList<>();

        for (Book book : books) {
            rankedBooks.add(new RankedBook(
                    this.calculateEditDistance(keyword, book.getTitle()),
                    book
            ));
        }

        Collections.sort(rankedBooks);

        List<Book> newBookList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            newBookList.add(rankedBooks.get(i).getBook());
        }

        return newBookList;
    }

    public List<Book> findBooksByTitle(String keyword, List<Book> books) {
        List<RankedBook> rankedBooks = new ArrayList<>();

        for (Book book : books) {
            rankedBooks.add(new RankedBook(
                    this.calculateEditDistance(keyword, book.getTitle()),
                    book
            ));
        }

        Collections.sort(rankedBooks);

        List<Book> found = new ArrayList<>();

        for (RankedBook rankedBook : rankedBooks) {
            if (rankedBook.getRank() <= RANK_CUTOFF) {
                found.add(rankedBook.getBook());
            } else {
                break;
            }
        }

        return found;
    }

    public int calculateEditDistance(String a, String b) {
        a = '#' + a;
        b = '#' + b;
        int n = a.length();
        int m = b.length();
        int[][] distance = new int[n + 1][m + 1];
        for (int i = 0; i <= n; ++i) {
            for (int j = 0; j <= m; ++j) {
                if (i == 0) {
                    distance[i][j] = j;
                }
                else if (j == 0) {
                    distance[i][j] = i;
                }
                else {
                    int choice1 = distance[i - 1][j] + 1;
                    int choice2 = distance[i][j - 1] + 1;
                    int choice3 = distance[i - 1][j - 1];
                    if (a.charAt(i - 1) != b.charAt(j - 1)) choice3++;
                    distance[i][j] = Math.min(Math.min(choice1, choice2), choice3);
                }
            }
        }

        return distance[n][m];
    }
}
