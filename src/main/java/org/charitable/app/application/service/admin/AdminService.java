package org.charitable.app.application.service.admin;

import jakarta.inject.Singleton;
import org.charitable.app.application.dto.request.admin.AdminRegisterRequestDTO;
import org.charitable.app.application.port.inbound.admin.AdminUseCase;
import org.charitable.app.domain.entity.admin.Admin;
import org.charitable.app.domain.port.outbound.admin.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AdminService implements AdminUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;

    AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    @Override
    public Admin register(AdminRegisterRequestDTO admin) {
        var model = Admin.builder()
                        .firstName(admin.getFirstName())
                        .middleName(admin.getMiddleName() != null ? admin.getMiddleName() : null)
                        .lastName(admin.getLastName())
                        .build();

        var resp = adminRepository.register(model);
        logger.info("Registered Admin {}", resp.getId());
        return resp;
    }
}
