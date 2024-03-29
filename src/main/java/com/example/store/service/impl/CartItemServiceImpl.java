package com.example.store.service.impl;

import com.example.store.dto.cartitem.CartItemDto;
import com.example.store.dto.cartitem.CartItemRequestDto;
import com.example.store.dto.cartitem.CartItemUpdateRequestDto;
import com.example.store.exception.EntityNotFoundException;
import com.example.store.exception.UnauthorizedModificationException;
import com.example.store.mapper.CartItemMapper;
import com.example.store.model.Book;
import com.example.store.model.CartItem;
import com.example.store.model.ShoppingCart;
import com.example.store.repository.book.BookRepository;
import com.example.store.repository.cartitem.CartItemRepository;
import com.example.store.service.CartItemService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Override
    public Set<CartItem> getAllByUserEmail(String email) {
        Set<CartItem> cartItems = cartItemRepository.getAllByShoppingCartUserEmail(email);
        if (cartItems.isEmpty()) {
            throw new EntityNotFoundException("Cant find cart item where user email: " + email);
        }
        return cartItems;
    }

    @Override
    @Transactional
    public CartItem addOrUpdateCartItem(CartItemRequestDto requestDto, ShoppingCart shoppingCart) {
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(requestDto.getBookId()))
                .findFirst();
        CartItem cartItem;
        if (existingCartItem.isPresent()) {
            cartItem = existingCartItem.get();
            cartItem.setQuantity(requestDto.getQuantity());
        } else {
            cartItem = cartItemMapper.toModel(requestDto);
            Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                    () -> new EntityNotFoundException("Can't find book with id: "
                            + requestDto.getBookId())
            );
            cartItem.setBook(book);
            cartItem.setShoppingCart(shoppingCart);
        }
        return cartItemRepository.save(cartItem);
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

    @Override
    public void deleteAllByUserShoppingCart(ShoppingCart shoppingCart) {
        cartItemRepository.deleteAllByShoppingCart(shoppingCart);
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
