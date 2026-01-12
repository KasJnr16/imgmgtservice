package com.pm.medicalstaffservice.service;

import com.pm.medicalstaffservice.dto.MedicalStaffRequestDTO;
import com.pm.medicalstaffservice.dto.MedicalStaffResponseDTO;
import com.pm.medicalstaffservice.exception.EmailAlreadyExistsException;
import com.pm.medicalstaffservice.exception.MedicalStaffNotFoundException;
import com.pm.medicalstaffservice.grpc.AuthServiceGrpcClient;
// import com.pm.medicalstaffservice.kafka.KafkaProducer;
import com.pm.medicalstaffservice.mapper.MedicalStaffMapper;
import com.pm.medicalstaffservice.model.MedicalStaff;
import com.pm.medicalstaffservice.repository.MedicalStaffRepository;

import authservice.AuthServiceResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;

@Service
public class MedicalStaffService {

  private final MedicalStaffRepository medicalStaffRepository;
  private final AuthServiceGrpcClient
      authServiceGrpcClient;
  // private final KafkaProducer kafkaProducer;

  public MedicalStaffService(
      MedicalStaffRepository medicalStaffRepository
      , AuthServiceGrpcClient authServiceGrpcClient
      // KafkaProducer kafkaProducer
    ) {
    this.authServiceGrpcClient = authServiceGrpcClient;
    this.medicalStaffRepository = medicalStaffRepository;
    // this.kafkaProducer = kafkaProducer;
  }

  public List<MedicalStaffResponseDTO> getMedicalStaff() {
    List<MedicalStaff> medicalStaff = medicalStaffRepository.findAll();
    return medicalStaff.stream()
        .map(MedicalStaffMapper::toDTO)
        .toList();
  }

  public MedicalStaffResponseDTO registerUserInAuthService(MedicalStaffRequestDTO medicalStaffRequestDTO) {

    AuthServiceResponse  response = authServiceGrpcClient.createMedicalStaff(medicalStaffRequestDTO);
    if(response.getId().isEmpty() || "FAILED".equals(response.getStatus())){
      throw new RuntimeErrorException(null, "User registeration failed");
    }
    medicalStaffRequestDTO.setId(UUID.fromString(response.getId()));
    return createMedicalStaff(medicalStaffRequestDTO);

  }

  public MedicalStaffResponseDTO createMedicalStaff(
      MedicalStaffRequestDTO medicalStaffRequestDTO) {

    if (medicalStaffRepository.existsByEmail(
        medicalStaffRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException(
          "A medical staff with this email already exists "
              + medicalStaffRequestDTO.getEmail());
    }

    MedicalStaff newMedicalStaff = medicalStaffRepository.save(
        MedicalStaffMapper.toModel(medicalStaffRequestDTO));

    return MedicalStaffMapper.toDTO(newMedicalStaff);
  }

  public MedicalStaffResponseDTO updateMedicalStaff(
      UUID id,
      MedicalStaffRequestDTO medicalStaffRequestDTO) {

    MedicalStaff medicalStaff = medicalStaffRepository.findById(id)
        .orElseThrow(() -> new MedicalStaffNotFoundException(
            "Medical staff not found with ID: " + id));

    if (medicalStaffRepository.existsByEmailAndIdNot(
        medicalStaffRequestDTO.getEmail(), id)) {
      throw new EmailAlreadyExistsException(
          "A medical staff with this email already exists "
              + medicalStaffRequestDTO.getEmail());
    }

    medicalStaff.setName(medicalStaffRequestDTO.getName());
    medicalStaff.setAddress(medicalStaffRequestDTO.getAddress());
    medicalStaff.setEmail(medicalStaffRequestDTO.getEmail());
    medicalStaff.setDateOfBirth(
        LocalDate.parse(medicalStaffRequestDTO.getDateOfBirth()));

    MedicalStaff updatedMedicalStaff =
        medicalStaffRepository.save(medicalStaff);

    return MedicalStaffMapper.toDTO(updatedMedicalStaff);
  }

  public void deleteMedicalStaff(UUID id) {
    medicalStaffRepository.deleteById(id);
  }
}
