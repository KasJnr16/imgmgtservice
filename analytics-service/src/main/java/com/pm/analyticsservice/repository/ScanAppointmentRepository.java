package com.pm.analyticsservice.repository;

import com.pm.analyticsservice.model.ScanAppointment;
import com.pm.analyticsservice.model.enums.AppointmentStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScanAppointmentRepository extends JpaRepository<ScanAppointment, UUID> {
    
    List<ScanAppointment> findByPatientIdOrderByRequestedAtDesc(String patientId);
    
    List<ScanAppointment> findByDoctorIdOrderByRequestedAtDesc(String doctorId);
    
    List<ScanAppointment> findByRadiologistIdOrderByRequestedAtDesc(String radiologistId);
    
    List<ScanAppointment> findByStatusOrderByRequestedAtDesc(AppointmentStatus status);
    
    List<ScanAppointment> findByMedicalRecordId(String medicalRecordId);
    
    @Query("SELECT s FROM ScanAppointment s WHERE s.status = :status ORDER BY s.requestedAt ASC")
    List<ScanAppointment> findByStatusOrderByRequestedAtAsc(@Param("status") AppointmentStatus status);
    
    @Query("SELECT s FROM ScanAppointment s WHERE s.radiologistId IS NULL AND s.status = 'REQUESTED' ORDER BY s.requestedAt ASC")
    List<ScanAppointment> findUnassignedRequestedScans();
    
    @Query("SELECT s FROM ScanAppointment s WHERE s.radiologistId = :radiologistId AND s.status IN ('SCHEDULED', 'IN_PROGRESS') ORDER BY s.scheduledAt ASC")
    List<ScanAppointment> findUpcomingAppointmentsForRadiologist(@Param("radiologistId") String radiologistId);
    
    @Query("SELECT s FROM ScanAppointment s WHERE s.patientId = :patientId AND s.status != 'CANCELLED' ORDER BY s.requestedAt DESC")
    List<ScanAppointment> findActiveAppointmentsForPatient(@Param("patientId") String patientId);
    
    @Query("SELECT s FROM ScanAppointment s WHERE s.requestedAt BETWEEN :startDate AND :endDate ORDER BY s.requestedAt DESC")
    List<ScanAppointment> findByRequestedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    Optional<ScanAppointment> findByIdAndPatientId(UUID id, String patientId);
    
    Optional<ScanAppointment> findByIdAndDoctorId(UUID id, String doctorId);
    
    Optional<ScanAppointment> findByIdAndRadiologistId(UUID id, String radiologistId);
}
