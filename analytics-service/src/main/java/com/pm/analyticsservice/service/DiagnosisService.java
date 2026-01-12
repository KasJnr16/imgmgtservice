
package com.pm.analyticsservice.service;

import com.pm.analyticsservice.dto.CreateDiagnosisDTO;
import com.pm.analyticsservice.model.Diagnosis;
import com.pm.analyticsservice.model.DiagnosisImage;
import com.pm.analyticsservice.model.DiagnosisStatus;
import com.pm.analyticsservice.model.MedicalRecord;
import com.pm.analyticsservice.repository.DiagnosisImageRepository;
import com.pm.analyticsservice.repository.DiagnosisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DiagnosisService {
    
    private final DiagnosisRepository diagnosisRepository;
    private final DiagnosisImageRepository imageRepository;
    private final MedicalRecordService medicalRecordService;
    
    public DiagnosisService(
        DiagnosisRepository diagnosisRepository,
        DiagnosisImageRepository imageRepository,
        MedicalRecordService medicalRecordService
    ) {
        this.diagnosisRepository = diagnosisRepository;
        this.imageRepository = imageRepository;
        this.medicalRecordService = medicalRecordService;
    }
    
    @Transactional
    public Diagnosis createDiagnosis(CreateDiagnosisDTO dto) {
        MedicalRecord record = medicalRecordService.getRecordById(dto.getMedicalRecordId());
        
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setMedicalRecord(record);
        diagnosis.setDiagnosedByStaffId(dto.getDiagnosedByStaffId());
        diagnosis.setDiseaseName(dto.getDiseaseName());
        diagnosis.setIcdCode(dto.getIcdCode());
        diagnosis.setDescription(dto.getDescription());
        diagnosis.setSeverity(dto.getSeverity());
        diagnosis.setStatus(dto.getStatus());
        diagnosis.setTreatmentPlan(dto.getTreatmentPlan());
        diagnosis.setNotes(dto.getNotes());
        
        Diagnosis savedDiagnosis = diagnosisRepository.save(diagnosis);
        
        // Link images if provided
        if (dto.getImageIds() != null && !dto.getImageIds().isEmpty()) {
            for (UUID imageId : dto.getImageIds()) {
                DiagnosisImage diagnosisImage = new DiagnosisImage();
                diagnosisImage.setDiagnosis(savedDiagnosis);
                diagnosisImage.setImageId(imageId);
                diagnosisImage.setImageType("DIAGNOSTIC"); // Could be enhanced
                imageRepository.save(diagnosisImage);
            }
        }
        
        return savedDiagnosis;
    }
    
    public List<Diagnosis> getPatientDiagnoses(UUID patientId) {
        return diagnosisRepository.findByMedicalRecord_PatientIdOrderByDiagnosisDateDesc(patientId);
    }
    
    public List<Diagnosis> getActiveDiagnoses(UUID patientId) {
        return diagnosisRepository.findByMedicalRecord_PatientIdAndStatus(
            patientId, DiagnosisStatus.ACTIVE);
    }
    
    public List<Diagnosis> getDiagnosesByDateRange(UUID patientId, LocalDateTime start, LocalDateTime end) {
        return diagnosisRepository.findByMedicalRecord_PatientIdAndDiagnosisDateBetween(
            patientId, start, end);
    }
    
    public Diagnosis updateDiagnosisStatus(UUID diagnosisId, DiagnosisStatus newStatus) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
            .orElseThrow(() -> new IllegalArgumentException("Diagnosis not found: " + diagnosisId));
        
        diagnosis.setStatus(newStatus);
        return diagnosisRepository.save(diagnosis);
    }
}
