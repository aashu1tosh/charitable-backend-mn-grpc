package org.charitable.app.infrastructure.adapter.outbound.jpa.donation;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import lombok.Locked;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DonationJpaRepository  extends JpaRepository<DonationEntity, UUID> {
    Optional<DonationEntity> findById(UUID id);

    @Query("""
    SELECT d FROM DonationEntity d
    LEFT JOIN FETCH d.organization
    LEFT JOIN FETCH d.donor
    WHERE d.id = :id
""")
    Optional<DonationEntity> findByIdWithRelations(UUID id);
}
