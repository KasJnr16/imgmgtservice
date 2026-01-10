package com.pm.imageservice.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class ImageUploadRequestDTO {

    private UUID patientId;
    private UUID staffId;
    private String imageType;
    private String diseaseTag;
}

