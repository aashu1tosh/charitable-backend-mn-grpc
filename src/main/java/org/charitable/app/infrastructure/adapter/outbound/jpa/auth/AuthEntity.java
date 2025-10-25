package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;

import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.charitable.app.domain.model.Role;
import org.charitable.app.domain.model.auth.AuthStatus;
import org.charitable.app.infrastructure.adapter.outbound.jpa.admin.AdminEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.organization.OrganizationEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.user.UserEntity;

@Entity
@Table(name = "auth")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthEntity extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotNull
    private String email;

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Column(name="phone_number", nullable = false, unique = true)
    @NotNull
    private String phoneNumber;

    @Column(name = "role", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_email_verified", nullable = false)
    @NotNull
    private Boolean isEmailVerified;

    @Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AuthStatus status = AuthStatus.ACTIVE;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "organization_id", nullable = true)
    private OrganizationEntity organization;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "admin_id", nullable = true)
    private AdminEntity admin;
}
