package com.example.store.service;

import com.example.store.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    Book findById(Long id);

    List<Book> findAll();
}
