package com.example.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.store.dto.book.BookDto;
import com.example.store.dto.book.BookSearchParametersDto;
import com.example.store.dto.book.CreateBookRequestDto;
import com.example.store.exception.EntityAlreadyExistsException;
import com.example.store.exception.EntityNotFoundException;
import com.example.store.mapper.BookMapper;
import com.example.store.model.Book;
import com.example.store.model.Category;
import com.example.store.repository.book.BookRepository;
import com.example.store.repository.book.BookSpecificationBuilder;
import com.example.store.service.impl.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private Specification<Book> specification;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("verify save method to valid data")
    void save_SaveBook_ShouldReturnBookDto() {
        String testIsbn = "testIsbn";

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setIsbn(testIsbn);

        Book book = new Book();
        book.setIsbn(testIsbn);

        BookDto bookDto = new BookDto();
        bookDto.setIsbn(testIsbn);

        when(bookRepository.findBookByIsbn(requestDto.getIsbn())).thenReturn(Optional.empty());
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(requestDto);

        assertNotNull(result);
        assertEquals(result.getIsbn(), testIsbn);

        verify(bookRepository, times(1)).findBookByIsbn(requestDto.getIsbn());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookMapper, times(1)).toDto(any(Book.class));

        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("verify save method to not valid isbn")
    void save_SaveBook_ShouldThrowException() {
        String testIsbn = "testIsbn";

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setIsbn(testIsbn);

        Book book = new Book();
        book.setIsbn(testIsbn);

        when(bookRepository.findBookByIsbn(requestDto.getIsbn()))
                .thenReturn(Optional.of(book));

        assertThrows(EntityAlreadyExistsException.class,
                () -> bookService.save(requestDto));

        verify(bookRepository, times(1)).findBookByIsbn(requestDto.getIsbn());
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("verify find by id method to valid id")
    void findById_FindBookById_ShouldReturnBookDto() {
        Long testId = 1L;
        Book book = new Book();
        book.setId(testId);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(testId);

        when(bookRepository.findById(testId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto result = bookService.findById(testId);

        assertNotNull(result);
        assertEquals(testId, result.getId());

        verify(bookRepository, times(1)).findById(testId);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("verify find by id method to not valid id")
    void findById_FindBookByIdWithNotValidId_ShouldThrowException() {
        Long testId = 1L;

        when(bookRepository.findById(testId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(testId));

        verify(bookRepository, times(1)).findById(testId);
        verifyNoMoreInteractions(bookRepository);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("verify find by id method to valid parameters")
    void search_FindBookByParameters_ShouldReturnListBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto();
        Book book = new Book();
        Page<Book> page = new PageImpl<>(List.of(book));

        when(bookSpecificationBuilder.build(searchParametersDto)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(page);
        when(bookMapper.toDto(any(Book.class))).thenReturn(new BookDto());

        List<BookDto> result = bookService.search(pageable, searchParametersDto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(page.getContent().size(), result.size());

        verify(bookSpecificationBuilder, times(1)).build(searchParametersDto);
        verify(bookRepository, times(1)).findAll(specification, pageable);
        verify(bookMapper, times(page.getContent().size())).toDto(any(Book.class));
    }

    @Test
    @DisplayName("verify find by id method to valid parameters")
    void search_FindBookByParameters_ShouldThrowException() {
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto();
        Page<Book> page = new PageImpl<>(List.of());

        when(bookSpecificationBuilder.build(searchParametersDto)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(page);

        assertThrows(EntityNotFoundException.class,
                () -> bookService.search(pageable, searchParametersDto));

        verify(bookSpecificationBuilder, times(1)).build(searchParametersDto);
        verify(bookRepository, times(1)).findAll(specification, pageable);
    }

    @Test
    @DisplayName("verify deleteById method to delete by id")
    void deleteById_deleteBookByValidId_ShouldDeleteCategory() {
        Long categoryId = 1L;

        bookService.deleteById(categoryId);

        verify(bookRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("verify findAll method returns all books")
    void findAll_WithExistingBooks_ReturnsBookDtoList() {
        Book book1 = new Book();
        Book book2 = new Book();
        Pageable pageable = PageRequest.of(0,10);
        Page<Book> page = new PageImpl<>(List.of(book1, book2));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        when(bookMapper.toDto(any(Book.class))).thenAnswer(
                invocation -> {
                    Book book = invocation.getArgument(0);
                    BookDto bookDto = new BookDto();
                    bookDto.setId(book.getId());
                    bookDto.setTitle(book.getTitle());
                    bookDto.setAuthor(book.getAuthor());
                    bookDto.setIsbn(book.getIsbn());
                    bookDto.setPrice(book.getPrice());
                    bookDto.setCategoryIds(book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet()));
                    bookDto.setDescription(book.getDescription());
                    bookDto.setCoverImage(book.getCoverImage());
                    return bookDto;
                }
        );

        List<BookDto> result = bookService.findAll(pageable);

        verify(bookRepository).findAll(any(Pageable.class));
        verify(bookMapper, times(page.getContent().size())).toDto(any(Book.class));

        assertEquals(result.size(), page.getContent().size());
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("verify findAll method returns all books")
    void findAll_WithNonExistingBooks_ShouldThrowException() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Book> page = new PageImpl<>(List.of());
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(page);

        assertThrows(EntityNotFoundException.class,
                () -> bookService.findAll(pageable));

        verify(bookRepository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
