package org.charitable.app.infrastructure.grpc.mapper;

import lombok.NoArgsConstructor;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.domain.model.auth.AuthStatus;

@NoArgsConstructor
public final class AuthStatusMapper {

    public static AuthStatus fromProto(org.charitable.app.proto.AuthStatus protoStatus) {
        return switch (protoStatus) {
            case ACTIVE -> AuthStatus.ACTIVE;
            case INACTIVE -> AuthStatus.INACTIVE;
            case SUSPENDED -> AuthStatus.SUSPENDED;
            case DELETED -> AuthStatus.DELETED;
            case STATUS_UNSPECIFIED, UNRECOGNIZED -> throw AppException.badRequest("Invalid status: " + protoStatus);
        };
    }

    public static org.charitable.app.proto.AuthStatus toProto(AuthStatus domainStatus) {
        return switch (domainStatus) {
            case ACTIVE -> org.charitable.app.proto.AuthStatus.ACTIVE;
            case INACTIVE -> org.charitable.app.proto.AuthStatus.INACTIVE;
            case SUSPENDED -> org.charitable.app.proto.AuthStatus.SUSPENDED;
            case DELETED -> org.charitable.app.proto.AuthStatus.DELETED;
        };
    }
}
