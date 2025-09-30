package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import jakarta.inject.Singleton;
import org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth.AuthGrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class GlobalGrpcExceptionInterceptor implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<>(delegate) {
            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception e) {
                    logger.info("Exception caught in gRPC call: {}", e.getMessage(), e);
                    call.close(
                            Status.INTERNAL.withDescription("Oops! Something went wrong."),
                            new Metadata());
                }
            }
        };
    }
}
