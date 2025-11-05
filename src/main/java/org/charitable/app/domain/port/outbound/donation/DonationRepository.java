package org.charitable.app.domain.port.outbound.donation;

import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.model.donation.DonationFilter;
import org.charitable.app.domain.model.token.TokenPayload;

import java.util.UUID;

public interface DonationRepository {
    Donation save (Donation donation);
    Page<Donation> getDonations(DonationFilter filter, TokenPayload user);
    Donation claimDonation(UUID donationId, UUID organization);
}
