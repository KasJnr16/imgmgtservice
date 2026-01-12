package com.pm.analyticsservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

import com.pm.analyticsservice.model.enums.AppointmentStatus;
import com.pm.analyticsservice.model.enums.ScanType;

@Entity
@Table(name = "scan_appointments")
public class ScanAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "medical_record_id", nullable = false)
    private String medicalRecordId;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "doctor_id", nullable = false)
    private String doctorId;

    @Column(name = "radiologist_id")
    private String radiologistId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scan_type", nullable = false)
    private ScanType scanType;

    @Column(name = "scan_reason", columnDefinition = "TEXT")
    private String scanReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AppointmentStatus status;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "image_file_path")
    private String imageFilePath;

    @Column(name = "image_uploaded_at")
    private LocalDateTime imageUploadedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "fee_amount")
    private Double feeAmount;

    @Column(name = "billing_charge_id")
    private String billingChargeId;

    // Constructors
    public ScanAppointment() {
        this.status = AppointmentStatus.REQUESTED;
        this.requestedAt = LocalDateTime.now();
    }

    public ScanAppointment(String medicalRecordId, String patientId, String doctorId, ScanType scanType, String scanReason) {
        this();
        this.medicalRecordId = medicalRecordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.scanType = scanType;
        this.scanReason = scanReason;
    }

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
}
