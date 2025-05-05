package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setBio(user.getBio());
        dto.setRole(user.getRole());
        return dto;
    }
    public User toEntity(UserWithAvatarRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Nên encode trong Service
        user.setBio(request.getBio());
        user.setAvatarUrl(handleAvatarUpload(request.getAvatar()));
        return user;
    }
    private String handleAvatarUpload(MultipartFile avatar) {
        if (avatar == null || avatar.isEmpty()) {
            return null;
        }

        try {
            String uploadDir = "uploads";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + avatar.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            avatar.transferTo(filePath.toFile());

            return "/" + uploadDir + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save avatar file", e);
        }
    }
}
