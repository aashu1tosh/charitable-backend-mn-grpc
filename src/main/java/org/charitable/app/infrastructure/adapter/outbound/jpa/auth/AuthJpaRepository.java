package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;


import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthJpaRepository extends JpaRepository<AuthEntity, UUID> {
    Optional<AuthEntity> findByEmail(String email);

    @Query("""
    SELECT a FROM AuthEntity a
    LEFT JOIN FETCH a.organization
    LEFT JOIN FETCH a.user
    WHERE a.id = :id
""")
    Optional<AuthEntity> findByIdWithRelations(UUID id);

}
