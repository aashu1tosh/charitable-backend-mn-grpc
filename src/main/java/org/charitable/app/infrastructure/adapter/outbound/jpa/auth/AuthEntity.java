package org.charitable.app.infrastructure.adapter.outbound.jpa.auth;

import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;

@Entity
@Table(name = "auth")
@Getter
@Setter
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
