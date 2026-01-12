package com.pm.analytics.controller;

import com.pm.analytics.dto.ScanAppointmentDTO;
import com.pm.analytics.dto.ScanRequestDTO;
import com.pm.analytics.dto.ScanUpdateDTO;
import com.pm.analytics.service.ScanAppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics/scan-appointments")
@CrossOrigin(origins = "*")
public class ScanAppointmentController {

    @Autowired
    private ScanAppointmentService scanAppointmentService;

    @PostMapping("/request")
    public ResponseEntity<ScanAppointmentDTO> requestScan(@Valid @RequestBody ScanRequestDTO request) {
        ScanAppointmentDTO appointment = scanAppointmentService.createScanRequest(request);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScanAppointmentDTO> getAppointment(@PathVariable UUID id) {
        ScanAppointmentDTO appointment = scanAppointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScanAppointmentDTO> updateAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody ScanUpdateDTO update,
            @RequestParam String userId) {
        ScanAppointmentDTO appointment = scanAppointmentService.updateAppointment(id, update, userId);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ScanAppointmentDTO> uploadScanImage(
            @PathVariable UUID id,
            @RequestParam String imageFilePath,
            @RequestParam String radiologistId) {
        ScanAppointmentDTO appointment = scanAppointmentService.uploadScanImage(id, imageFilePath, radiologistId);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelAppointment(@PathVariable UUID id, @RequestParam String userId) {
        scanAppointmentService.cancelAppointment(id, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<ScanAppointmentDTO>> getAppointmentsForPatient(@PathVariable String patientId) {
        List<ScanAppointmentDTO> appointments = scanAppointmentService.getAppointmentsForPatient(patientId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ScanAppointmentDTO>> getAppointmentsForDoctor(@PathVariable String doctorId) {
        List<ScanAppointmentDTO> appointments = scanAppointmentService.getAppointmentsForDoctor(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/radiologist/{radiologistId}")
    public ResponseEntity<List<ScanAppointmentDTO>> getAppointmentsForRadiologist(@PathVariable String radiologistId) {
        List<ScanAppointmentDTO> appointments = scanAppointmentService.getAppointmentsForRadiologist(radiologistId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<ScanAppointmentDTO>> getUnassignedRequestedScans() {
        List<ScanAppointmentDTO> appointments = scanAppointmentService.getUnassignedRequestedScans();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/radiologist/{radiologistId}/upcoming")
    public ResponseEntity<List<ScanAppointmentDTO>> getUpcomingAppointmentsForRadiologist(@PathVariable String radiologistId) {
        List<ScanAppointmentDTO> appointments = scanAppointmentService.getUpcomingAppointmentsForRadiologist(radiologistId);
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/{id}/assign-radiologist")
    public ResponseEntity<ScanAppointmentDTO> assignRadiologist(
            @PathVariable UUID id,
            @RequestParam String radiologistId) {
        ScanUpdateDTO update = new ScanUpdateDTO();
        update.setStatus(com.pm.analytics.model.enums.AppointmentStatus.IN_PROGRESS);
        update.setRadiologistId(radiologistId);
        
        ScanAppointmentDTO appointment = scanAppointmentService.updateAppointment(id, update, radiologistId);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/{id}/schedule")
    public ResponseEntity<ScanAppointmentDTO> scheduleAppointment(
            @PathVariable UUID id,
            @RequestParam String radiologistId) {
        ScanUpdateDTO update = new ScanUpdateDTO();
        update.setStatus(com.pm.analytics.model.enums.AppointmentStatus.SCHEDULED);
        update.setRadiologistId(radiologistId);
        
        ScanAppointmentDTO appointment = scanAppointmentService.updateAppointment(id, update, radiologistId);
        return ResponseEntity.ok(appointment);
    }
}
