package org.charitable.app.domain.port.outbound.db.auth;

import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.model.auth.AuthStatus;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository {
    Optional<Auth> findByEmail(String email);
    Optional<Auth> findByPhone(String phoneNumber);
    Optional<Auth> findById(UUID id);
    Auth findMyInfo(UUID id);
    Auth updateAuthStatus(UUID id, AuthStatus authStatus);
    Auth save(Auth auth);
    Auth update(Auth auth);

    Optional<Auth> findByEmailVerificationToken(String token);
}
