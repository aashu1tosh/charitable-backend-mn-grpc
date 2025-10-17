package org.charitable.app.application.port.inbound.auth;

import org.charitable.app.application.dto.request.auth.AuthRegisterRequestDTO;
import org.charitable.app.application.dto.request.auth.LoginRequestDTO;
import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.application.dto.request.user.UserRegisterRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.domain.entity.auth.Auth;

import java.util.UUID;

public interface AuthUseCase {
    AppResponse<?> login(LoginRequestDTO loginRequestDTO);
    AppResponse<String> registerOrganization(AuthRegisterRequestDTO authRegisterRequestDTO, OrganizationRegisterRequestDTO organizationRegisterRequestDTO );
    AppResponse<String> registerUser(AuthRegisterRequestDTO authRegisterRequestDTO, UserRegisterRequestDTO userRegisterRequestDTO);
    AppResponse<Auth> myInfo(UUID authId);
}
