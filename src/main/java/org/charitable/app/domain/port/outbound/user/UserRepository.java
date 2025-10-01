package org.charitable.app.domain.port.outbound.user;

import org.charitable.app.domain.entity.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);
    User save(User user);
}
