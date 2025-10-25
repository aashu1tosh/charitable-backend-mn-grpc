package org.charitable.app.application.service.auth;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.charitable.app.application.dto.request.admin.AdminRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.application.dto.request.user.UserRegisterRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.inbound.admin.AdminUseCase;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.application.port.inbound.organization.OrganizationUseCase;
import org.charitable.app.application.port.inbound.user.UserUseCase;
import org.charitable.app.application.port.outbound.authToken.AuthTokenManager;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.IdentityTokens;
import org.charitable.app.domain.port.outbound.auth.AuthRepository;
import org.charitable.app.domain.port.outbound.passwordHash.PasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Singleton
class AuthService implements AuthUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthRepository authRepository;
    private final PasswordHash passwordHash;
    private final OrganizationUseCase organizationService;
    private final UserUseCase userService;
    private final AuthTokenManager tokenService;
    private final AdminUseCase adminService;

    public AuthService(
            AuthRepository authRepository,
            PasswordHash passwordHash,
            OrganizationUseCase organizationService,
            UserUseCase userService,
            AdminUseCase adminService,
            AuthTokenManager tokenService) {
        this.authRepository = authRepository;
        this.passwordHash = passwordHash;
        this.organizationService = organizationService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.adminService = adminService;
    }

    public AppResponse<?> login(LoginRequestDTO data) {
        logger.info("Service login for user: {}", data.getUsername());

        var auth = authRepository.findByEmail(data.getUsername());

        logger.info("Found auth for user: {}", auth);

        if (auth.isEmpty() || !passwordHash.matches(data.getPassword(), auth.get().getPassword())) {
            return new AppResponse<>(false, "Invalid email or password", "");
        }

        var token = tokenService.generateToken(auth.get());
        logger.info("New token for user: {}", token);
        return new AppResponse<IdentityTokens>(true, "Login successful", token);
    }

    @Transactional
    public AppResponse<String> registerOrganization(AuthRegisterRequestDTO data, OrganizationRegisterRequestDTO organization) {
        logger.info("Service register organization for user: {}", data.getEmail());

        var existingAuth = authRepository.findByEmail(data.getEmail());
        if (existingAuth.isPresent()) {
            throw AppException.badRequest("Email already in use");
        }

        var savedOrg = organizationService.register(organization);

        String hashedPassword = passwordHash.hash(data.getPassword());

//        var auth = new Auth(
//                data.getEmail(),
//                hashedPassword,
//                data.getPhone(),
//                data.getRole(),
//                false,
//                data.getStatus(),
//                savedOrg,
//                null,
//                null
//        );

        var auth = Auth.builder()
                .email(data.getEmail())
                .password(hashedPassword)
                .phone(data.getPhone())
                .role(data.getRole())
                .isEmailVerified(false)
                .status(data.getStatus())
                .organization(savedOrg)
                .build();

        var newAuth = authRepository.save(auth);

        logger.info("Registered new organization with ID: {}", newAuth.getId());

        return new AppResponse<String>(true, "Registration successful", "");
    }

    @Transactional
    public AppResponse<String> registerAdmin(AuthRegisterRequestDTO data, AdminRegisterRequestDTO admin) {
        var existingAuth = authRepository.findByEmail(data.getEmail());
        if (existingAuth.isPresent()) {
            throw AppException.badRequest("Email already in user");
        }

        var adminEntity = adminService.register(admin);

        var auth = Auth.builder()
                .email(data.getEmail())
                .password(passwordHash.hash(data.getPassword()))
                .phone(data.getPhone())
                .role(data.getRole())
                .status(data.getStatus())
                .isEmailVerified(false)
                .admin(adminEntity)
                .build();

        authRepository.save(auth);
        return new AppResponse<String>(true, "Registration successful", "");
    }

    @Transactional
    public AppResponse<String> registerUser(AuthRegisterRequestDTO data, UserRegisterRequestDTO user) {
        logger.info("Service register user for email: {}", data.getEmail());

        var existingAuth = authRepository.findByEmail(data.getEmail());
        if (existingAuth.isPresent()) {
            throw AppException.badRequest("Email already in use");
        }

        var savedUser = userService.register(user);
        String hashedPassword = passwordHash.hash(data.getPassword());

        var auth = new Auth(
                data.getEmail(),
                hashedPassword,
                data.getPhone(),
                data.getRole(),
                false,
                data.getStatus(),
                null,
                savedUser,
                null
        );
        var newAuth = authRepository.save(auth);

        logger.info("Registered new user with ID: {}", newAuth.getId());

        return new AppResponse<String>(true, "Registration successful", "dummy-token-for-" + newAuth.getId());
    }

    public AppResponse<Auth> myInfo(UUID authId) {
        logger.info("Service myInfo for authId: {}", authId);
        var auth = authRepository.findMyInfo(authId);

        return new AppResponse<Auth>(true, "Information fetched Successfully", auth);
    }
}
