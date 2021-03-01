package dao;

import domain.Book;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.stream.Stream;

public class SqliteBookDao implements BookDao {

    private Connection db;

    public SqliteBookDao(String name) {
        try {
            this.db = DriverManager.getConnection("jdbc:sqlite:" + name);
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSchema() {
        try {
            // Java 8 problems
            StringBuilder schema = new StringBuilder();
            Stream<String> stream = Files.lines(Paths.get("schema.sql"), StandardCharsets.UTF_8);
            stream.forEach(s -> schema.append(" " + s));
            return schema.toString();
        } catch (Exception ignored) { }
        return null;
    }

    @Override
    public Book create(Book book) {
        try {
            Statement s = this.db.createStatement();
            PreparedStatement p = this.db.prepareStatement("INSERT INTO Books (link, title, markedRead) VALUES (?, ?, 0)");
            p.setString(1, book.getLink());
            p.setString(2, book.getTitle());
            p.execute();

            // Retrieve book id
            ResultSet r = s.executeQuery("SELECT last_insert_rowid()");

            r.next();
            book.setId(r.getInt(1));

            return book;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Book setRead(Book book) {
        try {
            PreparedStatement p = this.db.prepareStatement("UPDATE Books SET markedRead=1 WHERE id=?");
            p.setString(1, String.valueOf(book.getId()));
            p.execute();
        } catch (Exception ignored) {}
        return null;
    }

    @Override
    public List<Book> getAll() {
        try {
            Statement s = this.db.createStatement();
            ResultSet r = s.executeQuery("SELECT id, link, title FROM Books");

            List<Book> books = new ArrayList<>();

            while (r.next()) {
                books.add(new Book(r.getString("link"), r.getString("title"), r.getInt("id")));
            }

            return books;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Book> getUnread() {
        try {
            Statement s = this.db.createStatement();
            ResultSet r = s.executeQuery("SELECT link, title FROM Books WHERE markedRead=0");

            List<Book> books = new ArrayList<>();

            while (r.next()) {
                books.add(new Book(r.getString("link"), r.getString("title")));
            }

            return books;
        } catch (Exception e) {
            return null;
        }
    }

    private void createTable() {
        try {
            Statement statement = this.db.createStatement();
            statement.execute(getSchema());
        } catch (Exception ignored) { }
    }

    @Override
    public List<Book> findByTitle(String title) {
        try {
            PreparedStatement p = this.db.prepareStatement("SELECT id, link, title FROM Books WHERE title LIKE ?");
            p.setString(1, "%" + title + "%");
            ResultSet r = p.executeQuery();

            List<Book> books = new ArrayList<>();

            while (r.next()) {
                books.add(new Book(r.getString("link"), r.getString("title"), r.getInt("id")));
            }

            return books;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
