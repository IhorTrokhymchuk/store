package com.example.store.service;

import com.example.store.dto.cartitem.CartItemRequestDto;
import com.example.store.dto.shoppingcart.ShoppingCartDto;
import com.example.store.model.User;

public interface ShoppingCartService {
    ShoppingCartDto registerNewShoppingCart(User user);

    ShoppingCartDto addItemToShoppingCart(CartItemRequestDto requestDto, String email);

    ShoppingCartDto findShoppingCart(String email);

    ShoppingCartDto cleanCart(String email);
}
