package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.Diagnosis;
import com.pm.analyticsservice.model.DiagnosisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, UUID> {
    List<Diagnosis> findByMedicalRecord_PatientIdOrderByDiagnosisDateDesc(UUID patientId);
    List<Diagnosis> findByMedicalRecord_PatientIdAndStatus(UUID patientId, DiagnosisStatus status);
    List<Diagnosis> findByMedicalRecord_PatientIdAndDiagnosisDateBetween(UUID patientId, LocalDateTime start, LocalDateTime end);
}