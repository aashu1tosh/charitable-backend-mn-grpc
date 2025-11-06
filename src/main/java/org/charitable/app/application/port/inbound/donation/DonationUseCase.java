package org.charitable.app.application.port.inbound.donation;

import org.charitable.app.application.dto.request.donation.ClaimDonationRequestDTO;
import org.charitable.app.application.dto.request.donation.DonateRequestDTO;
import org.charitable.app.application.dto.request.donation.GetDonationFilterDTO;
import org.charitable.app.application.dto.request.donation.GotDonationRequestDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.token.TokenPayload;
import org.charitable.app.proto.GotDonationRequest;

public interface DonationUseCase {
    Donation donate(DonateRequestDTO data, TokenPayload user);
    Page<Donation> getDonation(GetDonationFilterDTO data, TokenPayload user);
    Donation claimDonation(ClaimDonationRequestDTO data, TokenPayload user);
    Donation gotDonation(GotDonationRequestDTO data, TokenPayload user);
}
