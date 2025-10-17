package org.charitable.app.infrastructure.adapter.outbound.jpa.user;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findById(UUID id);

    @Query("SELECT u FROM UserEntity u JOIN u.auth a WHERE a.email = :email")
    Optional<UserEntity> findByEmail(String email);

}
