package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor;

import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.infrastructure.adapter.inbound.grpc.GrpcExceptionMapper;
import org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth.AuthGrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Singleton
public class GlobalGrpcExceptionInterceptor implements ServerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(GlobalGrpcExceptionInterceptor.class);

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
                } catch (AppException appEx) {
                    logger.warn("AppException caught in gRPC call: {}", appEx.getMessage());
                    call.close(GrpcExceptionMapper.toGrpc(appEx).getStatus(), new Metadata());
                }
                catch (ConstraintViolationException cve) {
                    logger.warn("Validation exception in gRPC call: {}", cve.getMessage());
                    String message = cve.getConstraintViolations().stream()
                            .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                            .collect(Collectors.joining(", "));
                    call.close(
                            Status.INVALID_ARGUMENT.withDescription(message),
                            new Metadata()
                    );
                }catch (Exception e) {
                    logger.error("Unexpected exception in gRPC call: {}", e.getMessage(), e);
                    call.close(
                            Status.INTERNAL.withDescription("Oops! Something went wrong.").withCause(e),
                            new Metadata()
                    );
                }
            }
        };
    }
}