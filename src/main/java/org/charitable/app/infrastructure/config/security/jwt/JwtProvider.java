package org.charitable.app.infrastructure.config.security.jwt;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.micronaut.context.annotation.Bean;
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
import java.time.Instant;
import java.util.*;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;


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

    @Bean
    public SecretKey accessTokenKey() {
        String secret = envVariables.getJwtAccessTokenSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_ACCESS_SECRET environment variable not set");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Bean
    public SecretKey refreshTokenKey() {
        String secret = envVariables.getJwtRefreshTokenSecret();
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("JWT_REFRESH_SECRET environment variable not set");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public IdentityTokens generateToken(Auth auth) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", auth.getId());
        attributes.put("role", auth.getRole());
        attributes.put("organizationId", auth.getOrganization() != null ? auth.getOrganization().getId() : null);
        attributes.put("userId", auth.getUser() != null ? auth.getUser().getId() : null);

        String accessToken = generateAccessToken(auth.getId().toString(), auth.getRole().toString(), attributes);
        String refreshToken = generateRefreshToken(auth.getId().toString(), auth.getRole().toString(), attributes);

        return new IdentityTokens(accessToken, refreshToken);
    }

    private String generateAccessToken(String userId, String role, Map<String, Object> attributes) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(3600); // 1 hour

        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .claims(attributes)
                .issuedAt(java.util.Date.from(now))
                .expiration(java.util.Date.from(expirationTime))
                .signWith(accessTokenKey())
                .compact();
    }

    private String generateRefreshToken(String userId, String role, Map<String, Object> attributes) {
        Instant now = Instant.now();
        Instant expirationTime = now.plusSeconds(2592000); // 30 days

        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .claims(attributes)
                .issuedAt(java.util.Date.from(now))
                .expiration(java.util.Date.from(expirationTime))
                .signWith(refreshTokenKey())
                .compact();
    }
    @Override
    public TokenPayload validateAccessToken(String token) {
        try {
            logger.info("Provided token: " + token);

            // 1️⃣ Parse the token
            SignedJWT signedJWT = SignedJWT.parse(token);

            var jwtSecret = envVariables.getJwtAccessTokenSecret();
            logger.info("Jwt Secret: {}", jwtSecret);
            JWSVerifier verifier = new MACVerifier(jwtSecret);
            boolean validSignature = signedJWT.verify(verifier);

            logger.info("JWT Signature verified: {}", validSignature);
            if (!validSignature) {
                throw AppException.unauthorized("Please login again.");
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
            throw AppException.internal("Error validating token. Please try again.");
        }
    }
}
