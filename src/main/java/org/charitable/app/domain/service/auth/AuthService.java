package org.charitable.app.domain.service.auth;

import jakarta.inject.Singleton;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.domain.port.outbound.AuthRepository;
import org.charitable.app.domain.port.outbound.passwordHash.PasswordHash;
import org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth.AuthGrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class AuthService implements AuthUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    private final AuthRepository authRepository;
    private final PasswordHash passwordHash;

    public AuthService(AuthRepository authRepository, PasswordHash passwordHash) {
        this.authRepository = authRepository;
        this.passwordHash = passwordHash;
    }

    public AppResponse<String> login(LoginRequestDTO data) {
        logger.info("Service login for user: {}", data.getUsername());

        var auth = authRepository.findByEmail(data.getUsername());

        logger.info("Found auth for user: {}", auth);

        if (auth.isEmpty() || !passwordHash.matches(data.getPassword(), auth.get().getPassword())) {
            return new AppResponse<>(false, "Invalid email or password", "");
        }

        String token = "dummy-token-for-" + auth.get().getId();
        return new AppResponse<String>(true, "Login successful", token);
    }
}
