package com.example.store.service.impl;

import com.example.store.dto.cartitem.CartItemRequestDto;
import com.example.store.dto.shoppingcart.ShoppingCartDto;
import com.example.store.exception.EntityNotFoundException;
import com.example.store.mapper.ShoppingCartMapper;
import com.example.store.model.ShoppingCart;
import com.example.store.model.User;
import com.example.store.repository.shoppingcart.ShoppingCartRepository;
import com.example.store.repository.user.UserRepository;
import com.example.store.service.CartItemService;
import com.example.store.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto registerNewShoppingCart(User user) {
        return shoppingCartMapper.toDto(createShoppingCart(user));
    }

    @Override
    @Transactional
    public ShoppingCartDto addItemToShoppingCart(CartItemRequestDto requestDto, String email) {
        ShoppingCart shoppingCart = getShoppingCart(email);
        shoppingCart.getCartItems().add(cartItemService.save(requestDto, shoppingCart));
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto findShoppingCart(String email) {
        return shoppingCartMapper.toDto(getShoppingCart(email));
    }

    private ShoppingCart getShoppingCart(String email) {
        Optional<ShoppingCart> existingShoppingCart =
                shoppingCartRepository.findShoppingCartAndCartItemByUserEmail(email);

        return existingShoppingCart.orElseGet(
                () -> createShoppingCart(userRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Cant find user with email: " + email)
        )));
    }

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart newShoppingCart = new ShoppingCart();
        newShoppingCart.setUser(user);
        return shoppingCartRepository.save(newShoppingCart);
    }
}
