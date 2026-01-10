package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pm.billingservice.service.BillingAccountService;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    private final BillingAccountService billingAccountService;

    public BillingGrpcService(BillingAccountService billingAccountService) {
        this.billingAccountService = billingAccountService;
    }

  private static final Logger log = LoggerFactory.getLogger(
      BillingGrpcService.class);

  @Override
  public void createBillingAccount(BillingRequest billingRequest,
      StreamObserver<BillingResponse> responseObserver) {

      log.info("createBillingAccount request received {}", billingRequest.toString());

      var billingResponse = billingAccountService.createAccount(UUID.fromString(billingRequest.getPatientId()), billingRequest.getName());

      BillingResponse response = BillingResponse.newBuilder()
          .setAccountId(billingResponse.getId().toString())
          .setStatus("CREATED")
          .build();

      responseObserver.onNext(response);
      responseObserver.onCompleted();
  }
}
