package org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.charitable.app.proto.*;

@Singleton
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        if ("user".equals(username) && "pass".equals(password)) {
            LoginResponse response = LoginResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Login successful")
                    .build();
            responseObserver.onNext(response);
        } else {
            LoginResponse response = LoginResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Invalid credentials")
                    .build();
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        RegisterResponse response = RegisterResponse.newBuilder()
                .setSuccess(true)
                .setMessage("User registered successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void logout(LogoutRequest request, @NotNull StreamObserver<LogoutResponse> responseObserver) {
        LogoutResponse response = LogoutResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Logout successful")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
