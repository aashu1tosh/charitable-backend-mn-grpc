package org.charitable.app.infrastructure.adapter.inbound.grpc.interceptor.authentication;

import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.bind.ArgumentBinder;
import io.micronaut.core.convert.ArgumentConversionContext;
import jakarta.inject.Singleton;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.domain.model.token.TokenPayload;

import java.util.Optional;

@Singleton
public class AuthenticatedUserArgumentBinder implements ArgumentBinder<TokenPayload, MethodInvocationContext<?, ?>> {

    @Override
    public BindingResult<TokenPayload> bind(
            ArgumentConversionContext<TokenPayload> context,
            MethodInvocationContext<?, ?> source) {

        if (!context.getArgument().isAnnotationPresent(AuthenticatedUser.class)) {
            return BindingResult.UNSATISFIED;
        }

        Optional<TokenPayload> TokenPayload = source.getAttribute("TokenPayload", TokenPayload.class);

        if (TokenPayload.isEmpty()) {
            throw AppException.internal("");
        }

        return () -> TokenPayload;
    }

    public Class<MethodInvocationContext<?, ?>> argumentType() {
        return (Class) MethodInvocationContext.class;
    }
}