package org.charitable.app.domain.port.outbound.db.donation;

import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.model.donation.DonationFilter;
import org.charitable.app.domain.model.token.TokenPayload;

import java.util.Optional;
import java.util.UUID;

public interface DonationRepository {
    Optional<Donation> findById(UUID id);
    Optional<Donation> findByIdWithRelations(UUID id);
    Donation save (Donation donation);
    Page<Donation> getDonations(DonationFilter filter, TokenPayload user);
    Donation claimDonation(UUID donationId, Organization organization);
    Donation gotDonation(UUID donationId);
}
