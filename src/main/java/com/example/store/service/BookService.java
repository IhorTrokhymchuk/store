package com.example.store.service;

import com.example.store.dto.book.BookDto;
import com.example.store.dto.book.BookSearchParametersDto;
import com.example.store.dto.book.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    List<BookDto> search(Pageable pageable, BookSearchParametersDto bookSearchParametersDto);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);
}
