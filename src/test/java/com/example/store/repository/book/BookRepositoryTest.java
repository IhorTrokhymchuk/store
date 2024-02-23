package com.example.store.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.store.model.Book;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    @Sql(scripts = "classpath:database/delete-all-data.sql")
    public void setUp() {
    }

    @Test
    @DisplayName("verify find book by isbn method with valid data")
    @Sql(scripts = {
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books/add-book-to-books-table.sql"
    },executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_GetBookByIsbn_ShouldReturnOptionalOfBook() {
        String testIsbn = "TestBookIsbn_1";
        Optional<Book> bookByIsbn = bookRepository.findBookByIsbn(testIsbn);
        assertTrue(bookByIsbn.isPresent());
        assertEquals(bookByIsbn.get().getIsbn(), testIsbn);
    }

    @Test
    @DisplayName("verify find book by isbn method with not valid data")
    @Sql(scripts = {
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books/add-book-to-books-table.sql"
    },executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findBookByIsbn_GetBookBynNotValidIsbn_ShouldReturnEmptyOptionalOfBook() {
        String testIsbn = "Non valid isbn";
        Optional<Book> bookByIsbn = bookRepository.findBookByIsbn(testIsbn);
        assertFalse(bookByIsbn.isPresent());
    }

    @Test
    @DisplayName("verify find books by category method with valid category")
    @Sql(scripts = {
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books/add-books-with-any-categories-to-book-table.sql"
    },executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByCategoryId_GetBookByCategory_ShouldReturnBookWithValidCategory() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> booksByCategoryId = bookRepository.findAllByCategoryId(categoryId, pageable);
        assertFalse(booksByCategoryId.isEmpty());
        assertEquals(2, booksByCategoryId.size());
        assertTrue(booksByCategoryId.get(0).getCategories().stream().anyMatch(
                category -> category.getId().equals(categoryId)));
        assertTrue(booksByCategoryId.get(1).getCategories().stream().anyMatch(
                category -> category.getId().equals(categoryId)));
    }

    @Test
    @DisplayName("verify find books by category method with not existing book with need category")
    @Sql(scripts = {
            "classpath:database/category/add-categories-to-categories-table.sql",
            "classpath:database/books/add-books-with-any-categories-to-book-table.sql"
    },executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByCategoryId_GetBookByCategory_ShouldReturnEmptyList() {
        Long categoryId = 3L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> booksByCategoryId = bookRepository.findAllByCategoryId(categoryId, pageable);
        assertTrue(booksByCategoryId.isEmpty());
    }

    @AfterEach
    @Sql(scripts = "classpath:database/delete-all-data.sql")
    public void tearDown() {
    }
}
