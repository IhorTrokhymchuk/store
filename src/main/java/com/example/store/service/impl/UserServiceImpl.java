package com.example.store.service.impl;

import com.example.store.dto.user.UserRegistrationRequestDto;
import com.example.store.dto.user.UserResponseDto;
import com.example.store.exception.EntityAlreadyExistsException;
import com.example.store.exception.RegistrationException;
import com.example.store.mapper.UserMapper;
import com.example.store.model.Role;
import com.example.store.model.User;
import com.example.store.repository.role.RoleRepository;
import com.example.store.repository.user.UserRepository;
import com.example.store.service.ShoppingCartService;
import com.example.store.service.UserService;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new EntityAlreadyExistsException("User with email: "
                    + requestDto.getEmail() + " is register");
        }
        if (!requestDto.getPassword().equals(requestDto.getRepeatPassword())) {
            throw new RegistrationException("Invalid passwords");
        }
        User newUser = new User();
        newUser.setEmail(requestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        newUser.setLastName(requestDto.getLastName());
        newUser.setFirstName(requestDto.getFirstName());
        newUser.setShippingAddress(requestDto.getShippingAddress());
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByRole(Role.RoleName.USER));
        newUser.setRoles(roles);
        userRepository.save(newUser);
        shoppingCartService.registerNewShoppingCart(newUser);
        return userMapper.toDto(newUser);
    }
}
