package com.pm.medicalstaffservice.grpc;

import authservice.AuthServiceGrpc;
import authservice.AuthServiceRequest;
import authservice.AuthServiceResponse;
import com.pm.medicalstaffservice.dto.MedicalStaffRequestDTO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceGrpcClient {

  private static final Logger log =
      LoggerFactory.getLogger(AuthServiceGrpcClient.class);

  private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

  public AuthServiceGrpcClient(
      @Value("${auth.service.address:localhost}") String serverAddress,
      @Value("${auth.service.grpc.port:9003}") int serverPort) {

    log.info("Connecting to Auth Service GRPC service at {}:{}",
        serverAddress, serverPort);

    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(serverAddress, serverPort)
        .usePlaintext()
        .build();

    this.blockingStub = AuthServiceGrpc.newBlockingStub(channel);
  }

  public AuthServiceResponse createMedicalStaff(
      MedicalStaffRequestDTO dto) {

    AuthServiceRequest request = AuthServiceRequest.newBuilder()
        .setEmail(dto.getEmail())
        .setPassword(dto.getPassword())
        .setRole(dto.getRole())
        .build();

    AuthServiceResponse response =
        blockingStub.createMedicalStaff(request);

    log.info("Received response from auth service via GRPC: {}", response);
    return response;
  }
}
