package com.example.store.service;

import com.example.store.dto.user.UserRegistrationRequestDto;
import com.example.store.dto.user.UserResponseDto;
import com.example.store.exception.RegistrationException;

public interface UserService {

    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
