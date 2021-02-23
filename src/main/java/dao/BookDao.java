package dao;

import domain.Book;

import java.util.List;

public interface BookDao {
    Book create(Book book);
    List<Book> getAll();
}
