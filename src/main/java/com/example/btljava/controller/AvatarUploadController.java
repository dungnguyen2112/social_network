package com.example.btljava.controller;

import com.example.btljava.domain.response.file.ResUploadFileDTO;
import com.example.btljava.service.FileService;
import com.example.btljava.util.annotation.ApiMessage;
import com.example.btljava.util.error.StorageException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class AvatarUploadController {

    @Value("${upload-file.base-uri}")
    private String baseDir;

    private final FileService fileService;

    public AvatarUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/{userId}/avatar")
    @ApiMessage("Upload user avatar")
    public ResponseEntity<ResUploadFileDTO> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            @PathVariable Long userId) throws IOException, StorageException {

        // Check if the file is empty
        if (file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a valid avatar file.");
        }

        // Check for valid file extensions
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
        boolean isValid = allowedExtensions.stream()
                .anyMatch(ext -> fileName.toLowerCase().endsWith(ext));

        if (!isValid) {
            throw new StorageException("Invalid file extension. Only jpg, jpeg, and png are allowed.");
        }

        // Create folder for the avatar based on userId if it doesn't exist
        String folder = "avatars/" + userId;
        Path folderPath = Paths.get(baseDir, folder);
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectories(folderPath);
                System.out.println(">>> Created directory for avatar upload: " + folderPath);
            } catch (IOException e) {
                throw new StorageException("Failed to create directory for avatar upload: " + e.getMessage());
            }
        }

        // Store the file
        String uploadedFileName;
        try {
            uploadedFileName = fileService.store(file, folderPath.toString());
        } catch (Exception e) {
            throw new StorageException("Failed to store file: " + e.getMessage());
        }

        // Create response DTO
        ResUploadFileDTO response = new ResUploadFileDTO(uploadedFileName, Instant.now());

        return ResponseEntity.ok().body(response);
    }
}
