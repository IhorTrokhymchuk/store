package com.example.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.store.dto.book.BookDto;
import com.example.store.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {
    private static final String ADD_BOOKS_SCRIPT_PATH =
            "database/books/add-books-with-any-categories-to-book-table.sql";
    private static final String ADD_CATEGORIES_SCRIPT_PATH =
            "database/category/add-categories-to-categories-table.sql";
    private static final String DELETE_ALL_SCRIPT_PATH =
            "database/delete-all-data.sql";
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws SQLException {
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
    @DisplayName("Create book with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void create_CreateBookWithValidData_ShouldReturnBookDto() throws Exception {
        Long testId = 4L;
        String testTitle = "testTileValid";
        String testAuthor = "testAuthorValid";
        String testIsbn = "testIsbnValid";
        Set<Long> testCategoryIds = Set.of(1L, 2L);
        BigDecimal testPrice = BigDecimal.valueOf(3.99);

        BookDto expectedDto = createExpectedBookDto(
                testId, testTitle, testAuthor, testIsbn, testCategoryIds, testPrice);
        CreateBookRequestDto requestDto = createCreateBookRequestDto(
                testTitle, testAuthor, testIsbn, testCategoryIds, testPrice);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertBookDto(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Update book with valid data")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void update_UpdateBookWithValidData_ShouldReturnUpdateBookDto() throws Exception {
        Long testId = 1L;
        String updateTitle = "updateTile";
        String updateAuthor = "updateAuthor";
        String updateIsbn = "updateIsbn";
        Set<Long> updateCategoryIds = Set.of(1L);
        BigDecimal updatePrice = BigDecimal.valueOf(3.99);

        BookDto expectedDto = createExpectedBookDto(
                testId, updateTitle, updateAuthor, updateIsbn, updateCategoryIds, updatePrice);
        CreateBookRequestDto requestDto = createCreateBookRequestDto(
                updateTitle, updateAuthor, updateIsbn, updateCategoryIds, updatePrice);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", testId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertBookDto(expectedDto, resultDto);
    }

    @Test
    @DisplayName("Delete book by valid id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void deleteById_DeleteBookById_ShouldReturnNoContent() throws Exception {
        Long testId = 1L;

        MvcResult result = mockMvc.perform(delete("/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Get book by valid id")
    @WithMockUser(username = "user", authorities = {"USER"})
    void findById_GetBookByValidId_ShouldReturnBookDto() throws Exception {
        Long testId = 1L;
        BookDto expectedDto = getBookDto(testId);

        MvcResult result = mockMvc.perform(get("/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertBookDto(expectedDto, resultDto);
    }

    @NotNull
    private static BookDto getBookDto(Long testId) {
        String testTitle = "TestBookTitle1";
        String testAuthor = "TestBookAuthor1";
        String testIsbn = "TestBookIsbn1";
        Set<Long> testCategoryIds = Set.of(1L);
        BigDecimal testPrice = BigDecimal.valueOf(100);

        BookDto expectedDto = new BookDto();
        expectedDto.setId(testId);
        expectedDto.setTitle(testTitle);
        expectedDto.setAuthor(testAuthor);
        expectedDto.setIsbn(testIsbn);
        expectedDto.setCategoryIds(testCategoryIds);
        expectedDto.setPrice(testPrice);
        return expectedDto;
    }

    private CreateBookRequestDto createCreateBookRequestDto(
            String title, String author, String isbn, Set<Long> categoryIds, BigDecimal price) {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(title);
        requestDto.setAuthor(author);
        requestDto.setIsbn(isbn);
        requestDto.setCategoryIds(categoryIds);
        requestDto.setPrice(price);
        return requestDto;
    }

    private BookDto createExpectedBookDto(
            Long id, String title, String author, String isbn,
            Set<Long> categoryIds, BigDecimal price) {
        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);
        expectedDto.setTitle(title);
        expectedDto.setAuthor(author);
        expectedDto.setIsbn(isbn);
        expectedDto.setCategoryIds(categoryIds);
        expectedDto.setPrice(price);
        return expectedDto;
    }

    private BookDto createExpectedBookDto(
            String title, String author, String isbn, Set<Long> categoryIds, BigDecimal price) {
        return createExpectedBookDto(null, title, author, isbn, categoryIds, price);
    }

    private void assertBookDto(BookDto expected, BookDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getCategoryIds(), actual.getCategoryIds());
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
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
