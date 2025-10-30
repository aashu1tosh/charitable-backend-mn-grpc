package org.charitable.app.application.port.inbound.donation;

import org.charitable.app.domain.entity.donation.Donation;

public interface DonationUseCase {
    Donation donate(Object data);
}
