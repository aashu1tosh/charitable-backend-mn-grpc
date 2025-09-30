package org.charitable.app.infrastructure.adapter.outbound.jpa.organization;

import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationJpaRepository extends JpaRepository<OrganizationEntity, UUID> {

    Optional<OrganizationEntity> findByGovtId(String code);

}
