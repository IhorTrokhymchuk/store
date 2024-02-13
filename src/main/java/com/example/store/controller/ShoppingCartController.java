package com.example.store.controller;

import com.example.store.dto.cartitem.CartItemDto;
import com.example.store.dto.cartitem.CartItemRequestDto;
import com.example.store.dto.cartitem.CartItemUpdateRequestDto;
import com.example.store.dto.shoppingcart.ShoppingCartDto;
import com.example.store.service.CartItemService;
import com.example.store.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Shopping cart management", description = "Endpoints to managing shopping cart")
@RestController
@RequestMapping(value = "/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get all categories",
            description = "Get a page of all available categories and can use sort")
    public ShoppingCartDto getAll(Authentication authentication, Pageable pageable) {
        return shoppingCartService.findShoppingCart(authentication.getName());
    }

    @PutMapping("cart-item/{cartItemId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update book quantity",
            description = "Update books quantity in shopping cart")
    public CartItemDto update(@RequestBody @Valid CartItemUpdateRequestDto requestDto,
                              @PathVariable Long cartItemId,
                              Authentication authentication) {
        return cartItemService.update(requestDto, cartItemId, authentication.getName());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("cart-item/{cartItemId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Delete item by id", description = "Delete item's from shopping cart")
    public void deleteById(@PathVariable Long cartItemId, Authentication authentication) {
        cartItemService.deleteById(cartItemId, authentication.getName());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add book to cart", description = "Add books to shopping cart")
    public ShoppingCartDto save(@RequestBody @Valid CartItemRequestDto requestDto,
                                Authentication authentication) {
        return shoppingCartService.addItemToShoppingCart(requestDto, authentication.getName());
    }
}