package org.charitable.app.infrastructure.mapper.user;

import org.charitable.app.domain.entity.user.User;
import org.charitable.app.infrastructure.adapter.outbound.jpa.user.UserEntity;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.hibernate.Hibernate;

public class UserMapper {

    public static User mapToDomain(UserEntity user) {
        if (user == null) return null;

        return getUser(user);
    }


    public static UserEntity mapToSafeEntity(User user) {
        if (user == null) return null;

        var entity = new UserEntity(
                user.getFirstName(),
                user.getMiddleName(),
                user.getLastName(),
                user.getLatitude(),
                user.getLongitude(),
                AuthMapper.mapToEntity(user.getAuth())
        );
        entity.setId(user.getId());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    private static User getUser(UserEntity userEntity) {
        if(userEntity == null) return null;

        // Only map if the entity is initialized
        if (!Hibernate.isInitialized(userEntity)) {
            return null;
        }

        var user = new User(
                userEntity.getFirstName(),
                userEntity.getMiddleName(),
                userEntity.getLastName(),
                userEntity.getLatitude(),
                userEntity.getLongitude(),
                AuthMapper.mapToDomain(userEntity.getAuth())
        );
        user.setId(userEntity.getId());
        user.setCreatedAt(userEntity.getCreatedAt());
        user.setUpdatedAt(userEntity.getUpdatedAt());
        return user;
    }
}
