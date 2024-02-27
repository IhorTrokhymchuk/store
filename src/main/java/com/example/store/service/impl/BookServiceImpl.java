package com.example.store.service.impl;

import com.example.store.dto.book.BookDto;
import com.example.store.dto.book.BookSearchParametersDto;
import com.example.store.dto.book.CreateBookRequestDto;
import com.example.store.exception.EntityAlreadyExistsException;
import com.example.store.exception.EntityNotFoundException;
import com.example.store.mapper.BookMapper;
import com.example.store.model.Book;
import com.example.store.repository.book.BookRepository;
import com.example.store.repository.book.BookSpecificationBuilder;
import com.example.store.service.BookService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        if (bookRepository.findBookByIsbn(requestDto.getIsbn()).isPresent()) {
            throw new EntityAlreadyExistsException("Book with isbn:"
                    + requestDto.getIsbn() + " is created");
        }
        return bookMapper.toDto(bookRepository.save(bookMapper.toModel(requestDto)));
    }

    @Override
    @Transactional
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Cant find book by id: " + id)
        );
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public List<BookDto> search(Pageable pageable,
                                BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> bookSpecification
                = bookSpecificationBuilder.build(bookSearchParametersDto);

        Page<Book> bookList = bookRepository.findAll(bookSpecification, pageable);

        if (bookList.isEmpty()) {
            throw new EntityNotFoundException("Cant find books with parameters: "
                + bookSearchParametersDto);
        }
        return bookList.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cant find book by id: " + id)
        );
        Book model = bookMapper.toModel(requestDto);
        BeanUtils.copyProperties(model, book, "id", "isDeleted");
        bookRepository.save(book);
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public List<BookDto> findAll(Pageable pageable) {
        Page<Book> bookDtoList = bookRepository.findAll(pageable);
        if (bookDtoList.isEmpty()) {
            throw new EntityNotFoundException("Cant find books");
        }
        return bookDtoList.stream()
                .map(bookMapper::toDto)
                .toList();
    }
}
