package com.example.store.service;

import com.example.store.dto.cartitem.CartItemDto;
import com.example.store.dto.cartitem.CartItemRequestDto;
import com.example.store.dto.cartitem.CartItemUpdateRequestDto;
import com.example.store.model.CartItem;
import com.example.store.model.ShoppingCart;
import java.util.Set;

public interface CartItemService {
    Set<CartItem> getAllByUserEmail(String email);

    CartItem addOrUpdateCartItem(CartItemRequestDto requestDto, ShoppingCart shoppingCart);

    CartItemDto update(CartItemUpdateRequestDto requestDto, Long cartItemId, String email);

    void deleteById(Long cartItemId, String email);

    void deleteAllByUserShoppingCart(ShoppingCart shoppingCart);
}
