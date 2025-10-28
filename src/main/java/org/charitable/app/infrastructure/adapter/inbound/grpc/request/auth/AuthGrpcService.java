package org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotNull;
import org.charitable.app.application.dto.request.admin.AdminRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.auth.UpdateAuthStatusDTO;
import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.application.dto.request.user.UserRegisterRequestDTO;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.common.utils.UUIDUtils;
import org.charitable.app.common.utils.ValidationUtils;
import org.charitable.app.domain.entity.auth.IdentityTokens;
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
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
            logger.info("Received login request for user: {}", request.getUsername());
            String username = request.getUsername();
            String password = request.getPassword();

            var dto = new LoginRequestDTO(request.getUsername(), request.getPassword());

            validator.validate(dto);

            var result = authUseCase.login(dto);
            IdentityTokens tokens = (IdentityTokens) result.getData();

            var responseToken = AuthTokenResponse.newBuilder().setAccessToken(tokens.getAccessToken()).setRefreshToken(tokens.getRefreshToken()).build();
            var response = LoginResponse.newBuilder()
                    .setSuccess(result.isSuccess())
                            .setMessage(result.getMessage())
                            .setData(responseToken).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

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
    public void registerAdmin(RegisterAdminRequest request, StreamObserver<CommonResponse> responseObserver) {
        logger.info("Admin register request received.");
        String password = request.getPassword();
        String email = request.getEmail();
        String phone = request.getPhoneNumber();

        String firstName = request.getFirstName();
        String middleName = request.getMiddleName();
        String lastName = request.getLastName();

        var auth = AuthRegisterRequestDTO.builder()
                .email(email)
                .password(password)
                .phone(phone)
                .role(Role.ADMIN)
                .status(AuthStatus.ACTIVE)
                .build();

        validator.validate(auth);

        var admin = AdminRegisterRequestDTO.builder()
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .build();

        validator.validate(admin);

        var rsp = authUseCase.registerAdmin(auth, admin);

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(rsp.isSuccess())
                .setMessage(rsp.getMessage())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @GrpcAuthenticate()
    public void myInfo(EmptyRequest request, StreamObserver<MyInfoResponse> responseObserver) {

        logger.info("Receive my info request");
        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();

        var resp = authUseCase.myInfo(tokenPayload.getId());

        logger.info("My info log {}", resp.getData());
        logger.info("Receive my info response: {}", resp);

        Organization orgData = null;
        if (resp.getData().getOrganization() != null && resp.getData().getOrganization().getId() != null) {
            var org = resp.getData().getOrganization();
            orgData = Organization.newBuilder()
                    .setName(org.getName())
                    .setAddress(org.getAddress())
                    .setLatitude(org.getLatitude())
                    .setLongitude(org.getLongitude())
                    .setGovtId(org.getGovtId())
                    .setContactNumber(org.getContactNumber())
                    .build();
        }

        org.charitable.app.proto.User user = null;
        if (resp.getData().getUser() != null && resp.getData().getUser().getId() != null) {
            logger.info("Receive my info user {}", resp.getData().getUser().getId());
            var usr = resp.getData().getUser();
            user = User.newBuilder()
                    .setFirstName(usr.getFirstName())
                    .setMiddleName(usr.getMiddleName())
                    .setLastName(usr.getLastName())
                    .setLatitude(usr.getLatitude())
                    .setLongitude(usr.getLongitude())
                    .build();
        }

        MyInfoData.Builder infoBuilder = MyInfoData.newBuilder()
                .setEmail(resp.getData().getEmail())
                .setPhone(resp.getData().getPhone())
                .setRole(
                        resp.getData().getRole() != null
                                ? org.charitable.app.proto.Role.valueOf(resp.getData().getRole().name())
                                : org.charitable.app.proto.Role.ROLE_UNSPECIFIED
                )
                .setStatus(
                        resp.getData().getStatus() != null
                                ? org.charitable.app.proto.AuthStatus.valueOf(resp.getData().getStatus().name())
                                : org.charitable.app.proto.AuthStatus.STATUS_UNSPECIFIED
                );

        if (orgData != null) {
            infoBuilder.setOrganization(orgData);
        }

        if(user != null) {
            infoBuilder.setUser(user);
        }

        MyInfoData infoData = infoBuilder.build();

        MyInfoResponse response = MyInfoResponse.newBuilder()
                .setSuccess(true)
                .setMessage(resp.getMessage())
                .setData(infoData)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @GrpcAuthenticate(roles = {Role.ADMIN, Role.SUDO_ADMIN})
    public void updateAuthStatus(UpdateAuthStatusRequest request, StreamObserver<CommonResponse> responseObserver) {
        logger.info("Update auth status request received.");
        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();
        var data = UpdateAuthStatusDTO.builder()
                .id(UUIDUtils.stringToUUID(request.getAuthId()))
                .authStatus(org.charitable.app.infrastructure.grpc.mapper.AuthStatusMapper.fromProto(request.getStatus()))
                .build();

        validator.validate(data);
        var resp = authUseCase.updateAuthStatus(data, tokenPayload);

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(resp.isSuccess())
                .setMessage(resp.getMessage())
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
