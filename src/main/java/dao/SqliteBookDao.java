package dao;

import domain.Book;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class SqliteBookDao implements BookDao {

    private Connection db;

    public SqliteBookDao(String name) {
        try {
            this.db = DriverManager.getConnection("jdbc:sqlite:" + name);
            createTable("Books");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void createTable(String name) {
        try {
            Statement statement = this.db.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS Books (id INT PRIMARY KEY, link TEXT, title TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
