package org.charitable.app.infrastructure.adapter.outbound.jpa.organization;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationJpaRepository extends JpaRepository<OrganizationEntity, UUID> {

    Optional<OrganizationEntity> findByGovtId(String code);

}
