package com.example.store.service.impl;

import com.example.store.dto.cartitem.CartItemDto;
import com.example.store.dto.cartitem.CartItemRequestDto;
import com.example.store.dto.cartitem.CartItemUpdateRequestDto;
import com.example.store.exception.EntityNotFoundException;
import com.example.store.exception.UnauthorizedModificationException;
import com.example.store.mapper.CartItemMapper;
import com.example.store.model.CartItem;
import com.example.store.model.ShoppingCart;
import com.example.store.repository.book.BookRepository;
import com.example.store.repository.cartitem.CartItemRepository;
import com.example.store.service.CartItemService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public CartItem save(CartItemRequestDto requestDto, ShoppingCart shoppingCart) {
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(requestDto.getBookId()))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItemToUpdate = existingCartItem.get();
            cartItemToUpdate.setQuantity(requestDto.getQuantity());
            return cartItemRepository.save(cartItemToUpdate);
        } else {
            CartItem cartItem = cartItemMapper.toModel(requestDto);
            cartItem.setBook(bookRepository.findById(requestDto.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find book with id: "
                            + requestDto.getBookId())));
            cartItem.setShoppingCart(shoppingCart);
            return cartItemRepository.save(cartItem);
        }
    }

    @Override
    @Transactional
    public CartItemDto update(CartItemUpdateRequestDto requestDto, Long cartItemId, String email) {
        CartItem cartItem = checkUserPermissionForCartItemModification(cartItemId, email);
        cartItem.setQuantity(requestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void deleteById(Long cartItemId, String email) {
        checkUserPermissionForCartItemModification(cartItemId, email);
        cartItemRepository.deleteById(cartItemId);
    }

    private CartItem checkUserPermissionForCartItemModification(Long cartItemId, String email) {
        CartItem cartItem = cartItemRepository.findCartItemWithBookById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem by id: " + cartItemId)
        );
        if (!cartItem.getShoppingCart().getUser().getEmail().equals(email)) {
            throw new UnauthorizedModificationException("User with email: " + email
                    + " can't modify cart item with id: " + cartItemId);
        }
        return cartItem;
    }
}
