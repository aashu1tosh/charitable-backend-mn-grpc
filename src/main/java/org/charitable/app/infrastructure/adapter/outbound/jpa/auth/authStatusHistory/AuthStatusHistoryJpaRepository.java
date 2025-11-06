package org.charitable.app.infrastructure.adapter.outbound.jpa.auth.authStatusHistory;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Repository
public interface AuthStatusHistoryJpaRepository extends JpaRepository<AuthStatusHistory, UUID> {
}
