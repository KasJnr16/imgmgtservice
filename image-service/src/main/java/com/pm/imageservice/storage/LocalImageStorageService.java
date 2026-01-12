package com.pm.imageservice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalImageStorageService implements ImageStorageService {

    @Value("${image.storage.location:./images}")
    private String storageLocation;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(storageLocation);
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Override
    public String save(MultipartFile file, UUID patientId) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Cannot save empty file");
        }

        Path patientFolder = rootLocation.resolve(patientId.toString());
        if (!Files.exists(patientFolder)) {
            Files.createDirectories(patientFolder);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path destination = patientFolder.resolve(fileName);
        Files.copy(file.getInputStream(), destination);

        return destination.toString();
    }

    @Override
    public Resource load(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }
        return new FileSystemResource(file);
    }

    @Override
    public void delete(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }
}