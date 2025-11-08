package org.charitable.app.infrastructure.adapter.outbound.jpa.auth.authStatusHistory;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.charitable.app.domain.model.auth.AuthStatus;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.AuthEntity;
import org.charitable.app.infrastructure.adapter.outbound.jpa.base.BaseEntity;

@Entity
@Table(name = "auth_status_history")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class AuthStatusHistoryEntity extends BaseEntity {

    @Column(name = "status", nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", nullable = false)
    private AuthEntity auth;
}
