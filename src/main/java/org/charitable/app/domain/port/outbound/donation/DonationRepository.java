package org.charitable.app.domain.port.outbound.donation;

import org.charitable.app.domain.entity.donation.Donation;

public interface DonationRepository {
    Donation save (Donation donation);
}
