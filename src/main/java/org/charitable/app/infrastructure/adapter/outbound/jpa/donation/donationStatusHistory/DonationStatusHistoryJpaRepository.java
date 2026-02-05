package org.charitable.app.infrastructure.adapter.outbound.jpa.donation.donationStatusHistory;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Repository
public interface DonationStatusHistoryJpaRepository extends JpaRepository<DonationStatusHistoryEntity, UUID> {
}
