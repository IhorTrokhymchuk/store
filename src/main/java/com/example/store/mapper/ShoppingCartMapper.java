package com.example.store.mapper;

import com.example.store.config.MapperConfig;
import com.example.store.dto.shoppingcart.ShoppingCartDto;
import com.example.store.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCartDto requestDto);
}
