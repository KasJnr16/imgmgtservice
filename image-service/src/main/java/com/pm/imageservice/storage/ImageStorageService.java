package com.pm.imageservice.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface ImageStorageService {

    /**
     * Saves a file and returns its storage path
     */
    String save(MultipartFile file, UUID patientId) throws IOException;

    /**
     * Loads a file as a resource
     */
    Resource load(String filePath) throws IOException;

    /**
     * Deletes a file from storage
     */
    void delete(String filePath) throws IOException;
}
