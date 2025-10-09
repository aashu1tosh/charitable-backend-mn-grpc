package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import io.micronaut.aop.Around;
import org.charitable.app.domain.model.Role;

import java.lang.annotation.*;

@Around
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface GrpcAuthenticated {
    Role[] roles() default {};
}