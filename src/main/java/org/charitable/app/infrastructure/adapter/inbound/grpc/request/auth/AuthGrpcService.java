package org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.application.dto.request.user.UserRegisterRequestDTO;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.common.utils.ValidationUtils;
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.auth.AuthStatus;
import org.charitable.app.infrastructure.adapter.inbound.grpc.context.GrpcContextKeys;
import org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication.GrpcAuthenticate;
import org.charitable.app.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    private final AuthUseCase authUseCase;
    private final ValidationUtils validator;

    public AuthGrpcService(AuthUseCase authUseCase, ValidationUtils validator) {
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

            validator.validate(dto);

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
    public void registerUser(RegisterUserRequest request, StreamObserver<CommonResponse> responseObserver) {

        String password = request.getPassword();
        String email = request.getEmail();
        String phone = request.getPhoneNumber();
        var role = Role.USER;
        var status = AuthStatus.ACTIVE;

        var auth = new AuthRegisterRequestDTO(
                email,
                password,
                phone,
                role,
                status
        );
        validator.validate(auth);

        var user = UserRegisterRequestDTO.builder()
                        .firstName(request.getFirstName())
                        .middleName(request.getMiddleName())
                        .lastName(request.getLastName())
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .build();
        validator.validate(user);

        authUseCase.registerUser(auth, user);

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("User registered successfully")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
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
        validator.validate(auth);

        var org = new OrganizationRegisterRequestDTO(
                organizationName,
                address,
                latitude,
                longitude,
                govtId,
                contactNumber
        );

        var resp = authUseCase.registerOrganization(auth, org);

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(resp.isSuccess())
                .setMessage(resp.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @GrpcAuthenticate(roles = {Role.SUDO_ADMIN})
    public void myInfo(EmptyRequest request, StreamObserver<CommonResponse> responseObserver) {

        logger.info("Receive my info request");
        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();

        var resp = authUseCase.myInfo(tokenPayload.getId());

        logger.info("Receive my info response: {}", resp);
        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("My info retrieved successfully")
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
