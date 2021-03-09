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
    public boolean setRead(Book book) {
        try {
            PreparedStatement p = this.db.prepareStatement("UPDATE Books SET markedRead=1 WHERE id=?");
            p.setString(1, String.valueOf(book.getId()));
            p.execute();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public List<Book> getAll() {
        try {
            Statement s = this.db.createStatement();
            ResultSet r = s.executeQuery("SELECT b.id, b.link, b.title, d.description " +
                    "FROM Books b " +
                    "LEFT JOIN Descriptions d " +
                    "ON b.id = d.book_id");

            List<Book> books = new ArrayList<>();

            while (r.next()) {
                books.add(new Book(r.getString("link"), r.getString("title"), r.getInt("id"), r.getString("description")));
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
            ResultSet r = s.executeQuery("SELECT b.id, b.link, b.title FROM Books b " +
                    "LEFT JOIN Descriptions d ON b.id = d.book_id WHERE b.markedRead=0");

            return formList(r);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<Book> findByTitle(String title) {
        try {
            PreparedStatement p = this.db.prepareStatement("SELECT id, link, title FROM Books WHERE title LIKE ?");
            p.setString(1, "%" + title + "%");
            ResultSet r = p.executeQuery();

            return formList(r);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean removeBook(Book book) {
        try {
            PreparedStatement p = this.db.prepareStatement("DELETE FROM Books WHERE id=?");
            p.setString(1, String.valueOf(book.getId()));
            p.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateBook(Book book) {
        // Given parameter is new book with variables that have already been updated
        String newLink = book.getLink();
        String newTitle = book.getTitle();
        int id = book.getId();

        try {
            PreparedStatement p = this.db.prepareStatement("UPDATE Books SET link=?, title=? WHERE id=?");
            p.setString(1, newLink);
            p.setString(2, newTitle);
            p.setInt(3, id);
            p.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addTags(Book book, List<String> tags) {
        try {
            int id = book.getId();
            for (String tag : tags) {
                addTag(id, tag);
            }
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public List<Book> findByTag(String tag) {
        try {
            PreparedStatement p = this.db.prepareStatement(
                    "SELECT B.id, B.link, B.title " +
                    "FROM Books B, Tags T " +
                    "WHERE B.id = T.book_id AND T.tag=?");
            p.setString(1, tag);
            ResultSet r = p.executeQuery();

            return formList(r);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public List<String> findTagsByBook(Book book) {
        try {
            int id = book.getId();
            PreparedStatement p = this.db.prepareStatement(
                    "SELECT T.tag " +
                            "FROM Books B, Tags T " +
                            "WHERE B.id = T.book_id AND B.id = ?");
            p.setInt(1, id);
            ResultSet r = p.executeQuery();
            List<String> result = new ArrayList<>();
            while (r.next()) {
                result.add(r.getString("tag"));
            }
            return result;
        } catch (Exception ignored) {
            return null;
        }
    }

    private void addTag(int id, String tag) throws SQLException {
        PreparedStatement p = this.db.prepareStatement("INSERT INTO Tags (book_id, tag) VALUES (?, ?)");
        p.setInt(1, id);
        p.setString(2, tag);
        p.execute();
    }

    private List<Book> formList(ResultSet r) throws SQLException {
        List<Book> books = new ArrayList<>();

        while (r.next()) {
            books.add(new Book(r.getString("link"), r.getString("title"), r.getInt("id")));
        }

        return books;
    }

    private String getSchema() {
        try {
            // Java 8 problems
            StringBuilder schema = new StringBuilder();
            Stream<String> stream = Files.lines(Paths.get("schema.sql"), StandardCharsets.UTF_8);
            stream.forEach(s -> schema.append(" ").append(s));
            return schema.toString();
        } catch (Exception ignored) { }
        return null;
    }

    private void createTable() {
        try {
            Statement statement = this.db.createStatement();
            statement.executeUpdate(getSchema());
        } catch (Exception ignored) { }
    }

    public void updateDescription(Book book) {
        try {
            int id = book.getId();
            PreparedStatement p;
            if (getDescription(id) == null) {
                p = this.db.prepareStatement(
                        "INSERT INTO Descriptions (book_id, description) VALUES (?, ?)"
                );
                p.setInt(1, id);
                p.setString(2, book.getDescription());
            } else {
                p = this.db.prepareStatement(
                        "UPDATE Descriptions " +
                                "SET description = ? " +
                                "WHERE book_id = ?"
                );
                p.setString(1, book.getDescription());
                p.setInt(2, id);
            }
            p.execute();
        } catch (Exception e) {

        }
    }

    public String getDescription(int id) {
        try {
            PreparedStatement p  = this.db.prepareStatement(
                    "SELECT description FROM Descriptions WHERE book_id = ?"
            );
            p.setInt(1, id);
            ResultSet r = p.executeQuery();

            if (r.next()) {
                return r.getString("description");
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
