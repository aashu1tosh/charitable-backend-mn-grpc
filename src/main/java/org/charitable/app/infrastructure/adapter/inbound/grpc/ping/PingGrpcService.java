package org.charitable.app.infrastructure.adapter.inbound.grpc.ping;

import org.charitable.app.application.port.inbound.ping.IPingUseCase;
import org.charitable.app.domain.model.ping.Ping;
import org.charitable.app.proto.PingReply;
import org.charitable.app.proto.PingRequest;
import org.charitable.app.proto.PingServiceGrpc;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class PingGrpcService extends PingServiceGrpc.PingServiceImplBase {

    private final IPingUseCase pingService;

    @Override
    public void ping(PingRequest request, StreamObserver<PingReply> responseObserver) {
        Ping ping = pingService.ping();

        PingReply reply = PingReply.newBuilder()
                .setSuccess(true)
                .setMessage(ping.getMessage())
                .setTimestamp(ping.getTimestamp().toString())
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
