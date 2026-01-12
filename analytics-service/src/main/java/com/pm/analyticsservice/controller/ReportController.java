
package com.pm.analyticsservice.controller;

import com.pm.analyticsservice.dto.GenerateReportDTO;
import com.pm.analyticsservice.model.Report;
import com.pm.analyticsservice.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analytics/reports")
public class ReportController {
    
    private final ReportService service;
    
    public ReportController(ReportService service) {
        this.service = service;
    }
    
    @PostMapping("/generate")
    @Operation(summary = "Generate a medical report")
    public ResponseEntity<?> generateReport(@Valid @RequestBody GenerateReportDTO dto) {
        try {
            Report report = service.generateReport(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to generate report: " + e.getMessage());
        }
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get all reports for a patient")
    public ResponseEntity<List<Report>> getPatientReports(@PathVariable UUID patientId) {
        return ResponseEntity.ok(service.getPatientReports(patientId));
    }
    
    @GetMapping("/{reportId}")
    @Operation(summary = "Get a specific report")
    public ResponseEntity<?> getReport(@PathVariable UUID reportId) {
        try {
            Report report = service.getReportById(reportId);
            return ResponseEntity.ok(report);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        }
    }
}