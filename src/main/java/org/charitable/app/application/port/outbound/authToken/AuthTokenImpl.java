package org.charitable.app.application.port.outbound.authToken;

import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.IdentityTokens;

public interface AuthTokenImpl {
    IdentityTokens generateToken(Auth auth);
    String validateAccessToken(String token);
}
