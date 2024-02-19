package com.example.store.dto.orderitem;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private Integer quantity;
}
