package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends AuthService {
    UserDto createUser(UserCreateRequest request);
    UserDto updateUser(Long id, UserUpdateRequest request, MultipartFile avatarFile);
    void deleteUser(Long id);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto createUserWithAvatar(UserWithAvatarRequest request);
}


