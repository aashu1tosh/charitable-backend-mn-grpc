package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import io.grpc.*;
import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.AnnotationValue;
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
public class GrpcAuthInterceptor implements MethodInterceptor<Object, Object> {

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        // Access annotation values:
        AnnotationValue<GrpcAuthenticated> annotation =
                context.findAnnotation(GrpcAuthenticated.class)
                        .orElseThrow(() -> new IllegalStateException("Authentication not present"));

        if (annotation != null) {
            System.out.println("Custom annotation value: " + annotation.getAnnotationName());
        }

        // Perform pre-processing logic
        System.out.println("Before method execution with MyCustomAnnotation");

        try {
            // Proceed with the original method execution
            return context.proceed();
        } finally {
            // Perform post-processing logic
            System.out.println("After method execution with MyCustomAnnotation");
        }
    }
}