package com.pm.medicalstaffservice.controller;

import com.pm.medicalstaffservice.dto.MedicalStaffRequestDTO;
import com.pm.medicalstaffservice.dto.MedicalStaffResponseDTO;
import com.pm.medicalstaffservice.dto.validators.CreateMedicalStaffValidationGroup;
import com.pm.medicalstaffservice.service.MedicalStaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medical-staff")
@Tag(name = "Medical Staff", description = "API for managing Medical Staff")
public class MedicalStaffController {

  private final MedicalStaffService medicalStaffService;

  public MedicalStaffController(MedicalStaffService medicalStaffService) {
    this.medicalStaffService = medicalStaffService;
  }

  @GetMapping
  @Operation(summary = "Get Medical Staff")
  public ResponseEntity<List<MedicalStaffResponseDTO>> getMedicalStaff() {
    List<MedicalStaffResponseDTO> medicalStaff =
        medicalStaffService.getMedicalStaff();
    return ResponseEntity.ok().body(medicalStaff);
  }

  @PostMapping
  @Operation(summary = "Create a new Medical Staff")
  public ResponseEntity<MedicalStaffResponseDTO> createMedicalStaff(
      @Validated({Default.class, CreateMedicalStaffValidationGroup.class})
      @RequestBody MedicalStaffRequestDTO medicalStaffRequestDTO) {

    MedicalStaffResponseDTO medicalStaffResponseDTO =
        medicalStaffService.registerUserInAuthService(medicalStaffRequestDTO);

    return ResponseEntity.ok().body(medicalStaffResponseDTO);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update Medical Staff")
  public ResponseEntity<MedicalStaffResponseDTO> updateMedicalStaff(
      @PathVariable UUID id,
      @Validated({Default.class})
      @RequestBody MedicalStaffRequestDTO medicalStaffRequestDTO) {

    MedicalStaffResponseDTO medicalStaffResponseDTO =
        medicalStaffService.updateMedicalStaff(id, medicalStaffRequestDTO);

    return ResponseEntity.ok().body(medicalStaffResponseDTO);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete Medical Staff")
  public ResponseEntity<Void> deleteMedicalStaff(@PathVariable UUID id) {
    medicalStaffService.deleteMedicalStaff(id);
    return ResponseEntity.noContent().build();
  }
}
