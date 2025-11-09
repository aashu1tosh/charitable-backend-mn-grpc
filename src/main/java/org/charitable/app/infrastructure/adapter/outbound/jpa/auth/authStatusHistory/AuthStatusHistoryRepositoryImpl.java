package org.charitable.app.infrastructure.adapter.outbound.jpa.auth.authStatusHistory;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.charitable.app.common.utils.PrintUtils;
import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.authStatusHistory.AuthStatusHistory;
import org.charitable.app.domain.model.auth.AuthStatus;
import org.charitable.app.domain.port.outbound.auth.authStatusHistory.AuthStatusHistoryRepository;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;
import org.charitable.app.infrastructure.mapper.auth.authStatusHistory.AuthStatusHistoryMapper;

@Slf4j
@AllArgsConstructor
@Singleton
public class AuthStatusHistoryRepositoryImpl implements AuthStatusHistoryRepository {

    private final AuthStatusHistoryJpaRepository authStatusHistoryJpaRepository;


    @Override
    public AuthStatusHistory save(Auth auth, AuthStatus status) {
        var entity = AuthStatusHistoryEntity.builder()
                .auth(AuthMapper.mapToEntity(auth))
                .status(status)
                .build();
        log.info("Saving AuthStatusHistory for {}", PrintUtils.prettyPrint(entity));
        return AuthStatusHistoryMapper.mapToDomain(entity);
    }


}
