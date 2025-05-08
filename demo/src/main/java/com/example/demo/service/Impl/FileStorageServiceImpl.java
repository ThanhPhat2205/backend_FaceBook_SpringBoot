package com.example.demo.service.Impl;

import com.example.demo.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Override
    public String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        try {
            // Tạo đường dẫn tuyệt đối
            Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
            
            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Kiểm tra và xử lý tên file
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new IllegalArgumentException("Tên file không hợp lệ");
            }

            // Tạo tên file duy nhất
            String filename = UUID.randomUUID() + "_" + originalFilename;
            Path targetLocation = uploadDir.resolve(filename);

            // Kiểm tra xem file đã tồn tại chưa
            if (Files.exists(targetLocation)) {
                throw new RuntimeException("File đã tồn tại: " + filename);
            }

            // Lưu file
            Files.copy(file.getInputStream(), targetLocation);

            // Trả về đường dẫn tương đối
            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file: " + e.getMessage(), e);
        }
    }
}

