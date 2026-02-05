package org.charitable.app.domain.port.outbound.db.organization;

import org.charitable.app.domain.entity.organization.Organization;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {
    Optional<Organization> findById(UUID id);
    Optional<Organization> findByGovtId(String govtId);
    Organization save(Organization organization);
}
