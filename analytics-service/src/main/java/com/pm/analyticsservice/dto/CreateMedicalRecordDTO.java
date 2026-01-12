
package com.pm.analyticsservice.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateMedicalRecordDTO {
    private UUID patientId;
    private UUID createdByStaffId;
    private String chiefComplaint;
    private String historyOfPresentIllness;
    private String pastMedicalHistory;
    private String medications;
    private String allergies;
    
    // Vital Signs
    private Double temperature;
    private Integer bloodPressureSystolic;
    private Integer bloodPressureDiastolic;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Double oxygenSaturation;
}