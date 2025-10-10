package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import io.grpc.*;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AuthMetadataInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AuthMetadataInterceptor.class);
    public static final Context.Key<Metadata> METADATA_KEY = Context.key("grpc-metadata");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        log.debug("Intercepting gRPC call to capture metadata");

        // Store metadata in context so it can be accessed later
        Context context = Context.current().withValue(METADATA_KEY, headers);

        // Continue with the call in the new context
        return Contexts.interceptCall(context, call, headers, next);
    }
}