package com.pm.patientservice.grpc;

import patient.PatientServiceGrpc.PatientServiceImplBase;
import patient.PatientRequest;
import patient.PatientResponse;
import java.util.UUID;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.service.PatientService;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class PatientGrpcService extends PatientServiceImplBase {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
            .getLogger(PatientGrpcService.class);
    private final PatientService patientService;

    public PatientGrpcService(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public void createPatientAccount(
            PatientRequest request,
            StreamObserver<PatientResponse> responseObserver) {

        String idString = request.getId();
        UUID id = UUID.fromString(idString);
        String name = request.getName();
        String email = request.getEmail();
        logger.info("Received gRPC request to create patient account: Name={}, Email={}, AuthUserId={}",
                name, email, id);

        PatientRequestDTO patientRequestDTO = new PatientRequestDTO();

        patientRequestDTO.setId(id);
        patientRequestDTO.setName(name);
        patientRequestDTO.setEmail(email);
        patientRequestDTO.setAddress(request.getAddress());
        patientRequestDTO.setDateOfBirth(request.getDateOfBirth());
        patientRequestDTO.setRegisteredDate(request.getRegisteredDate());

        patientService.createPatient(patientRequestDTO);

        PatientResponse response = PatientResponse.newBuilder()
                .setEmail(patientRequestDTO.getEmail())
                .setStatus("SUCCESS")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
