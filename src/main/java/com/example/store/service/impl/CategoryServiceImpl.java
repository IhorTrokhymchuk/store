package com.example.store.service.impl;

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
import com.example.store.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toModel(requestDto)));
    }

    @Override
    public CategoryDto findById(Long id) {
        return categoryMapper.toDto(getCategoryById(id));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category category = getCategoryById(id);
        BeanUtils.copyProperties(requestDto, category, "id", "is_deleted");
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategory(Long id, Pageable pageable) {
        List<Book> allByCategoryId = bookRepository.findAllByCategoryId(id, pageable);

        if (allByCategoryId.isEmpty()) {
            throw new EntityNotFoundException("Can't find books by category where id: " + id);
        }

        return allByCategoryId.stream()
                .map(bookMapper::toBookDtoWithoutCategoryIds)
                .toList();
    }

    private Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id)
        );
    }
}
