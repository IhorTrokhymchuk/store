package com.example.store.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.store.model.Book;
import com.example.store.repository.category.CategoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
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

    @AfterEach
    public void tearDown() {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
