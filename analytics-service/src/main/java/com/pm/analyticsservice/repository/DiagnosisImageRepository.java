package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.DiagnosisImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DiagnosisImageRepository extends JpaRepository<DiagnosisImage, UUID> {
    List<DiagnosisImage> findByDiagnosisId(UUID diagnosisId);
}