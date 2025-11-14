package org.charitable.app.application.service.donation;


import jakarta.inject.Singleton;
import org.charitable.app.application.dto.request.donation.ClaimDonationRequestDTO;
import org.charitable.app.application.dto.request.donation.DonateRequestDTO;
import org.charitable.app.application.dto.request.donation.GetDonationFilterDTO;
import org.charitable.app.application.dto.request.donation.GotDonationRequestDTO;
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
import org.charitable.app.domain.port.outbound.organization.OrganizationRepository;
import org.charitable.app.proto.ClaimDonationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DonationService implements DonationUseCase {
    private static final Logger logger = LoggerFactory.getLogger(DonationService.class);

    private final DonationRepository donationRepo;
    private final AuthUseCase authService;
    private final OrganizationRepository organizationRepo;

    public DonationService(DonationRepository donationRepository, AuthUseCase authService, OrganizationRepository orgRepo) {
        this.donationRepo = donationRepository;
        this.authService = authService;
        this.organizationRepo = orgRepo;
    }

    @Override
    public Donation donate(DonateRequestDTO req, TokenPayload user) {
        var donation = Donation.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .status(DonationStatus.AVAILABLE)
                .type(req.getDonationType())
                .donor(authService.findById(user.getId()))
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .url(req.getUrl() != null ?  req.getUrl() : null)
                .build();

        return donationRepo.save(donation);
    }

    @Override
    public Page<Donation> getDonation(GetDonationFilterDTO request, TokenPayload user) {
        var filter = DonationFilter.builder()
                .limit(request.getLimit())
                .page(request.getPage())
                .search(request.getSearch())
                .status(request.getStatus())
                .type(request.getType())
                .build();

        var resp = donationRepo.getDonations(filter, user);
        logger.info("Get Donation Response: {}", resp);
        return resp;
    }

//    @Override
//    public Page<Donation> getMyDonation(GetDonationFilterDTO request, TokenPayload user) {
//        var filter = DonationFilter.builder()
//    }

    @Override
    public Donation claimDonation(ClaimDonationRequestDTO request, TokenPayload user) {
        var org = organizationRepo.findById(user.getOrganizationId());
        if(org.isEmpty()) {
            throw AppException.badRequest("Validate your organization");
        }

        var donation = donationRepo.findById(request.getId());

        if(donation.isEmpty()) {
            throw AppException.badRequest("Donation not found");
        }

        if(donation.get().getStatus() != DonationStatus.AVAILABLE) {
            throw AppException.badRequest("Donation not available");
        }

        return donationRepo.claimDonation(request.getId(), org.get());
    }

    @Override
    public Donation gotDonation(GotDonationRequestDTO req, TokenPayload user){
        var org = organizationRepo.findById(user.getOrganizationId());
        if(org.isEmpty()) {
            throw AppException.badRequest("Validate your organization");
        }

        var donationOp = donationRepo.findByIdWithRelations(req.getId());

        if(donationOp.isEmpty()) {
            throw AppException.badRequest("Donation not found");
        }

        var donation = donationOp.get();

        if(donation.getStatus() == DonationStatus.AVAILABLE || donation.getOrganization() == null)  {
            throw AppException.badRequest("Claim the donation first.");
        }

        if(user.getOrganizationId() != donation.getOrganization().getId()) {
            throw AppException.badRequest("This donation has been claimed by different organizations.");
        }

        return donationRepo.gotDonation(donation.getId());
    }
}
