package com.example.demo.service.Impl;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.example.demo.config.EmailAlreadyExistsException;
import com.example.demo.config.UnauthorizedException;
import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final FileStorageService fileStorageService;
    
    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Override
    public UserDto createUser(UserCreateRequest request) {
        validateUserRequest(request);
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAvatarUrl(request.getAvatarUrl());
        user.setBio(request.getBio());
        user.setRole("USER");

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateRequest request, MultipartFile avatarFile) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        
        if (request.getEmail() != null) {
            if (userRepository.findByEmail(request.getEmail())
                    .filter(existingUser -> !existingUser.getId().equals(id))
                    .isPresent()) {
                throw new EmailAlreadyExistsException("Email already registered");
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.saveFile(avatarFile);
            user.setAvatarUrl(avatarUrl);
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUserWithAvatar(UserWithAvatarRequest request) {
        validateUserRequest(request);
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto register(RegisterRequest request) {
        validateUserRequest(request);
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setBio(request.getBio());

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto login(LoginRequest request) {
        if (!StringUtils.hasText(request.getEmail()) || !StringUtils.hasText(request.getPassword())) {
            throw new UnauthorizedException("Email and password are required");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return userMapper.toDto(user);
    }

    private void validateUserRequest(Object request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        if (request instanceof UserCreateRequest) {
            UserCreateRequest userRequest = (UserCreateRequest) request;
            validateRequiredFields(userRequest.getName(), userRequest.getEmail(), userRequest.getPassword());
        } else if (request instanceof RegisterRequest) {
            RegisterRequest registerRequest = (RegisterRequest) request;
            validateRequiredFields(registerRequest.getName(), registerRequest.getEmail(), registerRequest.getPassword());
        } else if (request instanceof UserWithAvatarRequest) {
            UserWithAvatarRequest avatarRequest = (UserWithAvatarRequest) request;
            validateRequiredFields(avatarRequest.getName(), avatarRequest.getEmail(), avatarRequest.getPassword());
        }
    }

    private void validateRequiredFields(String name, String email, String password) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Name is required");
        }
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password is required");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}
