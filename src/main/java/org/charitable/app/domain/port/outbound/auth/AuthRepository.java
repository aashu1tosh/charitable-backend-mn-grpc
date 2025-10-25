package org.charitable.app.domain.port.outbound.auth;

import org.charitable.app.domain.entity.auth.Auth;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository {
    Optional<Auth> findByEmail(String email);
    Auth findById(UUID id);
    Auth findMyInfo(UUID id);
    Auth save(Auth auth);
}
