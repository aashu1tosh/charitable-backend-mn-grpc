package org.charitable.app.domain.service.auth;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.domain.port.outbound.AuthRepository;

@Singleton
@AllArgsConstructor
class AuthService implements AuthUseCase {

    private final AuthRepository authRepository;

    public AppResponse<String> login(LoginRequestDTO data) {

        var auth = authRepository.findByEmail(data.getUsername());

        if (auth == null || !auth.verifyPassword(data.getPassword())) {
            return new AppResponse<>(false, "Invalid email or password", "");
        }

        String token = "dummy-token-for-" + auth.getId();
        return new AppResponse<String>(true, "Login successful", token);
    }
}
