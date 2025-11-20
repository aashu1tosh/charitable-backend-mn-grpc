package org.charitable.app.domain.entity.donation.donationStatusHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.charitable.app.domain.entity.base.Base;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.donation.DonationStatus;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class DonationStatusHistory extends Base {
    private Donation donation;
    private DonationStatus status;
}