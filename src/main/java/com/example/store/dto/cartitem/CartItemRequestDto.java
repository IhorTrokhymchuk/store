package com.example.store.dto.cartitem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @Positive
    private Long bookId;
    @Positive
    private int quantity;
}
