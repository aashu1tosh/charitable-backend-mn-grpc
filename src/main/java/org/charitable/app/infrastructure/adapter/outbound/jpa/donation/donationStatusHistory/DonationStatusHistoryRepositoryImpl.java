package org.charitable.app.infrastructure.adapter.outbound.jpa.donation.donationStatusHistory;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charitable.app.common.utils.PrintUtils;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.entity.donation.donationStatusHistory.DonationStatusHistory;
import org.charitable.app.domain.model.donation.DonationStatus;
import org.charitable.app.domain.port.outbound.db.donation.donationStatusHistoryRespository.DonationStatusHistoryRepository;
import org.charitable.app.infrastructure.mapper.donation.DonationMapper;
import org.charitable.app.infrastructure.mapper.donation.donationStatusHistory.DonationStatusHistoryMapper;

@Slf4j
@AllArgsConstructor
@Singleton
public class DonationStatusHistoryRepositoryImpl implements DonationStatusHistoryRepository {

    private final DonationStatusHistoryJpaRepository donationStatusHistoryRepo;

    @Override
    public DonationStatusHistory save(Donation donation, DonationStatus status) {
        var entity = DonationStatusHistoryEntity.builder()
                .donation(DonationMapper.mapToEntity(donation))
                .status(status)
                .build();

        var resp = donationStatusHistoryRepo.save(entity);
        log.info("DonationStatusHistory for {}", PrintUtils.prettyPrint(entity));
        return DonationStatusHistoryMapper.mapToDomain(resp);
    };



}
