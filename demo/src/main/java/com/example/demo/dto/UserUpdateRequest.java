package com.example.demo.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String avatarUrl;
    private String bio;
}

