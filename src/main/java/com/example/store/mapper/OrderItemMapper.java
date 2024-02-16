package com.example.store.mapper;

import com.example.store.config.MapperConfig;
import com.example.store.dto.orderitem.OrderItemDto;
import com.example.store.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);
}
