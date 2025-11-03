package org.charitable.app.domain.port.outbound.donation;

import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.donation.DonationFilter;
import org.charitable.app.domain.model.token.TokenPayload;

public interface DonationRepository {
    Donation save (Donation donation);
    Page<Donation> getDonations(DonationFilter filter, TokenPayload user);
}
