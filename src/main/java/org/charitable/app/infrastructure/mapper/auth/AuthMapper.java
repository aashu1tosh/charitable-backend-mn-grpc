package org.charitable.app.infrastructure.mapper.auth;

import org.charitable.app.domain.entity.admin.Admin;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.entity.user.User;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.mapper.admin.AdminMapper;
import org.charitable.app.infrastructure.mapper.organization.OrganizationMapper;
import org.charitable.app.infrastructure.mapper.user.UserMapper;

public class AuthMapper {

    public static Auth mapToDomain(AuthEntity authEntity) {
        if (authEntity == null) {
            return null;
        }

        Organization orgDomain = authEntity.getOrganization() != null
                ? OrganizationMapper.mapToDomain(authEntity.getOrganization())
                : null;

        Admin adminDomain = authEntity.getAdmin() != null
                ? AdminMapper.mapToDomain(authEntity.getAdmin())
                : null;

        User userDomain = authEntity.getUser() != null
                ? UserMapper.mapToDomain(authEntity.getUser())
                : null;

        return Auth.builder()
                .id(authEntity.getId())
                .createdAt(authEntity.getCreatedAt())
                .updatedAt(authEntity.getUpdatedAt())
                .email(authEntity.getEmail())
                .phone(authEntity.getPhoneNumber())
                .role(authEntity.getRole())
                .isEmailVerified(authEntity.getIsEmailVerified())
                .status(authEntity.getStatus())
                .organization(orgDomain)
                .admin(adminDomain)
                .user(userDomain)
                .build();
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
                null,
                null,
                null
        );
        entity.setId(auth.getId());
        entity.setCreatedAt(auth.getCreatedAt());
        entity.setUpdatedAt(auth.getUpdatedAt());
        return entity;
    }

//    private static Auth getAuth(AuthEntity authEntity) {
//        var auth = new Auth(
//                authEntity.getEmail(),
//                null,
//                authEntity.getPhoneNumber(),
//                authEntity.getRole(),
//                authEntity.getIsEmailVerified(),
//                authEntity.getStatus(),
//                OrganizationMapper.mapToDomain(authEntity.getOrganization()),
//                UserMapper.mapToDomain(authEntity.getUser()),
//                AdminMapper.mapToDomain(authEntity.getAdmin())
//        );
//        auth.setId(authEntity.getId());
//        auth.setCreatedAt(authEntity.getCreatedAt());
//        auth.setUpdatedAt(authEntity.getUpdatedAt());
//        return auth;
//    }
}
