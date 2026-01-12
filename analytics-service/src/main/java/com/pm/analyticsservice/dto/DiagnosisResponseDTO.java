package com.pm.analyticsservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pm.analyticsservice.model.DiagnosisSeverity;
import com.pm.analyticsservice.model.DiagnosisStatus;

public record DiagnosisResponseDTO(
    UUID id,
    UUID medicalRecordId,
    UUID diagnosedByStaffId,
    String diseaseName,
    String icdCode,
    String description,
    DiagnosisSeverity severity,
    DiagnosisStatus status,
    String treatmentPlan,
    String notes,
    LocalDateTime diagnosisDate
) {}
