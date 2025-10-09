package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import io.grpc.*;
import io.micronaut.context.BeanContext;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.outbound.authToken.AuthTokenImpl;
import org.charitable.app.domain.model.Role;
//import org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication.AuthContext;
import org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication.GrpcAuthenticated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
@RequiredArgsConstructor
public class GrpcAuthInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrpcAuthInterceptor.class);
    private static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private static final Context.Key<String> AUTH_CONTEXT_KEY = Context.key("authContext");

    private final AuthTokenImpl authTokenService;
    private final BeanContext beanContext;

    // Cache to store which methods require authentication
    private final Map<String, MethodAuthConfig> methodAuthCache = new ConcurrentHashMap<>();

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String fullMethodName = call.getMethodDescriptor().getFullMethodName();

        // Check if this method requires authentication
        MethodAuthConfig authConfig = getMethodAuthConfig(fullMethodName);

        if (!authConfig.requiresAuth) {
            // Method doesn't require authentication, proceed normally
            return next.startCall(call, headers);
        }

        // Extract token from metadata
        String authHeader = headers.get(AUTHORIZATION_METADATA_KEY);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            call.close(Status.UNAUTHENTICATED.withDescription("Missing or invalid Authorization header"), headers);
            return new ServerCall.Listener<ReqT>() {};
        }

        String token = authHeader.substring(7);
        log.info("Grpc Interceptor Validating token: {}", token);

        try {
            // Validate token
            String authContext = authTokenService.validateAccessToken(token);

            // Check roles if required
//            if (authConfig.requiredRoles != null && authConfig.requiredRoles.length > 0) {
//                boolean hasRole = Arrays.asList(authConfig.requiredRoles).contains(authContext.getRole());
//                if (!hasRole) {
//                    call.close(Status.PERMISSION_DENIED.withDescription("Insufficient permissions"), headers);
//                    return new ServerCall.Listener<ReqT>() {};
//                }
//            }

            // Store in gRPC context
            Context context = Context.current().withValue(AUTH_CONTEXT_KEY, authContext);

            // Continue with the call in the new context
            return Contexts.interceptCall(context, call, headers, next);

        } catch (Exception e) {
            call.close(Status.UNAUTHENTICATED.withDescription("Invalid token: " + e.getMessage()), headers);
            return new ServerCall.Listener<ReqT>() {};
        }
    }

    private MethodAuthConfig getMethodAuthConfig(String fullMethodName) {
        return methodAuthCache.computeIfAbsent(fullMethodName, this::checkMethodAnnotation);
    }

    private MethodAuthConfig checkMethodAnnotation(String fullMethodName) {
        // Parse the method name: "package.ServiceName/MethodName"
        String[] parts = fullMethodName.split("/");
        if (parts.length != 2) {
            return new MethodAuthConfig(false, null);
        }

        String serviceName = parts[0];
        String methodName = parts[1];

        // Convert gRPC method name to Java method name (camelCase)
        String javaMethodName = toCamelCase(methodName);

        try {
            for (Object bean : beanContext.getBeansOfType(Object.class)) {
                Class<?> beanClass = bean.getClass();

                // Check if it’s a gRPC service class
                if (!beanClass.getSimpleName().contains(serviceName.split("\\.")[serviceName.split("\\.").length - 1])) {
                    continue;
                }

                // Check class-level annotation
                GrpcAuthenticated classAnnotation = beanClass.getAnnotation(GrpcAuthenticated.class);

                for (Method method : beanClass.getDeclaredMethods()) {
                    if (method.getName().equals(javaMethodName)) {
                        GrpcAuthenticated methodAnnotation = method.getAnnotation(GrpcAuthenticated.class);

                        // ✅ Method annotation takes precedence
                        if (methodAnnotation != null) {
                            return new MethodAuthConfig(true, methodAnnotation.roles());
                        }

                        // ✅ Fallback to class annotation
                        if (classAnnotation != null) {
                            return new MethodAuthConfig(true, classAnnotation.roles());
                        }

                        // Otherwise, no auth required
                        return new MethodAuthConfig(false, null);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to check @GrpcAuthenticated for {}: {}", fullMethodName, e.getMessage());
            return new MethodAuthConfig(false, null);
        }

        return new MethodAuthConfig(false, null);
    }


    private String toCamelCase(String grpcMethodName) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (int i = 0; i < grpcMethodName.length(); i++) {
            char c = grpcMethodName.charAt(i);
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }

        return result.toString();
    }

//    public static AuthContext getCurrentAuthContext() {
//        AuthContext authContext = AUTH_CONTEXT_KEY.get();
//        if (authContext == null) {
//            throw AppException.unauthorized("No authenticated user in context");
//        }
//        return authContext;
//    }



    private static class MethodAuthConfig {
        final boolean requiresAuth;
        final Role[] requiredRoles;

        MethodAuthConfig(boolean requiresAuth, Role[] requiredRoles) {
            this.requiresAuth = requiresAuth;
            this.requiredRoles = requiredRoles;
        }
    }
}