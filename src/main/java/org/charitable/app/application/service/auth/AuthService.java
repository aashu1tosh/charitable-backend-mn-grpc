package org.charitable.app.application.service.auth;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.charitable.app.application.dto.request.admin.AdminRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.auth.UpdateAuthStatusDTO;
import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.application.dto.request.user.UserRegisterRequestDTO;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.inbound.admin.AdminUseCase;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.application.port.inbound.organization.OrganizationUseCase;
import org.charitable.app.application.port.inbound.user.UserUseCase;
import org.charitable.app.application.port.outbound.authToken.AuthTokenManager;
import org.charitable.app.common.utils.PrintUtils;
import org.charitable.app.common.utils.UUIDUtils;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.IdentityTokens;
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.token.TokenPayload;
import org.charitable.app.domain.port.outbound.db.auth.AuthRepository;
import org.charitable.app.domain.port.outbound.db.auth.authStatusHistory.AuthStatusHistoryRepository;
import org.charitable.app.domain.port.outbound.emailQueue.EmailPublisherPort;
import org.charitable.app.domain.port.outbound.emailQueue.EmailTemplateLoadPort;
import org.charitable.app.domain.port.outbound.passwordHash.PasswordHash;
import org.charitable.app.infrastructure.adapter.outbound.rabbitmq.EmailMessage;
import org.charitable.app.infrastructure.config.environment.EnvVariables;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Singleton
@Slf4j
class AuthService implements AuthUseCase {

    private final AuthRepository authRepository;
    private final AuthStatusHistoryRepository authStatusHistoryRepository;
    private final PasswordHash passwordHash;
    private final OrganizationUseCase organizationService;
    private final UserUseCase userService;
    private final AuthTokenManager tokenService;
    private final AdminUseCase adminService;
    private final EmailPublisherPort emailPublisher;
    private final EmailTemplateLoadPort emailTemplate;
    private final EnvVariables env;

    AuthService(
            AuthRepository authRepository,
            PasswordHash passwordHash,
            OrganizationUseCase organizationService,
            UserUseCase userService,
            AdminUseCase adminService,
            AuthTokenManager tokenService,
            AuthStatusHistoryRepository authStatusHistoryRepository,
            EmailPublisherPort emailPublisher,
            EmailTemplateLoadPort emailTemplateLoadPort,
            EnvVariables env) {
        this.authRepository = authRepository;
        this.passwordHash = passwordHash;
        this.organizationService = organizationService;
        this.userService = userService;
        this.tokenService = tokenService;
        this.adminService = adminService;
        this.authStatusHistoryRepository = authStatusHistoryRepository;
        this.emailPublisher = emailPublisher;
        this.emailTemplate = emailTemplateLoadPort;
        this.env = env;
    }

    public IdentityTokens login(LoginRequestDTO data) {
        log.info("Service login for user: {}", data.getUsername());

        var auth = authRepository.findByEmail(data.getUsername());

        log.info("Found auth for user: {}", auth);

        if (auth.isEmpty() || !passwordHash.matches(data.getPassword(), auth.get().getPassword())) {
            throw AppException.badRequest("Invalid credentials");
        }

        return tokenService.generateToken(auth.get());
    }

    @Transactional
    public String registerOrganization(AuthRegisterRequestDTO data, OrganizationRegisterRequestDTO organization, AdminRegisterRequestDTO admin) {
        log.info("Service register organization for user: {}", data.getEmail());

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
        this.sendEmailVerification(newAuth.getEmail());
        log.info("Registered new organization with ID: {}", newAuth.getId());

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
        this.sendEmailVerification(auth.getEmail());
        return "Registration successful";
    }

    @Transactional
    public String registerUser(AuthRegisterRequestDTO data, UserRegisterRequestDTO user) {
        log.info("Service register user for email: {}", data.getEmail());

        var existingAuth = authRepository.findByEmail(data.getEmail());
        if (existingAuth.isPresent()) {
            throw AppException.badRequest("Email already in use");
        }

        var existingPhoneNumber = authRepository.findByPhone(data.getEmail());
        if (existingPhoneNumber.isPresent()) {
            throw AppException.badRequest("Phone number already in use");
        }

        var savedUser = userService.register(user);
        String hashedPassword = passwordHash.hash(data.getPassword());

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

        this.sendEmailVerification(newAuth.getEmail());
        log.info("Registered new user with ID: {}", newAuth.getId());
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
        log.info("Service myInfo for authId: {}", authId);
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

    public void sendEmailVerification(String email) {
        try {
            var auth = authRepository.findByEmail(email);

            if(auth.isEmpty()) {
                throw AppException.badRequest("Auth not found");
            }

            var authData = auth.get();
            if(authData.getIsEmailVerified()) {
                throw AppException.badRequest("Email already verified");
            }

            Instant publishAt = authData.getEmailVerificationPublishAt();
            if (publishAt != null && Instant.now().isBefore(publishAt.plus(Duration.ofMinutes(30)))) {
                throw AppException.badRequest(
                        "Email verification has been sent. Please check inbox as well as spam."
                );
            }

            var token = UUIDUtils.randomUUIDString();

            authData.setEmailVerificationPublishAt(Instant.now());
            authData.setEmailVerificationToken(token);

            var update = authRepository.update(authData);

            var user = authRepository.findMyInfo(authData.getId());
            log.info("User Details: {}", PrintUtils.prettyPrint(user));
            String fullName = switch (user.getRole()) {
                case ORGANIZATION_ADMIN, ORGANIZATION_SUPER_ADMIN, ADMIN ->
                        user.getAdmin().getFirstName() + " " + user.getAdmin().getLastName();
                case USER -> user.getUser().getFirstName() + " " + user.getUser().getLastName();
                default -> "User";
            };
            log.info("Full name : {}", fullName);
            EmailMessage message = EmailMessage.builder()
                    .to(authData.getEmail())
                    .subject("Verify your email")
                    .template(emailTemplate.loadTemplate(
                            "verify-email.html",
                            Map.of(
                                    "fullName", fullName,
                                    "email", authData.getEmail(),
                                    "link", env.getFrontEndUri() + "verify-email/" + token
                            )
                    ))
                    .from("noreply@givehope.com")
                    .build();


            emailPublisher.sendEmail(message);
            return;
        } catch (AppException e) {
            throw e;
        }
        catch (Exception ex) {
            log.error("Sending email verification failed with exception: {}", ex.getMessage());
            return;
        }
    }
    public Auth verifyEmail(String token) {
        var auth = authRepository.findByEmailVerificationToken(token);

        if(auth.isEmpty()) {
            throw AppException.badRequest("Invalid email verification token");
        }

        var authData = auth.get();
        var publishAt = authData.getEmailVerificationPublishAt();

        if (publishAt != null && Instant.now().isBefore(publishAt.plus(Duration.ofMinutes(30)))) {
            authData.setIsEmailVerified(true);
            return authRepository.save(authData);
        } else {
            throw AppException.badRequest("Email verification Token has expired");
        }
    }
}
