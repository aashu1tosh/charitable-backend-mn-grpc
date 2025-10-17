package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;
import org.charitable.app.domain.model.Role;

import java.lang.annotation.*;

@Around
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Type(GrpcAuthInterceptor.class)
public @interface GrpcAuthenticate {
    Role[] roles() default {};
}