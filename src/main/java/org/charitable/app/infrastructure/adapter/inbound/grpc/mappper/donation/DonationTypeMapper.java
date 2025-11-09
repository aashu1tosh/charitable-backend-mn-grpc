package org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.donation;

import lombok.NoArgsConstructor;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.domain.model.donation.DonationType;

@NoArgsConstructor
public final class DonationTypeMapper {

    public static DonationType fromProto(org.charitable.app.proto.DonationType protoStatus, boolean throwError) {
        return switch (protoStatus) {
            case BOOKS -> DonationType.BOOKS;
            case CLOTHES -> DonationType.CLOTHES;
            case DONATION_UNSPECIFIED, UNRECOGNIZED -> {
                if (throwError) {
                    throw AppException.badRequest("Invalid donation type: " + protoStatus);
                } else {
                    yield null;
                }
            }
        };
    }


    public static org.charitable.app.proto.DonationType toProto(DonationType domainStatus) {
        return switch (domainStatus) {
            case BOOKS -> org.charitable.app.proto.DonationType.BOOKS;
            case CLOTHES -> org.charitable.app.proto.DonationType.CLOTHES;
        };
    }
}