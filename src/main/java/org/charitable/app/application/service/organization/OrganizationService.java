package org.charitable.app.application.service.organization;

import jakarta.inject.Singleton;
import org.charitable.app.application.port.inbound.organization.OrganizationUseCase;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.port.outbound.organization.OrganizationRepository;
import org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth.AuthGrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class OrganizationService implements OrganizationUseCase {
    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }
    @Override
    public Organization register(Organization organization) {
        logger.info("Service register organization: {}", organization.getName());

        var org = organizationRepository.save(organization);
        logger.info("Registered organization: {}", organization);
        return org;
    }
}
