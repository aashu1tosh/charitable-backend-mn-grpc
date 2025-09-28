package org.charitable.app.domain.service.auth;

import jakarta.inject.Singleton;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;

@Singleton
class AuthService implements AuthUseCase {

    public AppResponse<String> login(LoginRequestDTO data) {
        if ("user".equals(data.getUsername()) && "pass".equals(data.getPassword())) {
            return new AppResponse<String>(true, "Login successful", "");
        } else {
            return new AppResponse<String>(false, "Invalid credentials", "");
        }
//        return new AppResponse<String>(true, "Login successful", "");
    }
}
