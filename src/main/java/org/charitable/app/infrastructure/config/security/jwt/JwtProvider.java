package org.charitable.app.infrastructure.config.security.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.micronaut.security.token.jwt.validator.JwtAuthenticationFactory;
import io.micronaut.security.token.jwt.validator.ReactiveJsonWebTokenValidator;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.charitable.app.application.exception.AppException;
import org.charitable.app.application.port.outbound.authToken.AuthTokenManager;
import org.charitable.app.common.utils.UUIDUtils;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.IdentityTokens;
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.token.TokenPayload;
import org.charitable.app.infrastructure.config.environment.EnvVariables;

import java.text.ParseException;
import java.util.*;

@Singleton
@AllArgsConstructor
class JwtProvider implements AuthTokenManager {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JwtProvider.class);

    @Named("access")
    private final JwtTokenGenerator accessTokenGenerator;

    @Named("refresh")
    private final JwtTokenGenerator refreshTokenGenerator;

    private final JwtAuthenticationFactory jwtAuthenticationFactory;

    private final EnvVariables envVariables;

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


    @Override
    public TokenPayload validateAccessToken(String token) {
        try {
            logger.info("Provided token: " + token);

            // 1️⃣ Parse the token
            SignedJWT signedJWT = SignedJWT.parse(token);

            var jwtSecret = envVariables.getJwtAccessTokenSecret();
            JWSVerifier verifier = new MACVerifier(jwtSecret);
            boolean validSignature = signedJWT.verify(verifier);

            if (!validSignature) {
                throw AppException.unauthorized("Invalid token signature");
            }

            if (signedJWT.getJWTClaimsSet().getExpirationTime() != null &&
                    signedJWT.getJWTClaimsSet().getExpirationTime().before(new java.util.Date())) {
                throw AppException.unauthorized("Token has expired");
            }

            Optional<Authentication> authentication = jwtAuthenticationFactory.createAuthentication(signedJWT);

            if (authentication.isEmpty()) {
                throw AppException.unauthorized("Invalid or expired token");
            }

            Map<String, Object> attributes = authentication.get().getAttributes();
            logger.info("Authentication attributes: " + attributes);


            var tokenPayload = TokenPayload.builder()
                    .id((UUID) UUIDUtils.stringToUUID(attributes.get("id").toString()))
                    .role(Role.valueOf(attributes.get("role").toString()))
                    .organizationId(attributes.get("organizationId") != null ? (UUID) attributes.get("organizationId") : null)
                    .adminId(attributes.get("adminId") != null ? (UUID) attributes.get("adminId") : null)
                    .userId(attributes.get("userId") != null ? (UUID) attributes.get("userId") : null)
                    .build();

            return tokenPayload;
        } catch (AppException e) {
            throw e;
        } catch (ParseException e) {
            throw AppException.unauthorized("Malformed token");
        } catch (Exception e) {
            throw AppException.internal("Error validating token: " + e.getMessage());
        }
    }
}
