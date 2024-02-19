package com.example.store.repository.cartitem;

import com.example.store.model.CartItem;
import com.example.store.model.ShoppingCart;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c JOIN FETCH c.book WHERE c.id = :id")
    Optional<CartItem> findCartItemWithBookById(Long id);

    @Query("SELECT ci FROM CartItem ci "
            + "JOIN FETCH ci.book "
            + "JOIN FETCH ci.shoppingCart sc "
            + "JOIN FETCH sc.user us "
            + "WHERE us.email = :email")
    Set<CartItem> getAllByShoppingCartUserEmail(String email);

    void deleteAllByShoppingCart(ShoppingCart shoppingCart);
}
