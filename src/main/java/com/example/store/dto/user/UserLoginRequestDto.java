package com.example.store.dto.user;

import com.example.store.validation.EmailValues;
import com.example.store.validation.PasswordValues;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @EmailValues
    private String email;
    @PasswordValues
    private String password;
}
