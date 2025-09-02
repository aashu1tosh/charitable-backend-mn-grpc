package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;

import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;

@MappedEntity
@Table(name = "auth")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private String role;
}
