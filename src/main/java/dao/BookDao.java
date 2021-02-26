package dao;

import domain.Book;

import java.util.List;

public interface BookDao {
    Book create(Book book);
    Book setRead(Book book);
    List<Book> getAll();
    List<Book> getUnread();
}
