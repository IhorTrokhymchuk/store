package com.example.store.mapper;

import com.example.store.config.MapperConfig;
import com.example.store.dto.book.BookDto;
import com.example.store.dto.book.BookDtoWithoutCategoryIds;
import com.example.store.dto.book.CreateBookRequestDto;
import com.example.store.model.Book;
import com.example.store.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategories(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> setOfCategoriesIds = book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(setOfCategoriesIds);
    }

    BookDtoWithoutCategoryIds toBookDtoWithoutCategoryIds(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @AfterMapping
    default void setSubject(@MappingTarget Book book, CreateBookRequestDto requestDto) {
        Set<Category> setOfCategories = requestDto.getCategories().stream()
                .map(Category::new)
                .collect(Collectors.toSet());
        book.setCategories(setOfCategories);
    }
}
