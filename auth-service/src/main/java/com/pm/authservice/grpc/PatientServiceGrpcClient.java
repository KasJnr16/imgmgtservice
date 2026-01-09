package com.pm.authservice.grpc;

import patient.PatientRequest;
import patient.PatientResponse;
import patient.PatientServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pm.authservice.dto.SignupRequestDTO;

@Service
public class PatientServiceGrpcClient {

  private static final Logger log = LoggerFactory.getLogger(
      PatientServiceGrpcClient.class);
  
  private final PatientServiceGrpc.PatientServiceBlockingStub blockingStub;

  public PatientServiceGrpcClient(
      @Value("${patient.service.address:localhost}") String serverAddress,
      @Value("${patient.service.grpc.port:9002}") int serverPort) {

    log.info("Connecting to Patient Service GRPC service at {}:{}",
        serverAddress, serverPort);

    ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,
        serverPort).usePlaintext().build();

    blockingStub = PatientServiceGrpc.newBlockingStub(channel);
  }

  public PatientResponse createPatientAccount(SignupRequestDTO signupRequestDTO, String patientId) {

    PatientRequest request = PatientRequest.newBuilder()
        .setId(patientId)
        .setName(signupRequestDTO.getName())
        .setEmail(signupRequestDTO.getEmail())
        .setAddress(signupRequestDTO.getAddress())
        .setDateOfBirth(signupRequestDTO.getDateOfBirth().toString())
        .setRegisteredDate(signupRequestDTO.getRegisteredDate().toString())
        .build();

    PatientResponse response = blockingStub.createPatientAccount(request);
    log.info("Received response from patient service via GRPC: {}", response.getStatus());
    return response;
  }
}
