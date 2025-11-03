package org.charitable.app.application.service.donation;


import jakarta.inject.Singleton;
import org.charitable.app.application.dto.request.donation.DonateRequestDTO;
import org.charitable.app.application.dto.request.donation.GetDonationFilterDTO;
import org.charitable.app.application.dto.response.AppResponse;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.inbound.auth.AuthUseCase;
import org.charitable.app.application.port.inbound.donation.DonationUseCase;
import org.charitable.app.domain.common.pagination.Page;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.donation.DonationFilter;
import org.charitable.app.domain.model.donation.DonationStatus;
import org.charitable.app.domain.model.token.TokenPayload;
import org.charitable.app.domain.port.outbound.donation.DonationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DonationService implements DonationUseCase {
    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    private final DonationRepository donationRepo;
    private final AuthUseCase authService;

    public DonationService(DonationRepository donationRepository, AuthUseCase authService) {
        this.donationRepo = donationRepository;
        this.authService = authService;
    }

    @Override
    public AppResponse<Donation> donate(DonateRequestDTO req, TokenPayload user) {
        var donation = Donation.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .status(DonationStatus.AVAILABLE)
                .type(req.getDonationType())
                .donor(authService.findById(user.getId()))
                .url(req.getUrl() != null ?  req.getUrl() : null)
                .build();

        return new AppResponse<>(true, "Donation Successful", donation);
    }

    @Override
    public AppResponse<Page<Donation>> getDonation(GetDonationFilterDTO request, TokenPayload user) {
        var filter = DonationFilter.builder()
                .limit(request.getLimit())
                .page(request.getPage())
                .search(request.getSearch())
                .status(request.getStatus())
                .type(request.getType())
                .build();

        var resp = donationRepo.getDonations(filter, user);
        return new AppResponse<>(true, "Fetched Successfully", resp);
    }
}
