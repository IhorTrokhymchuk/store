package com.example.store.repository.order;

import com.example.store.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o "
            + "INNER JOIN FETCH o.orderItems oi "
            + "INNER JOIN FETCH o.user u "
            + "WHERE u.email = :email")
    List<Order> findAllByUserEmail(String email, Pageable pageable);

    @Query("SELECT o FROM Order o "
            + "INNER JOIN FETCH o.orderItems oi "
            + "INNER JOIN FETCH o.user u "
            + "INNER JOIN FETCH o.status s "
            + "WHERE o.id = :id")
    Optional<Order> findOrderById(Long id);

}
