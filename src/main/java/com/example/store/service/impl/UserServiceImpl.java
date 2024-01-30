package com.example.store.service.impl;

import com.example.store.dto.user.UserRegistrationRequestDto;
import com.example.store.dto.user.UserResponseDto;
import com.example.store.exception.EntityAlreadyExistsException;
import com.example.store.exception.RegistrationException;
import com.example.store.mapper.UserMapper;
import com.example.store.repository.user.UserRepository;
import com.example.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new EntityAlreadyExistsException("User with email: "
                    + requestDto.getEmail() + " is register");
        }
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new RegistrationException("Invalid passwords");
        }
        return userMapper.toDto(userRepository.save(userMapper.toModel(requestDto)));
    }
}
