
package com.pm.analyticsservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "diagnosis_images")
public class DiagnosisImage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diagnosis_id", nullable = false)
    @JsonBackReference("diagnosis-images")
    private Diagnosis diagnosis;
    
    @Column(nullable = false)
    private UUID imageId; // References image-service
    
    @Column(nullable = false)
    private String imageType; // MRI, CT, XRAY
    
    private String notes;
    
    @Column(nullable = false)
    private LocalDateTime linkedAt;
    
    @PrePersist
    protected void onCreate() {
        linkedAt = LocalDateTime.now();
    }
}
