package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface GrpcAuthenticatedUser {
}