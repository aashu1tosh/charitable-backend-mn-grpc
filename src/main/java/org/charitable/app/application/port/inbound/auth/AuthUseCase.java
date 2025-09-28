package org.charitable.app.application.port.inbound.auth;

import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;

public interface AuthUseCase {
    AppResponse<String> login(LoginRequestDTO loginRequestDTO);
}
