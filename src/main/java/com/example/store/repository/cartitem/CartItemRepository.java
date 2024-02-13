package com.example.store.repository.cartitem;

import com.example.store.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM CartItem c JOIN FETCH c.book WHERE c.id = :id")
    Optional<CartItem> findCartItemWithBookById(Long id);
}
