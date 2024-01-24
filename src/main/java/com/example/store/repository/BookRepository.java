package com.example.store.repository;

import com.example.store.dto.BookDto;
import com.example.store.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    Book findById(Long id);

    List<Book> findAll();
}
