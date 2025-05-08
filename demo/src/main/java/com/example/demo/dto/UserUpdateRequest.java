package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserUpdateRequest {
    @Schema(description="Tên người dùng")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Schema(description="Email người dùng")
    private String email;

    @Size(max = 500, message = "Bio must be less than 500 characters")
    private String bio;

}


