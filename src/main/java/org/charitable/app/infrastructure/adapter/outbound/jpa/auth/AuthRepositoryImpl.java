package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;

import jakarta.inject.Singleton;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.port.outbound.AuthRepository;
import org.charitable.app.infrastructure.adapter.inbound.grpc.request.auth.AuthGrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

@Singleton
class AuthRepositoryImpl implements AuthRepository {

    private static final Logger logger = LoggerFactory.getLogger(AuthGrpcService.class);

    private final AuthJpaRepository jpaRepository;

    public AuthRepositoryImpl(AuthJpaRepository  jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Auth> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(entity -> {
                    logger.info("Finding user by email: {}, found: {}", email, entity);
                    return mapToDomain(entity);
                });
    }

    @Override
    public Optional<Auth> findById(String id) {
        return jpaRepository.findById(UUID.fromString(id))
                .map(this::mapToDomain);
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
