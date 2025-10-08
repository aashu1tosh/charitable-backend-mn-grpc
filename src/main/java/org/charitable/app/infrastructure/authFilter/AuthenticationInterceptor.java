//package org.charitable.app.infrastructure.authFilter;
//
//import io.micronaut.aop.MethodInterceptor;
//import io.micronaut.aop.MethodInvocationContext;
//import io.micronaut.context.annotation.Requires;
//import jakarta.inject.Singleton;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@Singleton
//@Requires(beans = JwtTokenProvider.class)
//public class AuthenticatedInterceptor implements MethodInterceptor<Object, Object> {
//
//    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedInterceptor.class);
//    private final JwtTokenProvider jwtTokenProvider;
//    private final RequestContextHolder requestContextHolder;
//
//    public AuthenticatedInterceptor(JwtTokenProvider jwtTokenProvider, RequestContextHolder requestContextHolder) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.requestContextHolder = requestContextHolder;
//    }
//
//    @Override
//    public Object intercept(MethodInvocationContext<Object, Object> context) {
//        logger.info("🔐 Running @Authenticated interceptor for method: {}", context.getMethodName());
//
//        // Extract JWT token from metadata (you can adapt this for HTTP headers if needed)
//        var metadata = context.getParameterValues();
//        String token = JwtUtils.extractTokenFromGrpcContext();
//        if (token == null) {
//            throw new RuntimeException("Missing or invalid token");
//        }
//
//        var auth = jwtTokenProvider.validateToken(token);
//        if (auth == null) {
//            throw new RuntimeException("Invalid JWT token");
//        }
//
//        // Store auth info for the request
//        requestContextHolder.setAuth(auth);
//
//        try {
//            return context.proceed(); // continue actual method execution
//        } finally {
//            requestContextHolder.clear(); // cleanup after request
//        }
//    }
//}