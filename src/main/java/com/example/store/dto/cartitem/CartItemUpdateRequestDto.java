package com.example.store.dto.cartitem;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemUpdateRequestDto {
    @Min(1)
    private int quantity;
}
