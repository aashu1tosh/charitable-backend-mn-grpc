package org.charitable.app.infrastructure.config.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
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
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

@Singleton
@AllArgsConstructor
class JwtProvider implements AuthTokenManager {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private final JwtAuthenticationFactory jwtAuthenticationFactory;

    private final EnvVariables envVariables;

    @Override
    public IdentityTokens generateToken(Auth auth) {
        try {
            var accessToken = createAccessToken(auth);
            var refreshToken = createRefreshToken(auth);
            return new IdentityTokens(accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    private String createAccessToken(Auth auth) throws JOSEException {
        var secret = envVariables.getJwtAccessTokenSecret();
        var expiration = envVariables.getJwtAccessTokenExpiration();
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(auth.getId().toString())
                .issuer("charitable-backend")
                .issueTime(now)
                .expirationTime(exp)
                .claim("id", auth.getId().toString())
                .claim("role", auth.getRole().toString())
                .claim("organization_id", auth.getOrganization() != null ? auth.getOrganization().getId().toString() : null)
                .claim("user_id", auth.getUser() != null ? auth.getUser().getId().toString() : null)
                .build();

        return signToken(claims, secret);
    }

    private String createRefreshToken(Auth auth) throws JOSEException {
        var secret = envVariables.getJwtRefreshTokenSecret();
        var expiration = envVariables.getJwtRefreshTokenExpiration();
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(auth.getId().toString())
                .issuer("charitable-backend")
                .issueTime(now)
                .expirationTime(exp)
                .claim("role", auth.getRole().toString())
                .build();

        return signToken(claims, secret);
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
                throw AppException.unauthorized("TOKEN_EXPIRED");
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

            logger.info("User Information {}", tokenPayload);

            return tokenPayload;
        } catch (AppException e) {
            throw e;
        } catch (ParseException e) {
            throw AppException.unauthorized("Malformed token");
        } catch (Exception e) {
            throw AppException.internal("Error validating token: " + e.getMessage());
        }
    }


    private String signToken(JWTClaimsSet claims, String secret) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims
        );
        signedJWT.sign(new MACSigner(secret));
        return signedJWT.serialize();
    }
}
