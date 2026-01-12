package com.pm.imageservice.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageStorageService {
    String save(MultipartFile file, UUID patientId) throws IOException;
    Resource load(String filePath) throws IOException;
    void delete(String filePath) throws IOException;
}