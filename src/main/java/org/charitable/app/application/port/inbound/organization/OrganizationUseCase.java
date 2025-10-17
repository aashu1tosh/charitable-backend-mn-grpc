package org.charitable.app.application.port.inbound.organization;

import org.charitable.app.application.dto.request.organization.OrganizationRegisterRequestDTO;
import org.charitable.app.domain.entity.organization.Organization;

public interface OrganizationUseCase {
    Organization register(OrganizationRegisterRequestDTO organization);
}
