
package com.pm.analyticsservice.controller;

import com.pm.analyticsservice.dto.CreateDiagnosisDTO;
import com.pm.analyticsservice.model.Diagnosis;
import com.pm.analyticsservice.model.DiagnosisStatus;
import com.pm.analyticsservice.service.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analytics/diagnoses")
public class DiagnosisController {
    
    private final DiagnosisService service;
    
    public DiagnosisController(DiagnosisService service) {
        this.service = service;
    }
    
    @PostMapping
    @Operation(summary = "Create a new diagnosis")
    public ResponseEntity<?> createDiagnosis(@Valid @RequestBody CreateDiagnosisDTO dto) {
        try {
            Diagnosis diagnosis = service.createDiagnosis(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(diagnosis);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create diagnosis: " + e.getMessage());
        }
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all diagnoses for a patient")
    public ResponseEntity<List<Diagnosis>> getPatientDiagnoses(@PathVariable UUID patientId) {
        return ResponseEntity.ok(service.getPatientDiagnoses(patientId));
    }
    
    @GetMapping("/patient/{patientId}/active")
    @Operation(summary = "Get active diagnoses for a patient")
    public ResponseEntity<List<Diagnosis>> getActiveDiagnoses(@PathVariable UUID patientId) {
        return ResponseEntity.ok(service.getActiveDiagnoses(patientId));
    }
    
    @GetMapping("/patient/{patientId}/date-range")
    @Operation(summary = "Get diagnoses for a patient within a date range")
    public ResponseEntity<List<Diagnosis>> getDiagnosesByDateRange(
        @PathVariable UUID patientId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(service.getDiagnosesByDateRange(patientId, startDate, endDate));
    }
    
    @PatchMapping("/{diagnosisId}/status")
    @Operation(summary = "Update diagnosis status")
    public ResponseEntity<?> updateStatus(
        @PathVariable UUID diagnosisId,
        @RequestParam DiagnosisStatus status
    ) {
        try {
            Diagnosis diagnosis = service.updateDiagnosisStatus(diagnosisId, status);
            return ResponseEntity.ok(diagnosis);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }
}
