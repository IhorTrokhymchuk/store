package com.example.store.service;

import com.example.store.dto.book.BookDtoWithoutCategoryIds;
import com.example.store.dto.category.CategoryDto;
import com.example.store.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto findById(Long id);

    void deleteById(Long id);

    CategoryDto update(Long id, CreateCategoryRequestDto requestDto);

    List<CategoryDto> findAll(Pageable pageable);

    List<BookDtoWithoutCategoryIds> getBooksByCategory(Long id, Pageable pageable);
}
