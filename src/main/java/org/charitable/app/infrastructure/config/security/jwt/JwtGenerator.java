package org.charitable.app.infrastructure.config.security.jwt;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.charitable.app.application.port.outbound.authToken.AuthTokenImpl;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.IdentityTokens;

import java.util.*;

@Singleton
@AllArgsConstructor
public class JwtGenerator implements AuthTokenImpl {

    @Named("access")
    private final JwtTokenGenerator accessTokenGenerator;

    @Named("refresh")
    private final JwtTokenGenerator refreshTokenGenerator;

    @Override
    public IdentityTokens generateToken(Auth auth) {

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", auth.getId());
        attributes.put("role", auth.getRole());
        attributes.put("organization_id", auth.getOrganization() != null ? auth.getOrganization().getId() : null);
        attributes.put("user_id", auth.getUser() != null ? auth.getUser().getId() : null);

        var roles = Collections.singletonList(auth.getRole().toString());

        Authentication authentication = Authentication.build(auth.getId().toString(), roles, attributes);
        // expiration will be injected from configuration
        Optional<String> accessToken = accessTokenGenerator.generateToken(authentication, null);

        Optional<String> refreshToken = refreshTokenGenerator.generateToken(authentication, null);

        return new IdentityTokens(
                accessToken.orElseThrow(() -> new RuntimeException("Failed to generate access token")),
                refreshToken.orElseThrow(() -> new RuntimeException("Failed to generate refresh token"))
        );
    }
}
