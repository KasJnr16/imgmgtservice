package com.pm.authservice.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.Optional;

import authservice.AuthServiceGrpc.AuthServiceImplBase;
import authservice.AuthServiceRequest;
import authservice.AuthServiceResponse;
import com.pm.authservice.model.User;
import com.pm.authservice.service.AuthService;

@GrpcService
public class AuthGrpcService extends AuthServiceImplBase {

    private final AuthService authService;

    public AuthGrpcService(
            AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void createMedicalStaff(
            AuthServiceRequest request, StreamObserver<AuthServiceResponse> responseObserver) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        Optional<User> OptionalUser = authService.save(user);
        if (OptionalUser.get() == null) {
            AuthServiceResponse.Builder responseBuilder = AuthServiceResponse.newBuilder()
                    .setId("")
                    .setStatus("FAILED");
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();

        } else {
            var newUser = OptionalUser.get();

            AuthServiceResponse.Builder responseBuilder = AuthServiceResponse.newBuilder()
                    .setId(newUser.getId().toString())
                    .setStatus("SUCCESS");

            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
        }

    }
}
