package org.charitable.app.infrastructure.mapper.organization;

import jakarta.validation.constraints.NotNull;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.infrastructure.adapter.outbound.jpa.organization.OrganizationEntity;

public class OrganizationMapper {
    public static Organization mapToDomain(OrganizationEntity entity) {
        if (entity == null) return null;

        return getOrganization(entity);
    }

    public static OrganizationEntity mapToEntitySafe(Organization domain) {
        if (domain == null) return null;

        return mapToEntity(domain);
    }

    private static Organization getOrganization(OrganizationEntity entity) {
        var org = new Organization(
                entity.getName(),
                entity.getAddress(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getGovtId(),
                entity.getContactNumber(),
                null
        );
        org.setId(entity.getId());
        org.setCreatedAt(entity.getCreatedAt());
        org.setUpdatedAt(entity.getUpdatedAt());
        return org;
    }

    private static OrganizationEntity mapToEntity(Organization domain) {
        if (domain == null) return null;

        var entity = new OrganizationEntity(
                domain.getName(),
                domain.getAddress(),
                domain.getLatitude(),
                domain.getLongitude(),
                domain.getGovtId(),
                domain.getContactNumber(),
                null
        );
        entity.setId(domain.getId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
