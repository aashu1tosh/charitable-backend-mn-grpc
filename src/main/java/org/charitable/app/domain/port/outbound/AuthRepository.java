package org.charitable.app.domain.port.outbound;

import org.charitable.app.domain.entity.auth.Auth;

public interface AuthRepository {
    Auth findByEmail(String email);
    Auth findById(String id);
}
