package org.charitable.app.infrastructure.adapter.outbound.jpa.user;

import jakarta.inject.Singleton;
import org.charitable.app.domain.entity.user.User;
import org.charitable.app.domain.port.outbound.user.UserRepository;
import org.charitable.app.infrastructure.mapper.user.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::mapToDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getLatitude(),
                user.getLongitude(),
                null
        );
        UserEntity savedEntity = userJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }


    private User mapToDomain(UserEntity userEntity) {
        return UserMapper.mapToDomain(userEntity);
    }

}
