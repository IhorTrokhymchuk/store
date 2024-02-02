package com.example.store.dto.user;

import com.example.store.validation.EmailValues;
import com.example.store.validation.PasswordValues;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegistrationRequestDto {
    @NotNull
    @EmailValues
    private String email;
    @PasswordValues
    private String password;
    @PasswordValues
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String shippingAddress;
}
