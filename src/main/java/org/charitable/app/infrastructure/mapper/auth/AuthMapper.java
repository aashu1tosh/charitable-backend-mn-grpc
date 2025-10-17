package org.charitable.app.infrastructure.mapper.auth;

import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.mapper.organization.OrganizationMapper;
import org.charitable.app.infrastructure.mapper.user.UserMapper;

public class AuthMapper {

    public static Auth mapToDomain(AuthEntity authEntity) {
        if (authEntity == null) {
            return null;
        }

        return getAuth(authEntity);
    }

    public static AuthEntity mapToEntity(Auth auth) {
        if (auth == null) {
            return null;
        }

        var entity = new AuthEntity(
                auth.getEmail(),
                null,
                auth.getPhone(),
                auth.getRole(),
                auth.getIsEmailVerified(),
                auth.getStatus(),
                OrganizationMapper.mapToEntitySafe(auth.getOrganization()),
                null
        );
        entity.setId(auth.getId());
        entity.setCreatedAt(auth.getCreatedAt());
        entity.setUpdatedAt(auth.getUpdatedAt());
        return entity;
    }


    private static Auth getAuth(AuthEntity authEntity) {
        var auth = new Auth(
                authEntity.getEmail(),
                null,
                authEntity.getPhoneNumber(),
                authEntity.getRole(),
                authEntity.getIsEmailVerified(),
                authEntity.getStatus(),
                OrganizationMapper.mapToDomain(authEntity.getOrganization()),
                UserMapper.mapToDomain(authEntity.getUser())
        );
        auth.setId(authEntity.getId());
        auth.setCreatedAt(authEntity.getCreatedAt());
        auth.setUpdatedAt(authEntity.getUpdatedAt());
        return auth;
    }
}
