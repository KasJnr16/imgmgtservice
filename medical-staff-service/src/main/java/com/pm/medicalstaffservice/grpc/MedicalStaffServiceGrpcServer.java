package com.pm.medicalstaffservice.grpc;


import medicalstaff.MedicalStaffServiceGrpc.MedicalStaffServiceImplBase;
import medicalstaff.MedicalStaffRequest;
import medicalstaff.MedicalStaffResponse;

import java.util.UUID;

import com.pm.medicalstaffservice.dto.MedicalStaffRequestDTO;
import com.pm.medicalstaffservice.service.MedicalStaffService;

import io.grpc.stub.StreamObserver;

public class MedicalStaffServiceGrpcServer extends MedicalStaffServiceImplBase {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(MedicalStaffServiceGrpcServer.class);
    private final MedicalStaffService medicalStaffService;

    public MedicalStaffServiceGrpcServer(MedicalStaffService medicalStaffService) {
        this.medicalStaffService = medicalStaffService;
    }

    @Override
    public void createMedicalStaff(
            MedicalStaffRequest request,
            StreamObserver<MedicalStaffResponse> responseObserver) {

        String idString = request.getId();
        UUID id = UUID.fromString(idString);
        String name = request.getName();
        String email = request.getEmail();
        logger.info("Received gRPC request to create patient account: Name={}, Email={}, AuthUserId={}",
                name, email, id);

        MedicalStaffRequestDTO medicalStaffRequestDTO = new MedicalStaffRequestDTO();

        medicalStaffRequestDTO.setId(id);
        medicalStaffRequestDTO.setName(name);
        medicalStaffRequestDTO.setEmail(email);
        medicalStaffRequestDTO.setAddress(request.getAddress());
        medicalStaffRequestDTO.setDateOfBirth(request.getDateOfBirth());
        medicalStaffRequestDTO.setRegisteredDate(request.getRegisteredDate());
        
        var responseDTO = medicalStaffService.createMedicalStaff(medicalStaffRequestDTO);

        MedicalStaffResponse response = MedicalStaffResponse.newBuilder()
                .setId(responseDTO.getId().toString())
                .setName(responseDTO.getName())
                .setEmail(responseDTO.getEmail())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
