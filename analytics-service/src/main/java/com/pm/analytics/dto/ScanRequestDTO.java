package com.pm.analytics.dto;

import com.pm.analytics.model.enums.ScanType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ScanRequestDTO {
    @NotBlank(message = "Medical record ID is required")
    private String medicalRecordId;

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;

    @NotNull(message = "Scan type is required")
    private ScanType scanType;

    @NotBlank(message = "Scan reason is required")
    private String scanReason;

    private String notes;

    // Constructors
    public ScanRequestDTO() {}

    public ScanRequestDTO(String medicalRecordId, String patientId, String doctorId, ScanType scanType, String scanReason) {
        this.medicalRecordId = medicalRecordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.scanType = scanType;
        this.scanReason = scanReason;
    }

    // Getters and Setters
    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public ScanType getScanType() {
        return scanType;
    }

    public void setScanType(ScanType scanType) {
        this.scanType = scanType;
    }

    public String getScanReason() {
        return scanReason;
    }

    public void setScanReason(String scanReason) {
        this.scanReason = scanReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
