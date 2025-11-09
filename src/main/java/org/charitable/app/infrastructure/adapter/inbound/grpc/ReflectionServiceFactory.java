package org.charitable.app.infrastructure.adapter.inbound.grpc;

import io.grpc.BindableService;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;

@Factory
public class ReflectionServiceFactory {

    @Singleton
    @Requires(property = "grpc.server.reflection.enabled", value = "true", defaultValue = "false")
    public BindableService reflectionService() {
        return ProtoReflectionService.newInstance();
    }
}
