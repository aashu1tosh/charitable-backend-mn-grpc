package org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.auth.AuthStatus;
import org.charitable.app.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@Singleton
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    private final AuthUseCase authUseCase;
    private final Validator validator;

    public AuthGrpcService(AuthUseCase authUseCase, Validator validator) {
        this.authUseCase = authUseCase;
        this.validator = validator;
    }

    @Override
    public void login(LoginRequest request, StreamObserver<CommonResponse> responseObserver) {
        try {
            logger.info("Received login request for user: {}", request.getUsername());
            String username = request.getUsername();
            String password = request.getPassword();

            var dto = new LoginRequestDTO(request.getUsername(), request.getPassword());

            Set<ConstraintViolation<LoginRequestDTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                violations.forEach(v -> sb.append(v.getMessage()));
                var response = CommonResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage(sb.toString())
                        .build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }

            var result = authUseCase.login(dto);

            var response = CommonResponse.newBuilder()
                    .setSuccess(result.isSuccess())
                    .setMessage(result.getMessage())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            var response = CommonResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("An error occurred during login")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            logger.error("Logging failed", e);
        }
    }

    @Override
    public void registerOrganization(RegisterOrganizationRequest request, StreamObserver<CommonResponse> responseObserver) {
        logger.info("Received register organization request for organization: {}", request.getOrganizationName());
        String password = request.getPassword();
        String email = request.getEmail();
        String organizationName = request.getOrganizationName();
        String address = request.getAddress();
        String govtId = request.getGovtId();
        String contactNumber = request.getContactNumber();
        float latitude = request.getLatitude();
        float longitude = request.getLongitude();

        var auth = new AuthRegisterRequestDTO(
                email,
                password,
                contactNumber,
                Role.ORGANIZATION,
                AuthStatus.ACTIVE
        );

        Set<ConstraintViolation<AuthRegisterRequestDTO>> violations = validator.validate(auth);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            violations.forEach(v -> sb.append(v.getMessage()));
            var response = CommonResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(sb.toString())
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            return;
        }

        var org = new OrganizationRegisterRequestDTO(
                organizationName,
                address,
                latitude,
                longitude,
                govtId,
                contactNumber
        );

        authUseCase.registerOrganization(auth, org);

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Organization registered successfully")
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
