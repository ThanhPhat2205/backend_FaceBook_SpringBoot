package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.UserDto;

public interface AuthService {
    UserDto register(RegisterRequest request);
    UserDto login(LoginRequest request);
}
