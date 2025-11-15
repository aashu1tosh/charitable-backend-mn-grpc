package org.charitable.app.infrastructure.adapter.inbound.grpc.request.donation;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import org.charitable.app.application.dto.request.donation.ClaimDonationRequestDTO;
import org.charitable.app.application.dto.request.donation.DonateRequestDTO;
import org.charitable.app.application.dto.request.donation.GetDonationFilterDTO;
import org.charitable.app.application.dto.request.donation.GotDonationRequestDTO;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.inbound.donation.DonationUseCase;
import org.charitable.app.common.utils.UUIDUtils;
import org.charitable.app.common.utils.ValidationUtils;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.domain.model.Role;
import org.charitable.app.infrastructure.adapter.inbound.grpc.context.GrpcContextKeys;
import org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication.GrpcAuthenticate;
import org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.donation.DonationMapper;
import org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.donation.DonationStatusMapper;
import org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.donation.DonationTypeMapper;
import org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.pagination.PaginationMapper;
import org.charitable.app.proto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DonationGrpcService extends DonationServiceGrpc.DonationServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(DonationGrpcService.class);

    private final DonationUseCase donationService;
    private final ValidationUtils validator;

    public DonationGrpcService(DonationUseCase donationService, ValidationUtils validator) {
        this.donationService = donationService;
        this.validator = validator;
    }

    @Override
    @GrpcAuthenticate(roles = {Role.USER})
    public void donate(UserDonationRequest request, StreamObserver<CommonResponse> responseObserver) {
        var req = DonateRequestDTO.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .donationType(DonationTypeMapper.fromProto(request.getType(), true))
                .latitude(request.getLat())
                .longitude(request.getLong())
                .url(request.getProductUrl())
                .build();

        validator.validate(req);
        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();

        var resp = donationService.donate(req, tokenPayload);

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Donated Successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @GrpcAuthenticate(roles = {Role.USER})
    public void getMyDonations(GetDonationRequest request, StreamObserver<GetDonationResponse> responseObserver) {
        var filter = GetDonationFilterDTO.builder()
                .limit(request.getLimit())
                .page(request.getPage())
                .search(request.getSearch())
                .type(DonationTypeMapper.fromProto(request.getType(), false))
                .status(DonationStatusMapper.fromProto(request.getStatus(), false))
                .build();

        validator.validate(filter);

        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();

        var resp = donationService.getDonation(filter,tokenPayload);
        var items = resp.getItems();

        logger.info("See what the response is: {}", resp);

        var pagination = PaginationMapper.toProtoPagination(resp.getPagination());

        PageDonation.Builder pageDonationBuilder = PageDonation.newBuilder();

        for (Donation domain : items) {
            DonationItems donationItem = DonationMapper.toProto(domain);
            pageDonationBuilder.addData(donationItem);
        }

        pageDonationBuilder.setPagination(pagination);
        PageDonation pageDonation = pageDonationBuilder.build();


        GetDonationResponse response = GetDonationResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Donated Successfully")
                .setData(pageDonation)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

        throw AppException.internal("Method not allowed");
    }

    @Override
    public void getDonations(GetDonationRequest request, StreamObserver<GetDonationResponse> responseObserver) {
        var filter = GetDonationFilterDTO.builder()
                .limit(request.getLimit() > 0 ? request.getLimit() : 10)
                .page(request.getPage() > 0 ? request.getPage() : 1)
                .search(request.getSearch())
                .type(DonationTypeMapper.fromProto(request.getType(), false))
                .status(DonationStatusMapper.fromProto(request.getStatus(), false))
                .build();

        validator.validate(filter);
        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();

        var resp = donationService.getDonation(filter,tokenPayload);
        var items = resp.getItems();

        logger.info("See what the response is: {}", resp);

        var pagination = PaginationMapper.toProtoPagination(resp.getPagination());

        PageDonation.Builder pageDonationBuilder = PageDonation.newBuilder();

        for (Donation domain : items) {
            DonationItems donationItem = DonationMapper.toProto(domain);
            pageDonationBuilder.addData(donationItem);
        }

        pageDonationBuilder.setPagination(pagination);
        PageDonation pageDonation = pageDonationBuilder.build();


        GetDonationResponse response = GetDonationResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Donated Successfully")
                .setData(pageDonation)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    @GrpcAuthenticate(roles = {Role.ORGANIZATION_ADMIN, Role.ORGANIZATION_SUPER_ADMIN})
    public void claimDonation(ClaimDonationRequest request, StreamObserver<CommonResponse> responseObserver) {
        var req = ClaimDonationRequestDTO.builder()
                .id(UUIDUtils.stringToUUID(request.getDonationId()))
                .build();

        validator.validate(req);

        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();

        var resp = donationService.claimDonation(req, tokenPayload);

        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Claim Success")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @GrpcAuthenticate(roles = {Role.ORGANIZATION_ADMIN, Role.ORGANIZATION_SUPER_ADMIN})
    public void gotDonation(GotDonationRequest request, StreamObserver<CommonResponse> responseObserver) {
        var req = GotDonationRequestDTO.builder()
                .id(UUIDUtils.stringToUUID(request.getDonationId()))
                .build();

        validator.validate(req);

        var tokenPayload = GrpcContextKeys.TOKEN_PAYLOAD_KEY.get();

        donationService.gotDonation(req, tokenPayload);
        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Action completed successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
