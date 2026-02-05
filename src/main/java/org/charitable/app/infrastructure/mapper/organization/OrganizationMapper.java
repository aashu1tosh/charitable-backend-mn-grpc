package org.charitable.app.infrastructure.mapper.organization;

import jakarta.validation.constraints.NotNull;
import org.charitable.app.common.utils.ValueUtils;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.infrastructure.adapter.outbound.jpa.organization.OrganizationEntity;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.hibernate.Hibernate;

public class OrganizationMapper {
    public static Organization mapToDomain(OrganizationEntity entity) {
        if (entity == null) return null;

        if (!Hibernate.isInitialized(entity)) {
            return null;
        }

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
                entity.getContactNumber()
        );
        org.setId(entity.getId());
        org.setCreatedAt(entity.getCreatedAt());
        org.setUpdatedAt(entity.getUpdatedAt());
        return org;
    }

    private static OrganizationEntity mapToEntity(Organization domain) {
        if (domain == null) return null;

//        var entity = new OrganizationEntity(
//                domain.getName(),
//                domain.getAddress(),
//                domain.getLatitude(),
//                domain.getLongitude(),
//                domain.getGovtId(),
//                domain.getContactNumber(),
//                null
//        );
//        entity.setId(domain.getId());
//        entity.setCreatedAt(domain.getCreatedAt());
//        entity.setUpdatedAt(domain.getUpdatedAt());

        var entity = OrganizationEntity.builder()
                .id(domain.getId() != null ? domain.getId() : null)
                .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt() : null)
                .updatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt() : null)
                .name(!ValueUtils.checkNullOrEmpty(domain.getName()) ? domain.getName() : null)
                .address(!ValueUtils.checkNullOrEmpty(domain.getAddress()) ? domain.getAddress() : null)
                .latitude(!ValueUtils.checkNullOrEmpty(domain.getLatitude()) ? domain.getLatitude() : null)
                .longitude(!ValueUtils.checkNullOrEmpty(domain.getLongitude()) ? domain.getLongitude() : null)
                .govtId(!ValueUtils.checkNullOrEmpty(domain.getGovtId()) ? domain.getGovtId() : null)
                .contactNumber(!ValueUtils.checkNullOrEmpty(domain.getContactNumber()) ? domain.getContactNumber() : null)
                .build();
        return entity;
    }
}
