package org.charitable.app.infrastructure.mapper.auth;

import lombok.extern.slf4j.Slf4j;
import org.charitable.app.common.utils.PrintUtils;
import org.charitable.app.common.utils.ValueUtils;
import org.charitable.app.domain.entity.admin.Admin;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.organization.Organization;
import org.charitable.app.domain.entity.user.User;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.mapper.admin.AdminMapper;
import org.charitable.app.infrastructure.mapper.organization.OrganizationMapper;
import org.charitable.app.infrastructure.mapper.user.UserMapper;
import org.hibernate.Hibernate;

@Slf4j
public class AuthMapper {

    public static Auth mapToDomain(AuthEntity authEntity) {
        if (authEntity == null) {
            return null;
        }

        if (!Hibernate.isInitialized(authEntity)) {
            return null;
        }
        log.info("See what the entity is {}", PrintUtils.prettyPrint(authEntity));

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
                .password(authEntity.getPassword())
                .role(authEntity.getRole())
                .isEmailVerified(authEntity.getIsEmailVerified())
                .emailVerificationPublishAt(authEntity.getEmailVerificationPublishAt())
                .emailVerificationToken(authEntity.getEmailVerificationToken())
                .status(authEntity.getStatus())
                .organization(orgDomain)
                .admin(adminDomain)
                .user(userDomain)
                .build();
    }

    public static AuthEntity mapToEntity(Auth domain) {
        if (domain == null) {
            return null;
        }

        return AuthEntity.builder()
                .id(!ValueUtils.checkNullOrEmpty(domain.getId()) ? domain.getId() : null)
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .email(domain.getEmail())
                .role(domain.getRole())
                .isEmailVerified(domain.getIsEmailVerified())
                .status(domain.getStatus())
                .phoneNumber(domain.getPhone())
                .organization(domain.getOrganization() != null ? OrganizationMapper.mapToEntitySafe(domain.getOrganization()) : null)
                .user(domain.getUser() != null ? UserMapper.mapToSafeEntity(domain.getUser()) : null)
                .admin(domain.getAdmin() != null ? AdminMapper.mapToSafeEntity(domain.getAdmin()) : null)
                .build();
    }
}
