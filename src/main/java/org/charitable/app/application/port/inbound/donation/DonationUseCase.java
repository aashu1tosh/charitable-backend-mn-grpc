package org.charitable.app.application.port.inbound.donation;

import org.charitable.app.application.dto.request.donation.DonateRequestDTO;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.token.TokenPayload;

public interface DonationUseCase {
    Donation donate(DonateRequestDTO data, TokenPayload user);
}
