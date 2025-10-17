package org.charitable.app.infrastructure.adapter.inbound.grpc.context;

import io.grpc.Context;
import org.charitable.app.domain.model.token.TokenPayload;

public class GrpcContextKeys {

    public static final Context.Key<TokenPayload> TOKEN_PAYLOAD_KEY =
            Context.key("tokenPayload");
}