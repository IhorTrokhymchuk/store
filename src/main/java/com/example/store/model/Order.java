package com.example.store.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@SQLDelete(sql = "UPDATE orders SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private OrderStatus status;
    @Column(name = "total", nullable = false)
    private BigDecimal total;
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate = LocalDateTime.now();
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "orders_order_items",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "order_items_id")
    )
    private Set<OrderItem> orderItems = new HashSet<>();
    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;
}
