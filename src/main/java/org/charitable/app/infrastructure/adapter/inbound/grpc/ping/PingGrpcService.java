package org.charitable.app.infrastructure.adapter.inbound.grpc.ping;

import org.charitable.app.application.service.ping.PingService;
import org.charitable.app.domain.Ping;
import org.charitable.app.proto.PingReply;
import org.charitable.app.proto.PingRequest;
import org.charitable.app.proto.PingServiceGrpc;

import io.grpc.stub.StreamObserver;
import jakarta.inject.Singleton;

@Singleton
public class PingGrpcService extends PingServiceGrpc.PingServiceImplBase {

    private final PingService pingService;

    public PingGrpcService() {
        this.pingService = new PingService();
    }

    @Override
    public void ping(PingRequest request, StreamObserver<PingReply> responseObserver) {
        Ping ping = pingService.ping();

        PingReply reply = PingReply.newBuilder()
                .setMessage(ping.getMessage())
                .setTimestamp(ping.getTimestamp().toString())
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
