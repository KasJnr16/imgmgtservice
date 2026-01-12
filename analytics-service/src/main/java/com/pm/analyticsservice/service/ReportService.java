
package com.pm.analyticsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.analyticsservice.dto.GenerateReportDTO;
import com.pm.analyticsservice.model.*;
import com.pm.analyticsservice.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class ReportService {
    
    private final ReportRepository reportRepository;
    private final MedicalRecordService medicalRecordService;
    private final DiagnosisService diagnosisService;
    private final ObjectMapper objectMapper;
    
    public ReportService(
        ReportRepository reportRepository,
        MedicalRecordService medicalRecordService,
        DiagnosisService diagnosisService,
        ObjectMapper objectMapper
    ) {
        this.reportRepository = reportRepository;
        this.medicalRecordService = medicalRecordService;
        this.diagnosisService = diagnosisService;
        this.objectMapper = objectMapper;
    }
    
    @Transactional
    public Report generateReport(GenerateReportDTO dto) throws Exception {
        Report report = new Report();
        report.setPatientId(dto.getPatientId());
        report.setGeneratedByStaffId(dto.getGeneratedByStaffId());
        report.setReportType(dto.getReportType());
        report.setStartDate(dto.getStartDate());
        report.setEndDate(dto.getEndDate());
        report.setFormat(dto.getFormat());
        
        String content = generateReportContent(dto);
        report.setReportContent(content);
        
        return reportRepository.save(report);
    }
    
    private String generateReportContent(GenerateReportDTO dto) throws Exception {
        Map<String, Object> reportData = new HashMap<>();
        
        switch (dto.getReportType()) {
            case PATIENT_SUMMARY:
                reportData = generatePatientSummary(dto.getPatientId());
                break;
            case DIAGNOSIS_HISTORY:
                reportData = generateDiagnosisHistory(dto.getPatientId(), dto.getStartDate(), dto.getEndDate());
                break;
            case TREATMENT_PROGRESS:
                reportData = generateTreatmentProgress(dto.getPatientId(), dto.getStartDate(), dto.getEndDate());
                break;
            case COMPREHENSIVE:
                reportData = generateComprehensiveReport(dto.getPatientId(), dto.getStartDate(), dto.getEndDate());
                break;
        }
        
        return objectMapper.writeValueAsString(reportData);
    }
    
    private Map<String, Object> generatePatientSummary(UUID patientId) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("patientId", patientId);
        
        List<MedicalRecord> records = medicalRecordService.getPatientRecords(patientId);
        summary.put("totalVisits", records.size());
        summary.put("recentRecords", records.stream().limit(5).toList());
        
        List<Diagnosis> diagnoses = diagnosisService.getPatientDiagnoses(patientId);
        summary.put("totalDiagnoses", diagnoses.size());
        
        List<Diagnosis> activeDiagnoses = diagnosisService.getActiveDiagnoses(patientId);
        summary.put("activeDiagnoses", activeDiagnoses);
        
        return summary;
    }
    
    private Map<String, Object> generateDiagnosisHistory(UUID patientId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        Map<String, Object> history = new HashMap<>();
        history.put("patientId", patientId);
        history.put("period", Map.of("start", start, "end", end));
        
        List<Diagnosis> diagnoses = diagnosisService.getDiagnosesByDateRange(patientId, start, end);
        history.put("diagnoses", diagnoses);
        history.put("count", diagnoses.size());
        
        return history;
    }
    
    private Map<String, Object> generateTreatmentProgress(UUID patientId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        Map<String, Object> progress = new HashMap<>();
        progress.put("patientId", patientId);
        progress.put("period", Map.of("start", start, "end", end));
        
        List<MedicalRecord> records = medicalRecordService.getRecordsByDateRange(patientId, start, end);
        List<Diagnosis> diagnoses = diagnosisService.getDiagnosesByDateRange(patientId, start, end);
        
        progress.put("visits", records.size());
        progress.put("newDiagnoses", diagnoses.size());
        progress.put("resolvedCount", diagnoses.stream()
            .filter(d -> d.getStatus() == DiagnosisStatus.RESOLVED).count());
        
        return progress;
    }
    
    private Map<String, Object> generateComprehensiveReport(UUID patientId, java.time.LocalDateTime start, java.time.LocalDateTime end) {
        Map<String, Object> comprehensive = new HashMap<>();
        comprehensive.put("summary", generatePatientSummary(patientId));
        comprehensive.put("diagnosisHistory", generateDiagnosisHistory(patientId, start, end));
        comprehensive.put("treatmentProgress", generateTreatmentProgress(patientId, start, end));
        
        return comprehensive;
    }
    
    public List<Report> getPatientReports(UUID patientId) {
        return reportRepository.findByPatientIdOrderByGeneratedAtDesc(patientId);
    }
    
    public Report getReportById(UUID reportId) {
        return reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
    }
}