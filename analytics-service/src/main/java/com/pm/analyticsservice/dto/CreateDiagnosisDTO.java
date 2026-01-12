package com.pm.analyticsservice.dto;

import com.pm.analyticsservice.model.DiagnosisSeverity;
import com.pm.analyticsservice.model.DiagnosisStatus;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CreateDiagnosisDTO {
    private UUID medicalRecordId;
    private UUID diagnosedByStaffId;
    private String diseaseName;
    private String icdCode;
    private String description;
    private DiagnosisSeverity severity;
    private DiagnosisStatus status;
    private String treatmentPlan;
    private String notes;
    private List<UUID> imageIds; // Link to images from image-service
}