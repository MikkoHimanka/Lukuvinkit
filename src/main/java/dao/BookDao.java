package dao;

import domain.Book;

import java.util.List;

public interface BookDao {
    Book create(Book book);
    boolean setRead(Book book);
    List<Book> getAll();
    List<Book> getUnread();
    List<Book> findByTitle(String title);
    boolean removeBook(Book book);
    boolean updateBook(Book book);
    boolean addTags(Book book, List<String> tags);
    List<Book> findByTag(String tag);
    List<String> findTagsByBook(Book book);
}
