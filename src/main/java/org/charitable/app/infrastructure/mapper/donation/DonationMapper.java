package org.charitable.app.infrastructure.mapper.donation;

import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.infrastructure.adapter.outbound.jpa.donation.DonationEntity;

public class DonationMapper {

    public static Donation mapToDomain(DonationEntity entity) {
        if(entity == null) {
            return null;
        }

        return Donation.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .latitude(entity.getLatitude() != null ? entity.getLatitude() : 0)
                .longitude(entity.getLongitude() != null ? entity.getLongitude() : 0)
                .type(entity.getType())
                .status(entity.getStatus())
                .urlPath(entity.getUrlPath() != null ?  entity.getUrlPath() : "")
                .build();

    }

    public static DonationEntity mapToEntity(Donation domain) {
        if(domain == null) {
            return null;
        }

        var entity = DonationEntity.builder()
                .title(domain.getTitle())
                .description(domain.getDescription())
                .type(domain.getType())
                .status(domain.getStatus())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .urlPath(domain.getUrlPath() != null ?  domain.getUrlPath() : null)
                .build();
        entity.setId(domain.getId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
