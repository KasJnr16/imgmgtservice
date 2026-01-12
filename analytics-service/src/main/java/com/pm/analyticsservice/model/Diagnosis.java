package com.pm.analyticsservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "diagnoses")
public class Diagnosis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    @JsonBackReference("record-diagnoses")
    private MedicalRecord medicalRecord;
    
    @Column(nullable = false)
    private UUID diagnosedByStaffId;
    
    @Column(nullable = false)
    private String diseaseName;
    
    private String icdCode; // ICD-10 code
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private DiagnosisSeverity severity;
    
    @Enumerated(EnumType.STRING)
    private DiagnosisStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String treatmentPlan;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(nullable = false)
    private LocalDateTime diagnosisDate;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "diagnosis", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("diagnosis-images")
    private List<DiagnosisImage> images = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (diagnosisDate == null) {
            diagnosisDate = LocalDateTime.now();
        }
        if (status == null) {
            status = DiagnosisStatus.ACTIVE;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}