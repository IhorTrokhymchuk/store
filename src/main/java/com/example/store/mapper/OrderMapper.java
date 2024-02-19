package com.example.store.mapper;

import com.example.store.config.MapperConfig;
import com.example.store.dto.order.OrderDto;
import com.example.store.model.CartItem;
import com.example.store.model.Order;
import com.example.store.model.OrderItem;
import java.math.BigDecimal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    OrderItem toOrder(CartItem cartItem, Order order);

    @AfterMapping
    default void setPrice(@MappingTarget OrderItem orderItem, CartItem cartItem) {
        if (cartItem != null && cartItem.getBook() != null) {
            BigDecimal bookPrice = cartItem.getBook().getPrice();
            orderItem.setPrice(bookPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
    }

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "status", ignore = true)
    OrderDto toDto(Order order);

    @AfterMapping
    default void setStatus(@MappingTarget OrderDto orderDto, Order order) {
        orderDto.setStatus(order.getStatus().getStatus().name());
    }
}
