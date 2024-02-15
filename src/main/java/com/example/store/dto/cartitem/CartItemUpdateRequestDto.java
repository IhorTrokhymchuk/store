package com.example.store.dto.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemUpdateRequestDto {
    @Positive
    private int quantity;
}
