package com.pm.imageservice.repository;

import com.pm.imageservice.model.MedicalImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicalImageRepository extends JpaRepository<MedicalImage, UUID> {

    List<MedicalImage> findByPatientId(UUID patientId);
}
