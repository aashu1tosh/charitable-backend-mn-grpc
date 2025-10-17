package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;


import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthJpaRepository extends JpaRepository<AuthEntity, UUID> {
    Optional<AuthEntity> findByEmail(String email);
}
