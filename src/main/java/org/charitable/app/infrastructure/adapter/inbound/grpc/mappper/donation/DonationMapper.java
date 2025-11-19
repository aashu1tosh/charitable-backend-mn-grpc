package org.charitable.app.infrastructure.adapter.inbound.grpc.mappper.donation;

import org.charitable.app.common.utils.UUIDUtils;
import org.charitable.app.common.utils.ValueUtils;
import org.charitable.app.domain.entity.donation.Donation;
import org.charitable.app.proto.DonationItems;

public class DonationMapper {

    public static DonationItems  toProto(Donation domain) {
        if(domain == null) {
            return null;
        }
        return DonationItems.newBuilder()
                .setId(!ValueUtils.checkNullOrEmpty(UUIDUtils.uuidToString(domain.getId())) ? UUIDUtils.uuidToString(domain.getId()) : "")
                .setCreatedAt(!ValueUtils.checkNullOrEmpty(domain.getCreatedAt()) ? domain.getCreatedAt().toString() : "")
                .setUpdatedAt(!ValueUtils.checkNullOrEmpty(domain.getUpdatedAt()) ? domain.getUpdatedAt().toString() : "")
                .setTitle(!ValueUtils.checkNullOrEmpty(domain.getTitle()) ? domain.getTitle() : "")
                .setDescription(!ValueUtils.checkNullOrEmpty(domain.getDescription()) ? domain.getDescription(): "")
                .setUrl(!ValueUtils.checkNullOrEmpty(domain.getUrlPath()) ? domain.getUrlPath() : "")
                .setType(DonationTypeMapper.toProto(domain.getType()))
                .setStatus(DonationStatusMapper.toProto(domain.getStatus()))
                .build();
    }
}
