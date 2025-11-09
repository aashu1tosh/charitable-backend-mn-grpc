package org.charitable.app.domain.port.outbound.auth.authStatusHistory;

import org.charitable.app.domain.entity.auth.Auth;
import org.charitable.app.domain.entity.auth.authStatusHistory.AuthStatusHistory;
import org.charitable.app.domain.model.auth.AuthStatus;

public interface AuthStatusHistoryRepository {
    AuthStatusHistory save(Auth auth, AuthStatus status);
}
