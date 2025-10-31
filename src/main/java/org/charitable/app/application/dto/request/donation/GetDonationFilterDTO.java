package org.charitable.app.application.dto.request.donation;

import io.micronaut.core.annotation.Introspected;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.charitable.app.domain.model.donation.DonationStatus;
import org.charitable.app.domain.model.donation.DonationType;

@Builder
@Introspected
@Getter
@Setter
public class GetDonationFilterDTO {
    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer limit = 10;

    private String search;
    private DonationType type;
    private DonationStatus status;
}
