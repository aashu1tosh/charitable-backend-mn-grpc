package org.charitable.app.domain.port.outbound.db.donation.donationStatusHistoryRespository;

import org.charitable.app.domain.entity.auth.authStatusHistory.AuthStatusHistory;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.entity.donation.donationStatusHistory.DonationStatusHistory;
import org.charitable.app.domain.model.donation.DonationStatus;

public interface DonationStatusHistoryRepository {
    DonationStatusHistory save(Donation donation, DonationStatus status);
}
