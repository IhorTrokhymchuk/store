package com.example.store.service;

import com.example.store.dto.BookDto;
import com.example.store.dto.CreateBookRequestDto;
import com.example.store.model.Book;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> findAll();
}
