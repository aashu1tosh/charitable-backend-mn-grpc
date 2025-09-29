package org.charitable.app.domain.port.outbound;

import org.charitable.app.domain.entity.auth.Auth;

import java.util.Optional;

public interface AuthRepository {
    Optional<Auth> findByEmail(String email);
    Optional<Auth> findById(String id);
}
