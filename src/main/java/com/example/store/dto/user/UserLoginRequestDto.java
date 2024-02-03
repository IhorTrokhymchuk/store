package com.example.store.dto.user;

import com.example.store.validation.EmailValues;
import com.example.store.validation.PasswordValues;

public record UserLoginRequestDto(@EmailValues String email, @PasswordValues String password) {
}
