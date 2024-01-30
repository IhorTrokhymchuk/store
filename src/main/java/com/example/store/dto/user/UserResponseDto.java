package com.example.store.dto.user;

import lombok.Data;

@Data
public class UserResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
