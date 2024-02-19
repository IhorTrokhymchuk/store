package com.example.store.controller;

import com.example.store.dto.order.OrderDto;
import com.example.store.dto.order.OrderPatchRequestDto;
import com.example.store.dto.order.OrderRequestDto;
import com.example.store.dto.orderitem.OrderItemDto;
import com.example.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints to managing orders")
@RestController
@RequestMapping(value = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get orders", description = "Get all orders")
    public List<OrderDto> getAll(Authentication authentication, Pageable pageable) {
        return orderService.findAll(authentication.getName(), pageable);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get items", description = "Get order items by order id")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderId,
                                            Authentication authentication) {
        return orderService.getOrderItemsByOrderId(orderId, authentication.getName());
    }

    @GetMapping("/{orderId}/items/{orderItemId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get items", description = "Get order items by order id")
    public OrderItemDto getOrderItems(@PathVariable Long orderId,
                                            @PathVariable Long orderItemId,
                                            Authentication authentication) {
        return orderService.getOrderItemByOrderItemId(
                orderId, orderItemId, authentication.getName());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Create order", description = "Create a new order")
    public OrderDto save(@RequestBody OrderRequestDto requestDto, Authentication authentication) {
        return orderService.save(requestDto, authentication.getName());
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Patch order", description = "Update order status by id")
    public OrderDto updateStatus(@RequestBody OrderPatchRequestDto requestDto,
                                 @PathVariable Long orderId) {
        return orderService.updateStatus(requestDto, orderId);
    }
}
