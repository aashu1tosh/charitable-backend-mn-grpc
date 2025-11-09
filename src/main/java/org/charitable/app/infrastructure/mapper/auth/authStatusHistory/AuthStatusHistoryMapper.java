package org.charitable.app.infrastructure.mapper.auth.authStatusHistory;

import org.charitable.app.domain.entity.auth.authStatusHistory.AuthStatusHistory;
import org.charitable.app.infrastructure.adapter.outbound.jpa.auth.authStatusHistory.AuthStatusHistoryEntity;
import org.charitable.app.infrastructure.mapper.auth.AuthMapper;

public class AuthStatusHistoryMapper {
    public static AuthStatusHistory mapToDomain(AuthStatusHistoryEntity entity) {
        var domain =  AuthStatusHistory.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .auth(AuthMapper.mapToDomain(entity.getAuth()))
                .status(entity.getStatus())
                .build();
        return domain;
    }
}
