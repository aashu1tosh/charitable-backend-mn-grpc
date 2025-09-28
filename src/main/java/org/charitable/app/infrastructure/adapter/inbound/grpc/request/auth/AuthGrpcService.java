package org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    private final AuthUseCase authUseCase;

    public AuthGrpcService(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<CommonResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        var dto = new LoginRequestDTO(request.getUsername(), request.getPassword());
        var result = authUseCase.login(dto);

        var response = CommonResponse.newBuilder()
                .setSuccess(result.isSuccess())
                .setMessage(result.getMessage())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<CommonResponse> responseObserver) {

        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("User registered successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void logout(LogoutRequest request, @NotNull StreamObserver<CommonResponse> responseObserver) {
        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Logout successful")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
