package org.charitable.app.application.port.outbound.authToken;

import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.IdentityTokens;
import org.charitable.app.domain.model.token.TokenPayload;

public interface AuthTokenManager {
    IdentityTokens generateToken(Auth auth);
    TokenPayload validateAccessToken(String token);
}
