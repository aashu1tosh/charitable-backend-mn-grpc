package org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.donation;

import lombok.NoArgsConstructor;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.domain.model.donation.DonationStatus;

@NoArgsConstructor
public final class DonationStatusMapper {

    public static DonationStatus fromProto(org.charitable.app.proto.DonationStatus protoStatus, boolean throwError) {
        return switch (protoStatus) {
            case AVAILABLE -> DonationStatus.AVAILABLE;
            case CLAIMED -> DonationStatus.CLAIMED;
            case DONATED-> DonationStatus.DONATED;
            case CANCELLED -> DonationStatus.CANCELLED;
            case DONATION_STATUS_UNSPECIFIED, UNRECOGNIZED -> {
                if (throwError) {
                    throw AppException.badRequest("Invalid donation type: " + protoStatus);
                } else {
                    yield null;
                }
            }
        };
    }

    public static org.charitable.app.proto.DonationStatus toProto(DonationStatus domainStatus) {
        return switch (domainStatus) {
            case AVAILABLE -> org.charitable.app.proto.DonationStatus.AVAILABLE;
            case CLAIMED -> org.charitable.app.proto.DonationStatus.CLAIMED;
            case DONATED -> org.charitable.app.proto.DonationStatus.DONATED;
            case CANCELLED -> org.charitable.app.proto.DonationStatus.CANCELLED;
        };
    }
}