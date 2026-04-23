package tn.esprit.disasteraidbackend.modules.identity.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.base-url:/uploads}")
    private String uploadBaseUrl;

    public String uploadProfileImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String original = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String filename = System.currentTimeMillis() + "_" + original.replaceAll("[^a-zA-Z0-9._-]", "_");

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        Path target = dir.resolve(filename).normalize();
        if (!target.toAbsolutePath().startsWith(dir.toAbsolutePath())) {
            throw new IOException("Invalid file path");
        }

        Files.write(target, file.getBytes(), StandardOpenOption.CREATE_NEW);

        return uploadBaseUrl + "/" + filename; // e.g. /uploads/167...png
    }
}
