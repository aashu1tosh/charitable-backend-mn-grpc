package org.charitable.app.infrastructure.adapter.inbound.grpc.request.donation;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import org.charitable.app.proto.*;

@Singleton
public class DonationGrpcService extends DonationServiceGrpc.DonationServiceImplBase {

    @Override
    public void donate(UserDonationRequest request, StreamObserver<CommonResponse> responseObserver) {
        CommonResponse response = CommonResponse.newBuilder()
                .setSuccess(true)
                .setMessage("Donation successful")
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
