package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;

import jakarta.inject.Singleton;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.port.outbound.AuthRepository;

import java.util.UUID;

@Singleton
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthJpaRepository jpaRepository;

    public AuthRepositoryImpl(AuthJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Auth findByEmail(String email) {
        AuthEntity entity = jpaRepository.findByEmail(email);
        if (entity == null) return null;

        return mapToDomain(entity);
    }

    @Override
    public Auth findById(String id) {
        return jpaRepository.findById(UUID.fromString(id))
                .map(this::mapToDomain)
                .orElse(null);
    }

    private Auth mapToDomain(AuthEntity entity) {
        return new Auth(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getPhoneNumber(),
                entity.getRole()
        );
    }
}
