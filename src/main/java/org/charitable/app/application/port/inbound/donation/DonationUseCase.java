package org.charitable.app.application.port.inbound.donation;

import org.charitable.app.application.dto.request.donation.DonateRequestDTO;
import org.charitable.app.application.dto.request.donation.GetDonationFilterDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.token.TokenPayload;

public interface DonationUseCase {
    AppResponse<Donation> donate(DonateRequestDTO data, TokenPayload user);
    AppResponse<Page<Donation>> getDonation(GetDonationFilterDTO data, TokenPayload user);
}
