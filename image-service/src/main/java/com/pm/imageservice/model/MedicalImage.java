package com.pm.imageservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MedicalImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID uploadedByStaffId;

    @Column(nullable = false)
    private String imageType; // MRI, CT, XRAY

    @Column(nullable = false)
    private String diseaseTag; // Stroke, Tumor, Pneumonia

    @Column(nullable = false)
    private String filePath; // S3 key or local path

    @Column(nullable = false)
    private String contentType; // image/png, image/jpeg

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;
    
}
