package com.example.store.repository.shoppingcart;

import com.example.store.model.ShoppingCart;
import com.example.store.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT sc FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems c WHERE sc.user.email = :email")
    Optional<ShoppingCart> findShoppingCartAndCartItemByUserEmail(String email);
}
