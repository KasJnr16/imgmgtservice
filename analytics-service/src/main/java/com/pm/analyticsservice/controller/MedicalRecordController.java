
package com.pm.analyticsservice.controller;

import com.pm.analyticsservice.dto.CreateMedicalRecordDTO;
import com.pm.analyticsservice.model.MedicalRecord;
import com.pm.analyticsservice.service.MedicalRecordService;
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
@RequestMapping("/analytics/medical-records")
public class MedicalRecordController {
    
    private final MedicalRecordService service;
    
    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }
    
    @PostMapping
    @Operation(summary = "Create a new medical record")
    public ResponseEntity<?> createRecord(@Valid @RequestBody CreateMedicalRecordDTO dto) {
        try {
            MedicalRecord record = service.createMedicalRecord(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create medical record: " + e.getMessage());
        }
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all medical records for a patient")
    public ResponseEntity<List<MedicalRecord>> getPatientRecords(@PathVariable UUID patientId) {
        return ResponseEntity.ok(service.getPatientRecords(patientId));
    }
    
    @GetMapping("/{recordId}")
    @Operation(summary = "Get a specific medical record")
    public ResponseEntity<?> getRecord(@PathVariable UUID recordId) {
        try {
            MedicalRecord record = service.getRecordById(recordId);
            return ResponseEntity.ok(record);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }
    
    @GetMapping("/patient/{patientId}/date-range")
    @Operation(summary = "Get medical records for a patient within a date range")
    public ResponseEntity<List<MedicalRecord>> getRecordsByDateRange(
        @PathVariable UUID patientId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return ResponseEntity.ok(service.getRecordsByDateRange(patientId, startDate, endDate));
    }
}