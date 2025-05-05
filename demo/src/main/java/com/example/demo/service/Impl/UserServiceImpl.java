package com.example.demo.service.Impl;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import com.example.demo.dto.*;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    @Value("${upload.path:uploads}")
    private String uploadPath;
    @Override
    public UserDto createUser(UserCreateRequest request) {
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
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(request.getName());
        user.setAvatarUrl(request.getAvatarUrl());
        user.setBio(request.getBio());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto).orElseThrow();
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }
    @Override
    public UserDto createUserWithAvatar(UserWithAvatarRequest request) {
        String fileName = null;

        // Xử lý ảnh nếu có upload
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            try {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                fileName = UUID.randomUUID() + "_" + request.getAvatar().getOriginalFilename();
                File dest = new File(uploadPath + File.separator + fileName);
                request.getAvatar().transferTo(dest);
            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
            }
        }

        // Lưu user
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(request.getBio());
        user.setAvatarUrl(fileName != null ? "/uploads/" + fileName : null);
        user.setRole("USER");

        return userMapper.toDto(userRepository.save(user));
    }
}
