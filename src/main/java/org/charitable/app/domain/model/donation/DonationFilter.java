package org.charitable.app.domain.model.donation;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DonationFilter {
    private final String search;
    private final DonationType type;
    private final DonationStatus status;
    private final int page;
    private final int limit;

}

