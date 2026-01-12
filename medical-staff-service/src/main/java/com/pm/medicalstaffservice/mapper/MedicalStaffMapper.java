package com.pm.medicalstaffservice.mapper;

import com.pm.medicalstaffservice.dto.MedicalStaffRequestDTO;
import com.pm.medicalstaffservice.dto.MedicalStaffResponseDTO;
import com.pm.medicalstaffservice.model.MedicalStaff;

import java.time.LocalDate;

public class MedicalStaffMapper {

    public static MedicalStaffResponseDTO toDTO(MedicalStaff medicalStaff) {
        if (medicalStaff == null) {
            return null;
        }

        MedicalStaffResponseDTO dto = new MedicalStaffResponseDTO();
        dto.setId(
                medicalStaff.getId() != null
                        ? medicalStaff.getId().toString()
                        : null
        );
        dto.setName(medicalStaff.getName());
        dto.setAddress(medicalStaff.getAddress());
        dto.setEmail(medicalStaff.getEmail());
        dto.setDateOfBirth(
                medicalStaff.getDateOfBirth() != null
                        ? medicalStaff.getDateOfBirth().toString()
                        : null
        );

        return dto;
    }

    public static MedicalStaff toModel(MedicalStaffRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        MedicalStaff medicalStaff = new MedicalStaff();
        medicalStaff.setId(requestDTO.getId());
        medicalStaff.setName(requestDTO.getName());
        medicalStaff.setAddress(requestDTO.getAddress());
        medicalStaff.setEmail(requestDTO.getEmail());
        medicalStaff.setDateOfBirth(LocalDate.parse(requestDTO.getDateOfBirth()));
        medicalStaff.setRegisteredDate(LocalDate.parse(requestDTO.getRegisteredDate()));

        return medicalStaff;
    }
}
