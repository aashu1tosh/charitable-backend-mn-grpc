package org.charitable.app.infrastructure.adapter.outbound.jpa.organization;

import jakarta.inject.Singleton;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.port.outbound.organization.OrganizationRepository;

import java.util.Optional;
import java.util.UUID;

@Singleton
class OrganizationRepositoryImpl implements OrganizationRepository {
    private final OrganizationJpaRepository organizationJpaRepository;

    public OrganizationRepositoryImpl(OrganizationJpaRepository organizationJpaRepository) {
        this.organizationJpaRepository = organizationJpaRepository;
    }

    @Override
    public Optional<Organization> findById(UUID id) {
        return organizationJpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public Optional<Organization> findByGovtId(String govtId) {
        return organizationJpaRepository.findByGovtId(govtId)
                .map(this::mapToDomain);
    }

    @Override
    public Organization save(Organization organization) {
        OrganizationEntity entity = new OrganizationEntity(
                organization.getName(),
                organization.getAddress(),
                organization.getLatitude(),
                organization.getLongitude(),
                organization.getGovtId(),
                organization.getContactNumber(),
                null
        );
        OrganizationEntity savedEntity = organizationJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }


    private Organization mapToDomain(OrganizationEntity organizationEntity) {
        var org = new Organization(
                organizationEntity.getName(),
                organizationEntity.getAddress(),
                organizationEntity.getLatitude(),
                organizationEntity.getLongitude(),
                organizationEntity.getGovtId(),
                organizationEntity.getContactNumber(),
                null
        );

        org.setId(organizationEntity.getId());
        org.setCreatedAt(organizationEntity.getCreatedAt());
        org.setUpdatedAt(organizationEntity.getUpdatedAt());
        return org;
    }
}
