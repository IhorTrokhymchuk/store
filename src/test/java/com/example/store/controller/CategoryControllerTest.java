package com.example.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.store.config.CustomMySqlContainer;
import com.example.store.dto.category.CategoryDto;
import com.example.store.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class CategoryControllerTest {
    @Container
    private static final CustomMySqlContainer MY_SQL_CONTAINER = CustomMySqlContainer.getInstance();
    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        MY_SQL_CONTAINER.start();
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    @Sql(scripts = "classpath:database/delete-all-data.sql")
    void setUp() {
    }

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
    }

    @Test
    @DisplayName("Create new categories")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void save_SaveNewCategory_ShouldReturnCategoryDto() throws Exception {
        String testName = "TestName";
        String testDescription = "TestDescription";

        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setName(testName);
        expectedDto.setDescription(testDescription);

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(testName);
        requestDto.setDescription(testDescription);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(resultDto);
        assertEquals(expectedDto.getName(), resultDto.getName());
        assertEquals(expectedDto.getDescription(), resultDto.getDescription());
    }

    @Test
    @DisplayName("Update category by id")
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Sql(scripts = "classpath:database/category/insert-test-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/category/delete-test-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_UpdateCategory_ShouldReturnUpdatedCategoryDto() throws Exception {
        Long categoryId = 1L;
        String updatedName = "UpdatedName";
        String updatedDescription = "UpdatedDescription";

        CreateCategoryRequestDto updateRequestDto = new CreateCategoryRequestDto();
        updateRequestDto.setName(updatedName);
        updateRequestDto.setDescription(updatedDescription);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto resultDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        assertNotNull(resultDto);
        assertEquals(categoryId, resultDto.getId());
        assertEquals(updatedName, resultDto.getName());
        assertEquals(updatedDescription, resultDto.getDescription());
    }

    @AfterEach
    @Sql(scripts = "classpath:database/delete-all-data.sql")
    void tearDown() {
    }
}
