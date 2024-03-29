package com.example.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.store.dto.book.BookDtoWithoutCategoryIds;
import com.example.store.dto.category.CategoryDto;
import com.example.store.dto.category.CreateCategoryRequestDto;
import com.example.store.exception.EntityNotFoundException;
import com.example.store.mapper.BookMapper;
import com.example.store.mapper.CategoryMapper;
import com.example.store.model.Book;
import com.example.store.model.Category;
import com.example.store.repository.book.BookRepository;
import com.example.store.repository.category.CategoryRepository;
import com.example.store.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify save method to save valid data")
    void save_SavedValidData_ShouldReturnValid() {
        String testName = "testNameValid";
        String testDescription = "testDescriptionValid";

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        Category category = new Category();
        CategoryDto expectedDto = createCategoryDto(testName, testDescription);

        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto result = categoryService.save(requestDto);

        assertNotNull(result);
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());

        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Verify findById method to find by valid id")
    void findById_FindCategoryByValidId_ShouldReturnValidCategoryDto() {
        Long testCategoryId = 1L;

        Category category = new Category();
        CategoryDto expectedDto = new CategoryDto();

        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto findCategory = categoryService.findById(testCategoryId);

        assertNotNull(findCategory);
        assertEquals(expectedDto, findCategory);

        verify(categoryRepository).findById(testCategoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Verify findById method to find by non valid id")
    void findById_FindCategoryByNonValidId_ShouldThrowException() {
        Long testCategoryId = -1L;
        when(categoryRepository.findById(testCategoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(testCategoryId));

        verify(categoryRepository).findById(testCategoryId);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Verify deleteById method to delete by id")
    void deleteById_deleteCategoryByValidId_ShouldDeleteCategory() {
        Long categoryId = 1L;

        categoryService.deleteById(categoryId);

        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("Verify update method to update by id")
    void update_UpdateCategoryWithValidId_ShouldReturnUpdateDto() {
        Long categoryId = 1L;
        String updateName = "UpdateName";
        String updateDescription = "UpdateDescription";

        Category category = new Category();
        CategoryDto expectedDto = createCategoryDto(updateName, updateDescription);
        expectedDto.setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        CategoryDto result = categoryService.update(categoryId, requestDto);

        assertNotNull(result);
        assertEquals(expectedDto.getId(), result.getId());
        assertEquals(expectedDto.getName(), result.getName());
        assertEquals(expectedDto.getDescription(), result.getDescription());

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Verify update method to update by not valid id")
    void update_UpdateCategoryWithNotValid_ShouldThrowException() {
        Long categoryId = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(categoryId, requestDto));

        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify findAll method to give valid data")
    void findAll_GetExistentData_ShouldReturnListCategoriesDto() {
        Category category1 = new Category();
        Category category2 = new Category();
        Pageable pageable = PageRequest.of(0,10);
        Page<Category> page = new PageImpl<>(List.of(category1, category2));
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        when(categoryMapper.toDto(any(Category.class))).thenAnswer(
                invocation -> {
                    Category category = invocation.getArgument(0);
                    CategoryDto categoryDto = new CategoryDto();
                    categoryDto.setName(category.getName());
                    categoryDto.setDescription(category.getDescription());
                    return categoryDto;
                }
        );

        List<CategoryDto> result = categoryService.findAll(pageable);

        verify(categoryRepository).findAll(any(Pageable.class));
        verify(categoryMapper, times(page.getContent().size())).toDto(any(Category.class));

        assertEquals(result.size(), page.getContent().size());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify findAll method to give empty data")
    void findAll_IfDataIsNonexistentData_ShouldReturnEmptyList() {
        Pageable pageable = PageRequest.of(0,10);
        Page<Category> page = new PageImpl<>(List.of());

        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(page);

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertTrue(result.isEmpty());

        verify(categoryRepository).findAll(any(Pageable.class));
        verify(categoryMapper, times(page.getContent().size())).toDto(any(Category.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getBooksByCategory method to give valid data")
    void getBooksByCategory_GetExistentData_ShouldReturnListBookDto() {
        Long categoryId = 1L;
        Book firstDto = new Book();
        Book secondDto = new Book();

        Pageable pageable = PageRequest.of(0,10);
        List<Book> bookList = List.of(firstDto, secondDto);

        when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(bookList);
        when(bookMapper.toBookDtoWithoutCategoryIds(any(Book.class))).thenAnswer(
                invocation -> {
                    Book bookArgument = invocation.getArgument(0);
                    BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds
                            = new BookDtoWithoutCategoryIds();
                    bookDtoWithoutCategoryIds.setTitle(bookArgument.getTitle());
                    bookDtoWithoutCategoryIds.setAuthor(bookArgument.getAuthor());
                    bookDtoWithoutCategoryIds.setIsbn(bookArgument.getIsbn());
                    bookDtoWithoutCategoryIds.setPrice(bookArgument.getPrice());
                    bookDtoWithoutCategoryIds.setDescription(bookArgument.getDescription());
                    bookDtoWithoutCategoryIds.setCoverImage(bookArgument.getCoverImage());
                    return bookDtoWithoutCategoryIds;
                }
        );

        List<BookDtoWithoutCategoryIds> result
                = categoryService.getBooksByCategory(categoryId, pageable);

        verify(bookRepository).findAllByCategoryId(categoryId, pageable);
        verify(bookMapper, times(bookList.size())).toBookDtoWithoutCategoryIds(any(Book.class));
        assertEquals(result.size(), bookList.size());
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify getBooksByCategory method to give non exist data")
    void getBooksByCategory_GetNonexistentData_ShouldThrow() {
        Long categoryId = 1L;

        Pageable pageable = PageRequest.of(0,10);
        List<Book> bookList = List.of();

        when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(bookList);
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.getBooksByCategory(categoryId, pageable));

        verify(bookRepository).findAllByCategoryId(categoryId, pageable);
        verify(bookMapper, times(0)).toBookDtoWithoutCategoryIds(any(Book.class));
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    private CategoryDto createCategoryDto(String testName, String testDescription) {
        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setName(testName);
        expectedDto.setDescription(testDescription);
        return expectedDto;
    }
}
