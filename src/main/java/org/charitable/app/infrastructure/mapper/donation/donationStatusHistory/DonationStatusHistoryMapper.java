package org.charitable.app.infrastructure.mapper.donation.donationStatusHistory;

import org.charitable.app.domain.entity.auth.authStatusHistory.AuthStatusHistory;
import org.charitable.app.domain.entity.donation.donationStatusHistory.DonationStatusHistory;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.authStatusHistory.AuthStatusHistoryEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.donation.donationStatusHistory.DonationStatusHistoryEntity;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.charitable.app.infrastructure.mapper.donation.DonationMapper;

public class DonationStatusHistoryMapper {
    public static DonationStatusHistory mapToDomain(DonationStatusHistoryEntity entity) {
        return DonationStatusHistory.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .donation(DonationMapper.mapToDomain(entity.getDonation()))
                .status(entity.getStatus())
                .build();

    }
}
