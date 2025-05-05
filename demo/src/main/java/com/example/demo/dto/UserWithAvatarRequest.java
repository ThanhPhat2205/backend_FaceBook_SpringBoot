package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserWithAvatarRequest {
    private String name;
    private String email;
    private String password;
    private String bio;
    @Schema(type = "string", format = "binary")
    private MultipartFile avatar;
}

