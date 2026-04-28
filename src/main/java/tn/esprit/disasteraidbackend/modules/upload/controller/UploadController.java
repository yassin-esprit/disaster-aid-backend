package tn.esprit.disasteraidbackend.modules.upload.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
public class UploadController {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".pdf", ".jpg", ".jpeg", ".png");

    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new RuntimeException("File is required.");
        }

        String extension = extractExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new RuntimeException("Invalid file type. Only PDF, JPG, JPEG, and PNG are allowed.");
        }

        String filename = UUID.randomUUID() + extension;
        Path targetDirectory = Paths.get(uploadDir);
        Path targetPath = targetDirectory.resolve(filename);

        try {
            Files.createDirectories(targetDirectory);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to upload file.");
        }

        String url = "http://localhost:8090/uploads/docs/" + filename;
        return ResponseEntity.ok(ApiResponse.ok("Uploaded", url));
    }

    private String extractExtension(String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == originalFilename.length() - 1) {
            throw new RuntimeException("File extension is missing.");
        }
        return originalFilename.substring(dotIndex).toLowerCase();
    }
}
