package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

@Repository
public interface AuthRepository extends CrudRepository<AuthEntity, UUID> {
    boolean existsByEmail(String email);

    AuthEntity findByEmail(String email);
}
