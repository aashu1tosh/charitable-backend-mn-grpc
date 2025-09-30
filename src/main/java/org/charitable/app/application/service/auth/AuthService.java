package org.charitable.app.application.service.auth;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.application.port.inbound.organization.OrganizationUseCase;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.port.outbound.auth.AuthRepository;
import org.charitable.app.domain.port.outbound.passwordHash.PasswordHash;
import org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth.AuthGrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class AuthService implements AuthUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    private final AuthRepository authRepository;
    private final PasswordHash passwordHash;
    private final OrganizationUseCase organizationService;

    public AuthService(AuthRepository authRepository, PasswordHash passwordHash, OrganizationUseCase organizationService) {
        this.authRepository = authRepository;
        this.passwordHash = passwordHash;
        this.organizationService = organizationService;
    }

    public AppResponse<String> login(LoginRequestDTO data) {
        logger.info("Service login for user: {}", data.getUsername());

        var auth = authRepository.findByEmail(data.getUsername());

        logger.info("Found auth for user: {}", auth);

        if (auth.isEmpty() || !passwordHash.matches(data.getPassword(), auth.get().getPassword())) {
            return new AppResponse<>(false, "Invalid email or password", "");
        }

        String token = "dummy-token-for-" + auth.get().getId();
        logger.info("New token for user: {}", token);
        return new AppResponse<String>(true, "Login successful", token);
    }

    @Transactional
    public AppResponse<String> registerOrganization(AuthRegisterRequestDTO data, OrganizationRegisterRequestDTO organization) {
        logger.info("Service register organization for user: {}", data.getEmail());

        var existingAuth = authRepository.findByEmail(data.getEmail());
        if (existingAuth.isPresent()) {
            return new AppResponse<>(false, "Email already in use", "");
        }

        var org = new Organization(
                organization.getName(),
                organization.getAddress(),
                organization.getLatitude(),
                organization.getLongitude(),
                organization.getGovtId(),
                organization.getContactNumber(),
                null
        );

        var savedOrg = organizationService.register(org);

        String hashedPassword = passwordHash.hash(data.getPassword());

        var auth = new Auth(
                data.getEmail(),
                hashedPassword,
                data.getPhone(),
                data.getRole(),
                false,
                data.getStatus(),
                savedOrg
        );
        var newAuth = authRepository.save(auth);

        logger.info("Registered new organization with ID: {}", newAuth.getId());

        return new AppResponse<String>(true, "Registration successful", "dummy-token-for-" + newAuth.getId());
    }
}
