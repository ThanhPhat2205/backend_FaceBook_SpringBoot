package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Controller", description = "Quản lý người dùng")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }
    @Operation(summary = "Update user with file upload")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestPart("data") @Valid UserUpdateRequest request,
            @RequestPart(value = "file", required = false)
            @Parameter(description = "File to upload", content = @Content(schema = @Schema(type = "string", format = "binary")))
            MultipartFile file) {
        // Xử lý logic
        return ResponseEntity.ok(userService.updateUser(id, request, file));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
    @Operation(summary = "Create user with avatar")
    @PostMapping(value = "/with-avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> createUserWithAvatar(@ModelAttribute @Valid UserWithAvatarRequest request) {
        UserDto createdUser = userService.createUserWithAvatar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

}

