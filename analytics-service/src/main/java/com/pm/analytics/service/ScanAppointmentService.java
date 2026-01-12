package com.pm.analytics.service;

import com.pm.analytics.dto.ScanAppointmentDTO;
import com.pm.analytics.dto.ScanRequestDTO;
import com.pm.analytics.dto.ScanUpdateDTO;
import com.pm.analytics.model.ScanAppointment;
import com.pm.analytics.model.enums.AppointmentStatus;
import com.pm.analytics.repository.ScanAppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ScanAppointmentService {

    @Autowired
    private ScanAppointmentRepository scanAppointmentRepository;

    @Autowired
    private MedicalStaffService medicalStaffService;

    @Autowired
    private BillingServiceClient billingServiceClient;

    @Value("${scan.fee.amount:150.00}")
    private Double defaultScanFee;

    public ScanAppointmentDTO createScanRequest(ScanRequestDTO request) {
        // Validate that the medical record exists and belongs to the patient
        // This would typically involve checking with the medical record service
        
        ScanAppointment appointment = new ScanAppointment(
            request.getMedicalRecordId(),
            request.getPatientId(),
            request.getDoctorId(),
            request.getScanType(),
            request.getScanReason()
        );
        
        appointment.setNotes(request.getNotes());
        
        ScanAppointment saved = scanAppointmentRepository.save(appointment);
        
        // Create billing charge for the scan
        try {
            String billingChargeId = billingServiceClient.createScanCharge(
                saved.getPatientId(),
                saved.getId().toString(),
                saved.getScanType().name(),
                defaultScanFee
            );
            saved.setBillingChargeId(billingChargeId);
            saved.setFeeAmount(defaultScanFee);
            saved = scanAppointmentRepository.save(saved);
        } catch (Exception e) {
            // Log error but don't fail the scan request
            System.err.println("Failed to create billing charge for scan: " + e.getMessage());
        }
        
        return convertToDTO(saved);
    }

    public ScanAppointmentDTO updateAppointment(UUID id, ScanUpdateDTO update, String userId) {
        ScanAppointment appointment = scanAppointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Scan appointment not found"));

        // Validate permissions - only doctor, radiologist, or assigned staff can update
        validateUpdatePermission(appointment, userId);

        // Update status
        if (update.getStatus() != null) {
            appointment.setStatus(update.getStatus());
            
            // Set timestamps based on status
            switch (update.getStatus()) {
                case SCHEDULED:
                    if (appointment.getScheduledAt() == null) {
                        appointment.setScheduledAt(LocalDateTime.now());
                    }
                    break;
                case IN_PROGRESS:
                    // Radiologist is now assigned
                    if (update.getRadiologistId() != null) {
                        appointment.setRadiologistId(update.getRadiologistId());
                    }
                    break;
                case COMPLETED:
                    if (appointment.getCompletedAt() == null) {
                        appointment.setCompletedAt(LocalDateTime.now());
                    }
                    break;
                case REQUESTED:
                case CANCELLED:
                case REJECTED:
                    // No specific timestamp updates needed for these statuses
                    break;
            }
        }

        // Update notes
        if (update.getNotes() != null) {
            appointment.setNotes(update.getNotes());
        }

        // Update radiologist
        if (update.getRadiologistId() != null) {
            appointment.setRadiologistId(update.getRadiologistId());
        }

        // Update image file path
        if (update.getImageFilePath() != null) {
            appointment.setImageFilePath(update.getImageFilePath());
            appointment.setImageUploadedAt(LocalDateTime.now());
            
            // If image is uploaded, mark as completed if not already
            if (appointment.getStatus() == AppointmentStatus.IN_PROGRESS) {
                appointment.setStatus(AppointmentStatus.COMPLETED);
                appointment.setCompletedAt(LocalDateTime.now());
            }
        }

        ScanAppointment saved = scanAppointmentRepository.save(appointment);
        return convertToDTO(saved);
    }

    public ScanAppointmentDTO uploadScanImage(UUID id, String imageFilePath, String radiologistId) {
        ScanAppointment appointment = scanAppointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Scan appointment not found"));

        // Validate that the radiologist is assigned or can self-assign
        if (appointment.getRadiologistId() == null) {
            appointment.setRadiologistId(radiologistId);
        } else if (!appointment.getRadiologistId().equals(radiologistId)) {
            throw new RuntimeException("Only assigned radiologist can upload images");
        }

        appointment.setImageFilePath(imageFilePath);
        appointment.setImageUploadedAt(LocalDateTime.now());
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setCompletedAt(LocalDateTime.now());

        ScanAppointment saved = scanAppointmentRepository.save(appointment);
        return convertToDTO(saved);
    }

    public List<ScanAppointmentDTO> getAppointmentsForPatient(String patientId) {
        List<ScanAppointment> appointments = scanAppointmentRepository.findByPatientIdOrderByRequestedAtDesc(patientId);
        return appointments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ScanAppointmentDTO> getAppointmentsForDoctor(String doctorId) {
        List<ScanAppointment> appointments = scanAppointmentRepository.findByDoctorIdOrderByRequestedAtDesc(doctorId);
        return appointments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ScanAppointmentDTO> getAppointmentsForRadiologist(String radiologistId) {
        List<ScanAppointment> appointments = scanAppointmentRepository.findByRadiologistIdOrderByRequestedAtDesc(radiologistId);
        return appointments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ScanAppointmentDTO> getUnassignedRequestedScans() {
        List<ScanAppointment> appointments = scanAppointmentRepository.findUnassignedRequestedScans();
        return appointments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<ScanAppointmentDTO> getUpcomingAppointmentsForRadiologist(String radiologistId) {
        List<ScanAppointment> appointments = scanAppointmentRepository.findUpcomingAppointmentsForRadiologist(radiologistId);
        return appointments.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public ScanAppointmentDTO getAppointmentById(UUID id) {
        ScanAppointment appointment = scanAppointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Scan appointment not found"));
        return convertToDTO(appointment);
    }

    public void cancelAppointment(UUID id, String userId) {
        ScanAppointment appointment = scanAppointmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Scan appointment not found"));

        validateUpdatePermission(appointment, userId);

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        scanAppointmentRepository.save(appointment);
    }

    private void validateUpdatePermission(ScanAppointment appointment, String userId) {
        // Allow if user is the requesting doctor
        if (appointment.getDoctorId().equals(userId)) {
            return;
        }

        // Allow if user is the assigned radiologist
        if (appointment.getRadiologistId() != null && appointment.getRadiologistId().equals(userId)) {
            return;
        }

        // Allow if user is the patient (for viewing only)
        if (appointment.getPatientId().equals(userId)) {
            return;
        }

        throw new RuntimeException("Unauthorized to update this appointment");
    }

    private ScanAppointmentDTO convertToDTO(ScanAppointment appointment) {
        ScanAppointmentDTO dto = new ScanAppointmentDTO();
        dto.setId(appointment.getId());
        dto.setMedicalRecordId(appointment.getMedicalRecordId());
        dto.setPatientId(appointment.getPatientId());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setRadiologistId(appointment.getRadiologistId());
        dto.setScanType(appointment.getScanType());
        dto.setScanReason(appointment.getScanReason());
        dto.setStatus(appointment.getStatus());
        dto.setRequestedAt(appointment.getRequestedAt());
        dto.setScheduledAt(appointment.getScheduledAt());
        dto.setCompletedAt(appointment.getCompletedAt());
        dto.setImageFilePath(appointment.getImageFilePath());
        dto.setImageUploadedAt(appointment.getImageUploadedAt());
        dto.setNotes(appointment.getNotes());
        dto.setFeeAmount(appointment.getFeeAmount());
        dto.setBillingChargeId(appointment.getBillingChargeId());

        // Load staff names
        try {
            Map<String, String> staffNames = medicalStaffService.getStaffNames(
                List.of(appointment.getPatientId(), appointment.getDoctorId(), appointment.getRadiologistId())
            );
            
            dto.setPatientName(staffNames.get(appointment.getPatientId()));
            dto.setDoctorName(staffNames.get(appointment.getDoctorId()));
            dto.setRadiologistName(staffNames.get(appointment.getRadiologistId()));
        } catch (Exception e) {
            // Continue without names if service fails
        }

        return dto;
    }
}
