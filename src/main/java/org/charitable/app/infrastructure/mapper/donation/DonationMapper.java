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
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .type(entity.getType())
                .status(entity.getStatus())
                .url(entity.getUrl() != null ?  entity.getUrl() : "")
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
                .url(domain.getUrl() != null ?  domain.getUrl() : null)
                .build();
        entity.setId(domain.getId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
}
