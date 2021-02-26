package dao;

import domain.Book;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

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
            return Files.readString(Paths.get("schema.sql"));
        } catch (Exception ignored) { }
        return null;
    }

    @Override
    public Book create(Book book) {
        try {
            PreparedStatement p = this.db.prepareStatement("INSERT INTO Books (link, title) VALUES (?, ?)");
            p.setString(1, book.getLink());
            p.setString(2, book.getTitle());
            p.execute();
            return book;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            Statement s = this.db.createStatement();
            ResultSet r = s.executeQuery("SELECT link, title FROM Books");

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
        } catch (Exception ignored) {
            // error if table already exists
        }
    }
}
