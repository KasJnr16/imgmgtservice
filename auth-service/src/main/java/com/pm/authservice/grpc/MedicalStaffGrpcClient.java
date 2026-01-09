package com.pm.authservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import medicalstaff.MedicalStaffRequest;
import medicalstaff.MedicalStaffResponse;
import medicalstaff.MedicalStaffServiceGrpc;
import com.pm.authservice.dto.SignupRequestDTO;

@Service
public class MedicalStaffGrpcClient {

    private static final Logger log = LoggerFactory.getLogger(MedicalStaffGrpcClient.class);

    private final MedicalStaffServiceGrpc.MedicalStaffServiceBlockingStub blockingStub;

    public MedicalStaffGrpcClient(
        @Value("${medical-staff.service.address:localhost}") String serverAddress,
        @Value("${medical-staff.service.grpc.port:9003}") int serverPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
            .usePlaintext()
            .build();
        blockingStub = MedicalStaffServiceGrpc.newBlockingStub(channel);
    }

    public MedicalStaffResponse createMedicalStaff(SignupRequestDTO requestDTO, String staffId) {
        MedicalStaffRequest request = MedicalStaffRequest.newBuilder()
            .setId(staffId)
            .setName(requestDTO.getName())
            .setEmail(requestDTO.getEmail())
            .setAddress(requestDTO.getAddress())
            .setDateOfBirth(requestDTO.getDateOfBirth().toString())
            .setRegisteredDate(requestDTO.getRegisteredDate().toString())
            .build();

        MedicalStaffResponse response = blockingStub.createMedicalStaff(request);
        log.info("Medical staff created via gRPC: {}", response);
        return response;
    }
}
