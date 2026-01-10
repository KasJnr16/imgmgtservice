package com.pm.imageservice.storage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalImageStorageService implements ImageStorageService {

    private final Path rootLocation = Paths.get("images");

    @Override
    public String save(MultipartFile file, UUID patientId) throws IOException {
        Path patientFolder = rootLocation.resolve(patientId.toString());
        if (!Files.exists(patientFolder)) {
            Files.createDirectories(patientFolder);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = patientFolder.resolve(fileName);
        Files.copy(file.getInputStream(), destination);

        return destination.toString(); // store this path in DB
    }

    @Override
    public Resource load(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) throw new IOException("File not found: " + filePath);
        return new FileSystemResource(file);
    }

    @Override
    public void delete(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }
}
