package org.charitable.app.application.port.inbound.admin;

import org.charitable.app.application.dto.request.admin.AdminRegisterRequestDTO;

public interface AdminUseCase {
    org.charitable.app.domain.entity.admin.Admin register(AdminRegisterRequestDTO admin);
}
