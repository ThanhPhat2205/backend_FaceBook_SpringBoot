package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Thông tin chi tiết người dùng (UserDto)")
public class UserDto {
    @Schema(description = "ID người dùng", example = "1")
    private Long id;

    @Schema(description = "Tên người dùng", example = "Nguyễn Văn A")
    private String name;

    @Schema(description = "Email", example = "abc@example.com")
    private String email;

    @Schema(description = "URL ảnh đại diện")
    private String avatarUrl;

    @Schema(description = "Tiểu sử/Bio", example = "Tôi là một lập trình viên Java")
    private String bio;

    @Schema(description = "Vai trò", example = "USER")
    private String role;
}
