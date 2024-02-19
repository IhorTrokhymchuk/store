package com.example.store.service;

import com.example.store.dto.order.OrderDto;
import com.example.store.dto.order.OrderPatchRequestDto;
import com.example.store.dto.order.OrderRequestDto;
import com.example.store.dto.orderitem.OrderItemDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto save(OrderRequestDto requestDto, String email);

    List<OrderItemDto> getOrderItemsByOrderId(Long orderId, String email);

    List<OrderDto> findAll(String email, Pageable pageable);

    OrderDto updateStatus(OrderPatchRequestDto requestDto, Long orderId);

    OrderItemDto getOrderItemByOrderItemId(Long orderId, Long orderItemId, String email);
}
