package com.example.store.service;

import com.example.store.dto.BookDto;
import com.example.store.dto.BookSearchParametersDto;
import com.example.store.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> search(BookSearchParametersDto bookSearchParametersDto);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> findAll();
}
