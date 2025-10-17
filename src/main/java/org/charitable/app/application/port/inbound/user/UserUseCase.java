package org.charitable.app.application.port.inbound.user;

import org.charitable.app.application.dto.request.user.UserRegisterRequestDTO;
import org.charitable.app.domain.entity.user.User;

public interface UserUseCase {
    User register(UserRegisterRequestDTO user);
}
