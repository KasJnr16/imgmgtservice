package com.pm.imageservice.controller;

import com.pm.imageservice.dto.ImageUploadRequestDTO;
import com.pm.imageservice.model.MedicalImage;
import com.pm.imageservice.service.MedicalImageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/images")
public class MedicalImageController {

    private final MedicalImageService service;

    public MedicalImageController(MedicalImageService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    @Operation(summary = "Upload a medical image for a patient")
    public ResponseEntity<?> uploadImage(
            @RequestPart MultipartFile file,
            @RequestPart ImageUploadRequestDTO request
    ) {
        try {
            MedicalImage image = service.uploadImage(file, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(image);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all medical images for a patient")
    public ResponseEntity<List<MedicalImage>> getImagesByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(service.getImagesByPatient(patientId));
    }

  @GetMapping("/{imageId}/download")
@Operation(summary = "Download a medical image by ID")
public ResponseEntity<Resource> downloadImage(@PathVariable UUID imageId) {
    try {
        MedicalImage image = service.getImageById(imageId);
        Resource resource = service.getResourceForImage(image); // create a helper in service

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + new File(image.getFilePath()).getName() + "\"")
                .body(resource);

    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}


    @DeleteMapping("/{imageId}")
    @Operation(summary = "Delete a medical image by ID")
    public ResponseEntity<?> deleteImage(@PathVariable UUID imageId) {
        try {
            service.deleteImage(imageId);
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete image: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}
