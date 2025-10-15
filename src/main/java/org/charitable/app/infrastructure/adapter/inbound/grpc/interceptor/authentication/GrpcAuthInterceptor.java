package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import io.grpc.*;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.outbound.authToken.AuthTokenManager;
import org.charitable.app.domain.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor
public class GrpcAuthInterceptor implements MethodInterceptor<Object, Object> {

    private static final Logger log = LoggerFactory.getLogger(GrpcAuthInterceptor.class);
    private static final String AUTHORIZATION_HEADER = "authorization"; // lowercase!
    private static final String BEARER_PREFIX = "Bearer ";

    private AuthTokenManager authTokenService;

    public GrpcAuthInterceptor(AuthTokenManager authTokenImpl) {
        this.authTokenService = authTokenImpl;
    }

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        // Access annotation
        AnnotationValue<GrpcAuthenticated> annotation =
                context.findAnnotation(GrpcAuthenticated.class)
                        .orElseThrow(() -> new IllegalStateException("Authentication annotation not present"));

        log.debug("Processing GrpcAuthenticated annotation");

        // Extract token from gRPC metadata
        String token = extractTokenFromMetadata();

        if (token == null || token.isEmpty()) {
            log.error("Authorization token not found in gRPC metadata");
            throw AppException.unauthorized("Authorization token is required");
        }

        try {
            var tokenPayload = authTokenService.validateAccessToken(token);

            if(annotation != null) {
                Optional<Role[]> requiredRoles  = annotation.get("roles", Role[].class);
                if (requiredRoles.isPresent() && requiredRoles.get().length > 0) {
                    System.out.println("Required roles: "+ Arrays.toString(requiredRoles.get()));
                    var role = tokenPayload.getRole();
                    var containsRole = Arrays.asList(requiredRoles.get()).contains(role);
                    if(!containsRole) {
                        throw AppException.unauthorized("You are not authorized to access this resource");
                    }
                }
            }

            context.setAttribute("TokenPayload", tokenPayload);
            return context.proceed();
        } catch (Exception ex) {
            log.error("Exception while processing GrpcAuthenticated annotation", ex);
            throw AppException.unauthorized("Authentication failed");
        } finally {
            log.debug("Completed method execution");
        }
    }

    private String extractTokenFromMetadata() {
        try {
            // Get metadata from context (stored by AuthMetadataInterceptor)
            Metadata metadata = AuthMetadataInterceptor.METADATA_KEY.get();

            if (metadata == null) {
                log.warn("No metadata found in gRPC context");
                return null;
            }

            // Get authorization header (lowercase!)
            Metadata.Key<String> authKey = Metadata.Key.of(AUTHORIZATION_HEADER, Metadata.ASCII_STRING_MARSHALLER);
            String authHeader = metadata.get(authKey);

            if (authHeader == null) {
                log.warn("Authorization header not found in metadata");
                return null;
            }

            log.debug("Raw authorization header: {}", authHeader);

//             Remove "Bearer " prefix if present
            if (authHeader.startsWith(BEARER_PREFIX)) {
                return authHeader.substring(BEARER_PREFIX.length());
            }

            return null;
        } catch (Exception e) {
            log.error("Error extracting token from metadata", e);
            return null;
        }
    }
}