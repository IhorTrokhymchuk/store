package com.example.store.service.impl;

import com.example.store.model.OrderItem;
import com.example.store.repository.orderitem.OrderItemRepository;
import com.example.store.service.OrderItemService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItem> saveAll(Set<OrderItem> orderItems) {
        return orderItemRepository.saveAll(orderItems);
    }
}
