package org.charitable.app.domain.entity.donation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.base.Base;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.model.donation.DonationStatus;
import org.charitable.app.domain.model.donation.DonationType;

@Getter
@Setter
@SuperBuilder
public class Donation extends Base {
    private String title;
    private String description;
    private String url;
    private float latitude;
    private float longitude;
    private Auth donor;
    private DonationType type;
    private DonationStatus status;
    private Organization organization;
}
