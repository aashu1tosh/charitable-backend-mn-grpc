package org.charitable.app.infrastructure.adapter.outbound.jpa.donation;

import jakarta.inject.Singleton;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.port.outbound.donation.DonationRepository;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.charitable.app.infrastructure.mapper.donation.DonationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class DonationRepositoryImpl implements DonationRepository {

    private static final Logger logger = LoggerFactory.getLogger(DonationRepositoryImpl.class);

    private final DonationJpaRepository donationJpaRepo;

    DonationRepositoryImpl(DonationJpaRepository donationJpaRepo) {
        this.donationJpaRepo = donationJpaRepo;
    }

    @Override
    public Donation save(Donation donation) {
        var entity = DonationEntity.builder()
                .title(donation.getTitle())
                .description(donation.getDescription())
                .type(donation.getType())
                .status(donation.getStatus())
                .donor(AuthMapper.mapToEntity(donation.getDonor()))
                .url(donation.getUrl() != null ?  donation.getUrl() : null)
                .build();
        var resp = donationJpaRepo.save(entity);
        return DonationMapper.mapToDomain(resp);

    }
}
