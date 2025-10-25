package org.charitable.app.application.port.inbound.admin;

import org.charitable.app.application.dto.request.admin.AdminRegisterRequestDTO;
import org.charitable.app.domain.admin.entity.Admin;

public interface AdminUseCase {
    Admin register(AdminRegisterRequestDTO admin);
}
