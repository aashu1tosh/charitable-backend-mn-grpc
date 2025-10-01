package org.charitable.app.infrastructure.adapter.inbound.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.charitable.app.application.exception.AppException;

public class GrpcExceptionMapper {

    public static StatusRuntimeException toGrpc(AppException ex) {
        return switch (ex.getType()) {
            case NOT_FOUND -> Status.NOT_FOUND.withDescription(ex.getMessage()).asRuntimeException();
            case UNAUTHORIZED -> Status.PERMISSION_DENIED.withDescription(ex.getMessage()).asRuntimeException();
            case BAD_REQUEST -> Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException();
            case CONFLICT -> Status.ALREADY_EXISTS.withDescription(ex.getMessage()).asRuntimeException();
            case INTERNAL -> Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException();
        };
    }
}