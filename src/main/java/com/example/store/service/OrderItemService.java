package com.example.store.service;

import com.example.store.model.OrderItem;
import java.util.List;
import java.util.Set;

public interface OrderItemService {
    List<OrderItem> saveAll(Set<OrderItem> orderItems);
}
