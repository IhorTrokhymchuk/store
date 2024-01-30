package com.example.store.mapper;

import com.example.store.config.MapperConfig;
import com.example.store.dto.user.UserRegistrationRequestDto;
import com.example.store.dto.user.UserResponseDto;
import com.example.store.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
