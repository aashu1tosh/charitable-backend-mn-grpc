package org.charitable.app.infrastructure.config.security.jwt;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.micronaut.security.token.jwt.validator.JwtValidator;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.outbound.authToken.AuthTokenManager;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.IdentityTokens;

import java.util.*;

@Singleton
@AllArgsConstructor
class JwtProvider implements AuthTokenManager {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtProvider.class);

    @Named("access")
    private final JwtTokenGenerator accessTokenGenerator;

    @Named("refresh")
    private final JwtTokenGenerator refreshTokenGenerator;

    @Named("access")
    private final JwtValidator accessTokenValidator;



    @Override
    public IdentityTokens generateToken(Auth auth) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", auth.getId());
        attributes.put("role", auth.getRole());
        attributes.put("organization_id", auth.getOrganization() != null ? auth.getOrganization().getId() : null);
        attributes.put("user_id", auth.getUser() != null ? auth.getUser().getId() : null);

        var roles = Collections.singletonList(auth.getRole().toString());

        Authentication authentication = Authentication.build(auth.getId().toString(), roles, attributes);

        Optional<String> accessToken = accessTokenGenerator.generateToken(authentication, null);
        Optional<String> refreshToken = refreshTokenGenerator.generateToken(authentication, null);

        return new IdentityTokens(
                accessToken.orElseThrow(() -> new RuntimeException("Failed to generate access token")),
                refreshToken.orElseThrow(() -> new RuntimeException("Failed to generate refresh token"))
        );
    }

    @Override
    public String validateAccessToken(String token) {
        try {
            logger.info("Validating token: " + token);

            // This validates the signature AND expiration
            Optional<Authentication> authentication = accessTokenValidator.validate(token, null);

            if (authentication.isEmpty()) {
                throw AppException.unauthorized("Invalid or expired token");
            }

            // Extract the ID from authentication attributes
            Map<String, Object> attributes = authentication.get().getAttributes();
            logger.info("Authentication attributes: " + attributes);

            Object idObj = attributes.get("id");
            if (idObj == null) {
                throw AppException.unauthorized("Token missing required 'id' claim");
            }

            return idObj.toString();

        } catch (Exception e) {
            logger.error("Token validation failed", e);
            throw AppException.unauthorized("Invalid token: " + e.getMessage());
        }
    }
}