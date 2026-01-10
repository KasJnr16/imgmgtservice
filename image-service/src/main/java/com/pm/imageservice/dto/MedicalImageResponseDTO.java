package com.pm.imageservice.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MedicalImageResponseDTO {

    private String id;
    private String imageType;
    private String diseaseTag;
    private String downloadUrl;
    private LocalDateTime uploadedAt;
}
