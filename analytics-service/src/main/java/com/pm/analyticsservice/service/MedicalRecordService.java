
package com.pm.analyticsservice.service;

import com.pm.analyticsservice.dto.CreateMedicalRecordDTO;
import com.pm.analyticsservice.model.MedicalRecord;
import com.pm.analyticsservice.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class MedicalRecordService {
    
    private final MedicalRecordRepository repository;
    
    public MedicalRecordService(MedicalRecordRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public MedicalRecord createMedicalRecord(CreateMedicalRecordDTO dto) {
        MedicalRecord record = new MedicalRecord();
        record.setPatientId(dto.getPatientId());
        record.setCreatedByStaffId(dto.getCreatedByStaffId());
        record.setChiefComplaint(dto.getChiefComplaint());
        record.setHistoryOfPresentIllness(dto.getHistoryOfPresentIllness());
        record.setPastMedicalHistory(dto.getPastMedicalHistory());
        record.setMedications(dto.getMedications());
        record.setAllergies(dto.getAllergies());
        
        // Vital Signs
        record.setTemperature(dto.getTemperature());
        record.setBloodPressureSystolic(dto.getBloodPressureSystolic());
        record.setBloodPressureDiastolic(dto.getBloodPressureDiastolic());
        record.setHeartRate(dto.getHeartRate());
        record.setRespiratoryRate(dto.getRespiratoryRate());
        record.setOxygenSaturation(dto.getOxygenSaturation());
        
        return repository.save(record);
    }
    
    public List<MedicalRecord> getPatientRecords(UUID patientId) {
        return repository.findByPatientIdOrderByRecordDateDesc(patientId);
    }
    
    public MedicalRecord getRecordById(UUID recordId) {
        return repository.findById(recordId)
            .orElseThrow(() -> new IllegalArgumentException("Medical record not found: " + recordId));
    }
    
    public List<MedicalRecord> getRecordsByDateRange(UUID patientId, LocalDateTime start, LocalDateTime end) {
        return repository.findByPatientIdAndRecordDateBetween(patientId, start, end);
    }
}
