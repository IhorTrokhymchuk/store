package com.example.store.service;

import com.example.store.dto.BookDto;
import com.example.store.dto.BookSearchParametersDto;
import com.example.store.dto.CreateBookRequestDto;
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
