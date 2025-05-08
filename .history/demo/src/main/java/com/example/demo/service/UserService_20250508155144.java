package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDto createUser(UserCreateRequest request);
    UserDto updateUser(Long id, UserUpdateRequest request, MultipartFile file);
    void deleteUser(Long id);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto createUserWithAvatar(UserCreateRequest request, MultipartFile file);
    UserDto register(RegisterRequest request);
    UserDto login(LoginRequest request);
}


