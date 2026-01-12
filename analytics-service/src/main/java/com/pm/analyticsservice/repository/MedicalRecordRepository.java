
package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> {
    List<MedicalRecord> findByPatientIdOrderByRecordDateDesc(UUID patientId);
    List<MedicalRecord> findByPatientIdAndRecordDateBetween(UUID patientId, LocalDateTime start, LocalDateTime end);
}
