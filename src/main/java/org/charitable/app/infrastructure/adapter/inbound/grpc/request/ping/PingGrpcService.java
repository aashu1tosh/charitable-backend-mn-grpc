package org.charitable.app.infrastructure.adapter.inbound.grpc.request.ping;

import org.charitable.app.application.port.inbound.ping.PingUseCase;
import org.charitable.app.proto.PingReply;
import org.charitable.app.proto.PingRequest;
import org.charitable.app.proto.PingServiceGrpc;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class PingGrpcService extends PingServiceGrpc.PingServiceImplBase {

    private final PingUseCase pingService;

    @Override
    public void ping(PingRequest request, StreamObserver<PingReply> responseObserver) {
        var ping = pingService.ping();

        PingReply reply = PingReply.newBuilder()
                .setSuccess(true)
                .setMessage(ping.getMessage())
                .setTimestamp(ping.getTimestamp())
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
