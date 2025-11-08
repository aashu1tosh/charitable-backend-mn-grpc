package org.charitable.app.application.service.auth;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.charitable.app.application.dto.request.admin.AdminRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.auth.UpdateAuthStatusDTO;
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
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.token.TokenPayload;
import org.charitable.app.domain.port.outbound.auth.AuthRepository;
import org.charitable.app.domain.port.outbound.auth.authStatusHistory.AuthStatusHistoryRepository;
import org.charitable.app.domain.port.outbound.passwordHash.PasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Singleton
class AuthService implements AuthUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthRepository authRepository;
    private final AuthStatusHistoryRepository authStatusHistoryRepository;
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
            AuthTokenManager tokenService,
            AuthStatusHistoryRepository authStatusHistoryRepository) {
        this.authRepository = authRepository;
        this.passwordHash = passwordHash;
        this.organizationService = organizationService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.adminService = adminService;
        this.authStatusHistoryRepository = authStatusHistoryRepository;
    }

    public IdentityTokens login(LoginRequestDTO data) {
        logger.info("Service login for user: {}", data.getUsername());

        var auth = authRepository.findByEmail(data.getUsername());

        logger.info("Found auth for user: {}", auth);

        if (auth.isEmpty() || !passwordHash.matches(data.getPassword(), auth.get().getPassword())) {
            throw AppException.badRequest("Invalid credentials");
        }

        return tokenService.generateToken(auth.get());
    }

    @Transactional
    public String registerOrganization(AuthRegisterRequestDTO data, OrganizationRegisterRequestDTO organization, AdminRegisterRequestDTO admin) {
        logger.info("Service register organization for user: {}", data.getEmail());

        var existingAuth = authRepository.findByEmail(data.getEmail());
        if (existingAuth.isPresent()) {
            throw AppException.badRequest("Email already in use");
        }

        var savedOrg = organizationService.register(organization);
        var savedAdmin = adminService.register(admin);

        String hashedPassword = passwordHash.hash(data.getPassword());

        var auth = Auth.builder()
                .email(data.getEmail())
                .password(hashedPassword)
                .phone(data.getPhone())
                .role(data.getRole())
                .isEmailVerified(false)
                .status(data.getStatus())
                .organization(savedOrg)
                .admin(savedAdmin)
                .build();

        var newAuth = authRepository.save(auth);
        authStatusHistoryRepository.save(newAuth, data.getStatus());

        logger.info("Registered new organization with ID: {}", newAuth.getId());

        return "Registration successful";
    }

    @Transactional
    public String registerAdmin(AuthRegisterRequestDTO data, AdminRegisterRequestDTO admin) {
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
        authStatusHistoryRepository.save(auth, data.getStatus());
        return "Registration successful";
    }

    @Transactional
    public String registerUser(AuthRegisterRequestDTO data, UserRegisterRequestDTO user) {
        logger.info("Service register user for email: {}", data.getEmail());

        var existingAuth = authRepository.findByEmail(data.getEmail());
        if (existingAuth.isPresent()) {
            throw AppException.badRequest("Email already in use");
        }

        var savedUser = userService.register(user);
        String hashedPassword = passwordHash.hash(data.getPassword());

//        var auth = new Auth(
//                data.getEmail(),
//                hashedPassword,
//                data.getPhone(),
//                data.getRole(),
//                false,
//                data.getStatus(),
//                null,
//                null,
//                savedUser,
//                null
//        );
        var auth = Auth.builder()
                .email(data.getEmail())
                .password(hashedPassword)
                .phone(data.getPhone())
                .role(data.getRole())
                .isEmailVerified(false)
                .status(data.getStatus())
                .user(savedUser)
                .build();
        var newAuth = authRepository.save(auth);

        logger.info("Registered new user with ID: {}", newAuth.getId());

        return "Registration successful";
    }

    public String updateAuthStatus(UpdateAuthStatusDTO data, TokenPayload user) {
        var auth = authRepository.findById(data.getId())
                .orElseThrow(() -> AppException.badRequest("Auth not found"));

        var prjRole = auth.getRole();

        if(Role.SUDO_ADMIN.equals(prjRole)) {
            throw AppException.badRequest("You are not authorized for this process");
        }

        if(user.getRole().equals(Role.ADMIN)) {
            throw AppException.badRequest("You are not authorized for this process");
        }

        var updatedAuth = authRepository.updateAuthStatus(data.getId(), data.getAuthStatus());
        authStatusHistoryRepository.save(updatedAuth, data.getAuthStatus());
        return "Update Successful";
    }

    public Auth myInfo(UUID authId) {
        logger.info("Service myInfo for authId: {}", authId);
        return  authRepository.findMyInfo(authId);
    }

    public Auth findById(UUID id) {

        var auth = authRepository.findById(id);
        if(auth.isEmpty()) {
            throw AppException.badRequest("Auth not found");
        }
        return auth.get();
    }

    public IdentityTokens refreshToken(String refreshToken) {
        var user = tokenService.validateRefreshToken(refreshToken);

        var auth = authRepository.findById(user.getId()).orElseThrow(() -> AppException.badRequest("Auth not found"));

        return tokenService.generateToken(auth);
    }
}
