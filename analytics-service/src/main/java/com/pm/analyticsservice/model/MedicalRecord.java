package com.pm.analyticsservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private UUID patientId;
    
    @Column(nullable = false)
    private UUID createdByStaffId;
    
    @Column(nullable = false)
    private String chiefComplaint;
    
    @Column(columnDefinition = "TEXT")
    private String historyOfPresentIllness;
    
    @Column(columnDefinition = "TEXT")
    private String pastMedicalHistory;
    
    @Column(columnDefinition = "TEXT")
    private String medications;
    
    @Column(columnDefinition = "TEXT")
    private String allergies;
    
    // Vital Signs
    private Double temperature;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Double oxygenSaturation;
    
    @Column(name = "scan_requested")
    private Boolean scanRequested = false;
    
    @Column(name = "scan_request_notes", columnDefinition = "TEXT")
    private String scanRequestNotes;
    
    @Column(nullable = false)
    private LocalDateTime recordDate;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("record-diagnoses")
    private List<Diagnosis> diagnoses = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (recordDate == null) {
            recordDate = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}