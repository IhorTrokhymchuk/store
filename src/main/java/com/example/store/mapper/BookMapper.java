package com.example.store.mapper;

import com.example.store.config.MapperConfig;
import com.example.store.dto.BookDto;
import com.example.store.dto.CreateBookRequestDto;
import com.example.store.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
