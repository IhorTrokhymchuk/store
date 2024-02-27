package com.example.store.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.store.model.Book;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final String ADD_BOOKS_SCRIPT_PATH =
            "database/books/add-books-with-any-categories-to-book-table.sql";
    private static final String ADD_CATEGORIES_SCRIPT_PATH =
            "database/category/add-categories-to-categories-table.sql";
    private static final String DELETE_ALL_SCRIPT_PATH = "database/delete-all-data.sql";
    @Autowired
    private BookRepository bookRepository;

    @BeforeAll
    static void beforeAll(@Autowired DataSource dataSource) throws SQLException {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(ADD_CATEGORIES_SCRIPT_PATH));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(ADD_BOOKS_SCRIPT_PATH));
        }
    }

    @Test
    @DisplayName("Verify find book by isbn method with valid data")
    void findBookByIsbn_GetBookByIsbn_ShouldReturnOptionalOfBook() {
        String testIsbn = "TestBookIsbn1";
        Optional<Book> bookByIsbn = bookRepository.findBookByIsbn(testIsbn);
        assertTrue(bookByIsbn.isPresent());
        assertEquals(testIsbn, bookByIsbn.get().getIsbn());
    }

    @Test
    @DisplayName("Verify find book by isbn method with not valid data")
    void findBookByIsbn_GetBookByNonexistentIsbn_ShouldReturnEmptyOptionalOfBook() {
        String testIsbn = "Non valid isbn";
        Optional<Book> bookByIsbn = bookRepository.findBookByIsbn(testIsbn);
        assertFalse(bookByIsbn.isPresent());
    }

    @Test
    @DisplayName("Verify find books by category method with valid category")
    void findAllByCategoryId_GetBookByExistentCategoryId_ShouldReturnBookWithValidCategory() {
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
    @DisplayName("Verify find books by category method with not existing book with need category")
    void findAllByCategoryId_GetBookByNonexistentCategoryId_ShouldReturnEmptyList() {
        Long categoryId = 3L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> booksByCategoryId = bookRepository.findAllByCategoryId(categoryId, pageable);
        assertTrue(booksByCategoryId.isEmpty());
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(DELETE_ALL_SCRIPT_PATH));
        }
    }
}
