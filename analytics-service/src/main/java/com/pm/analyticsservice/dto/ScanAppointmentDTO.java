package com.pm.analyticsservice.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pm.analyticsservice.model.enums.AppointmentStatus;
import com.pm.analyticsservice.model.enums.ScanType;

public class ScanAppointmentDTO {
    private UUID id;
    private String medicalRecordId;
    private String patientId;
    private String doctorId;
    private String radiologistId;
    private ScanType scanType;
    private String scanReason;
    private AppointmentStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime scheduledAt;
    private LocalDateTime completedAt;
    private String imageFilePath;
    private LocalDateTime imageUploadedAt;
    private String notes;
    private Double feeAmount;
    private String billingChargeId;

    // Additional fields for display
    private String patientName;
    private String doctorName;
    private String radiologistName;

    // Constructors
    public ScanAppointmentDTO() {}

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getRadiologistId() {
        return radiologistId;
    }

    public void setRadiologistId(String radiologistId) {
        this.radiologistId = radiologistId;
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

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public LocalDateTime getImageUploadedAt() {
        return imageUploadedAt;
    }

    public void setImageUploadedAt(LocalDateTime imageUploadedAt) {
        this.imageUploadedAt = imageUploadedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getBillingChargeId() {
        return billingChargeId;
    }

    public void setBillingChargeId(String billingChargeId) {
        this.billingChargeId = billingChargeId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getRadiologistName() {
        return radiologistName;
    }

    public void setRadiologistName(String radiologistName) {
        this.radiologistName = radiologistName;
    }
}
